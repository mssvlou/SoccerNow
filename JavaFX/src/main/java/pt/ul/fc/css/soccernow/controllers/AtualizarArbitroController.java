package pt.ul.fc.css.soccernow.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;
import pt.ul.fc.css.soccernow.model.DataModel;

public class AtualizarArbitroController extends ControllerWithModel {

    private ArbitroDTO arbitro;
    
    @FXML
    private TextField nomeField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private ToggleGroup certificado;

    @FXML
    private RadioButton simRadio;

    @FXML
    private RadioButton naoRadio;

    // override initModel & call custom initialize to avoid NullPointerException
    @Override
    public void initModel(Stage stage, DataModel model) {
        if (super.model != null) 
            throw new IllegalStateException("Model already initialized");
        super.stage = stage;
        super.model = model;

        initUI();
    }

    public void initUI() {
        arbitro = (ArbitroDTO) super.model.getSelectedUtilizador();

        nomeField.setText(arbitro.getNome());
        emailField.setText(arbitro.getEmail());

        certificado = new ToggleGroup();
        simRadio.setToggleGroup(certificado);
        naoRadio.setToggleGroup(certificado);

        if(arbitro.isCertificado())
            simRadio.setSelected(true);
        else
            naoRadio.setSelected(true);
    }

    @FXML
    void submeter(ActionEvent event) {
        if(!validateTextField(nomeField, "Nome"))
            return;
        arbitro.setNome(nomeField.getText().trim());

        if(!validateTextField(emailField, "E-mail"))
            return;
        arbitro.setEmail(emailField.getText().trim());

        arbitro.setPassword(passwordField.getText().trim());
        arbitro.setCertificado(((RadioButton) this.certificado.getSelectedToggle()).equals(simRadio));

        try {
            ApiClient.updateArbitro(arbitro);
            showAlert(AlertType.INFORMATION, "Árbitro atualizado", "Árbitro atualizado com sucesso",
                String.format("O árbitro %s foi atualizado com sucesso", arbitro.getNome()));
            back(event);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro na atualização de um árbitro", e.getMessage());
        }
    }

    @FXML
    void cancelar(ActionEvent event) {
        back(event);
    }

    @FXML
    void back(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/gerir_utilizadores.fxml", "SoccerNow: Gestão de utilizadores", model);
    }

    @FXML
    void quit(ActionEvent event) {
        System.exit(0);
    }
}
