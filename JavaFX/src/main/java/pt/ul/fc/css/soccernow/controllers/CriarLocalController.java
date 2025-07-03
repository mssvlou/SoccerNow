package pt.ul.fc.css.soccernow.controllers;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.LocalDTO;

public class CriarLocalController extends ControllerWithModel {

    @FXML
    private TextField nomeField;

    @FXML
    private TextField moradaField;

    @FXML
    private TextField codigoPostalField;

    @FXML
    void criar(ActionEvent event) {
        if(!validateTextField(nomeField, "Nome"))
            return;
        String nome = nomeField.getText().trim();

        if(!validateTextField(moradaField, "Morada"))
            return;
        String morada = moradaField.getText().trim();
        
        if(!validateTextField(codigoPostalField, "Código postal"))
            return;
        String codigoPostal = codigoPostalField.getText().trim();

        try {
            ApiClient.criarLocal(new LocalDTO(0L, nome, morada, codigoPostal, new ArrayList<>()));
            showAlert(AlertType.INFORMATION, "Local criado", "Local criado com sucesso",
                String.format("O local com o nome %s foi criado com sucesso", nome));
            back(event);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro na criação de um novo local", e.getMessage());
        }
    }

    @FXML
    void back(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/criar_jogo.fxml", "SoccerNow: Criação de jogos", model);
    }

    @FXML
    void quit(ActionEvent event) {
        System.exit(0);
    }
}
