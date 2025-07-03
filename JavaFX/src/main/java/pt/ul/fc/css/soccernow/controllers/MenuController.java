package pt.ul.fc.css.soccernow.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController extends ControllerWithModel {

    @FXML
    private Button gerirUtilizadores;

    @FXML
    private Button criarClube;

    @FXML
    private Button gerirClubes;

    @FXML
    private Button criarEquipa;

    @FXML
    private Button gerirEquipas;

    @FXML
    private Button criarJogo;

    @FXML
    private Button registarResultado;

    @FXML
    private Button criarCampeonato;

    @FXML
    private Button gerirCampeonatos;

    @FXML
    void gerirUtilizadores(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/gerir_utilizadores.fxml", "SoccerNow: Gestão de utilizadores", model);
    }

    @FXML
    void criarClube(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/criar_clube.fxml", "SoccerNow: Criação de clubes", model);
    }

    @FXML
    void gerirClubes(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/gerir_clubes.fxml", "SoccerNow: Gestão de clubes", model);
    }

    @FXML
    void criarEquipa(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/criar_equipa.fxml", "SoccerNow: Criação de equipas", model);
    }

    @FXML
    void gerirEquipas(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/gerir_equipas.fxml", "SoccerNow: Gestão de equipas", model);
    }

    @FXML
    void criarJogo(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/criar_jogo.fxml", "SoccerNow: Criação de jogos", model);
    }

    @FXML
    void registarResultado(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/registar_resultados.fxml", "SoccerNow: Registo de resultados", model);
    }

    @FXML
    void criarCampeonato(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/criar_campeonato.fxml", "SoccerNow: Criação de campeonatos", model);
    }

    @FXML
    void gerirCampeonatos(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/gerir_campeonatos.fxml", "SoccerNow: Gestão de campeonatos", model);
    }

    @FXML
    void cancelarJogo(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/cancelar_jogos.fxml", "SoccerNow: Cancelamento de jogos", model);
    }

    @FXML
    void logout(ActionEvent event) {
        model.setAuthenticatedUtilizador(null);
        switchScene("/pt/ul/fc/css/soccernow/view/init.fxml", "SoccerNow", model);
    }

    @FXML
    void quit(ActionEvent event) {
        System.exit(0);
    }
}
