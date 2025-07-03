package pt.ul.fc.css.soccernow.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class InitController extends ControllerWithModel {

    @FXML
    void login(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/login.fxml", "SoccerNow: Login", model);
    }

    @FXML
    void registarArbitro(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/registar_arbitro.fxml", "SoccerNow: Registar novo árbitro", model);
    }

    @FXML
    void registarJogador(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/registar_jogador.fxml", "SoccerNow: Registar novo jogador", model);
    }

    @FXML
    void quit(ActionEvent event) {
        System.exit(0);
    }
}
