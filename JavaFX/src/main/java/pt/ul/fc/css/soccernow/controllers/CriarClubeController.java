package pt.ul.fc.css.soccernow.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.ClubeDTO;

public class CriarClubeController extends ControllerWithModel {

    @FXML
    private TextField nomeField;

    @FXML
    void criar(ActionEvent event) {
        if(!validateTextField(nomeField, "Nome"))
            return;
        String nome = nomeField.getText().trim();

        ClubeDTO clubeDTO = new ClubeDTO();
        clubeDTO.setNome(nome);

        try {
            ApiClient.criarClube(clubeDTO);
            showAlert(AlertType.INFORMATION, "Clube criado", "Clube criado com sucesso",
                String.format("O clube com o nome %s foi criado com sucesso", nome));
            back(event);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro na criação de um novo clube", e.getMessage());
        }
    }

    @FXML
    void back(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/menu.fxml", "SoccerNow: Menu", model);
    }

    @FXML
    void quit(ActionEvent event) {
        System.exit(0);
    }
}
