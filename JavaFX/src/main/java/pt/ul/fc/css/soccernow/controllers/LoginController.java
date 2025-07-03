package pt.ul.fc.css.soccernow.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.UtilizadorDTO;

public class LoginController extends ControllerWithModel {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    void login(ActionEvent event) {
        if (!validateTextField(emailField, "E-mail"))
            return;
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        try {
            UtilizadorDTO utilizador = ApiClient.login(email, password);
            model.setAuthenticatedUtilizador(utilizador);
            showAlert(AlertType.INFORMATION, "Login", "Login bem-sucedido",
                    String.format("Bem-vindo, %s", utilizador.getNome()));
            switchScene("/pt/ul/fc/css/soccernow/view/menu.fxml", "SoccerNow: Menu", model);

        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro de Login", e.getMessage());
        }
    }

    @FXML
    private void back() {
        switchScene("/pt/ul/fc/css/soccernow/view/init.fxml", "SoccerNow", model);
    }

    @FXML
    void quit(ActionEvent event) {
        System.exit(0);
    }
}
