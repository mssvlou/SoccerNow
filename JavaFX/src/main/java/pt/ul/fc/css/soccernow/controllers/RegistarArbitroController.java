package pt.ul.fc.css.soccernow.controllers;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;

public class RegistarArbitroController extends ControllerWithModel {

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

    @FXML
    public void initialize() {
        certificado = new ToggleGroup();
        simRadio.setToggleGroup(certificado);
        naoRadio.setToggleGroup(certificado);
    }

    @FXML
    void registar(ActionEvent event) {
        if(!validateTextField(nomeField, "Nome"))
            return;
        String nome = nomeField.getText().trim();

        if(!validateTextField(emailField, "E-mail"))
            return;
        String email = emailField.getText().trim();
        
        String password = passwordField.getText().trim();
        boolean certificado = ((RadioButton) this.certificado.getSelectedToggle()).equals(simRadio);

        ArbitroDTO arbitroDTO = new ArbitroDTO(0L, nome, email, password, certificado, new ArrayList<>());
        try {
            arbitroDTO.setId(ApiClient.registarArbitro(arbitroDTO));
            model.setAuthenticatedUtilizador(arbitroDTO);
            switchScene("/pt/ul/fc/css/soccernow/view/menu.fxml", "SoccerNow: Menu", model);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro no registo de um novo árbitro", e.getMessage());
        }
    }

    @FXML
    void back(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/init.fxml", "SoccerNow", model);
    }

    @FXML
    void quit(ActionEvent event) {
        System.exit(0);
    }
}
