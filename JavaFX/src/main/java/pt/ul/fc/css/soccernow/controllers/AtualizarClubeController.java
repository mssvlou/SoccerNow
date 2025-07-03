package pt.ul.fc.css.soccernow.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.ClubeDTO;
import pt.ul.fc.css.soccernow.model.DataModel;

public class AtualizarClubeController extends ControllerWithModel {

    private ClubeDTO clube;
    
    @FXML
    private TextField nomeField;

    // override initModel to avoid NullPointerException
    @Override
    public void initModel(Stage stage, DataModel model) {
        if (super.model != null) 
            throw new IllegalStateException("Model already initialized");
        super.stage = stage;
        super.model = model;

        clube = super.model.getSelectedClube();
        nomeField.setText(clube.getNome());
    }

    @FXML
    void submeter(ActionEvent event) {
        if(!validateTextField(nomeField, "Nome"))
            return;
        clube.setNome(nomeField.getText().trim());

        try {
            ApiClient.updateClube(clube);
            showAlert(AlertType.INFORMATION, "Clube atualizado", "Clube atualizado com sucesso",
                String.format("O clube %s foi atualizado com sucesso", clube.getNome()));
            back(event);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro na atualização de um clube", e.getMessage());
        }
    }

    @FXML
    void cancelar(ActionEvent event) {
        back(event);
    }

    @FXML
    void back(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/gerir_clubes.fxml", "SoccerNow: Gestão de clubes", model);
    }

    @FXML
    void quit(ActionEvent event) {
        System.exit(0);
    }
}
