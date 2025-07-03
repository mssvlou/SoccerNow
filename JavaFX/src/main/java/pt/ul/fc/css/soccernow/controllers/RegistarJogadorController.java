package pt.ul.fc.css.soccernow.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.ClubeDTO;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;

public class RegistarJogadorController extends ControllerWithModel {

    @FXML
    private TextField nomeField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private ToggleGroup posicao;

    @FXML
    private RadioButton guardaRedesRadio;

    @FXML
    private RadioButton fixoRadio;

    @FXML
    private RadioButton alaRadio;

    @FXML
    private RadioButton pivoRadio;

    @FXML
    private ComboBox<ClubeDTO> clubeComboBox;

    @FXML
    public void initialize() throws Exception {
        posicao = new ToggleGroup();
        guardaRedesRadio.setToggleGroup(posicao);
        fixoRadio.setToggleGroup(posicao);
        alaRadio.setToggleGroup(posicao);
        pivoRadio.setToggleGroup(posicao);
        
        List<ClubeDTO> clubes = new ArrayList<>();
        clubes.add(null); // allow empty option
        clubes.addAll(ApiClient.getClubes()); 
        clubeComboBox.setItems(FXCollections.observableArrayList(clubes));
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
        Long clubeID = clubeComboBox.getValue() == null ? null : clubeComboBox.getValue().getId();

        String posicao;
        RadioButton selectedPosicaoRadio = (RadioButton) this.posicao.getSelectedToggle();
        if(selectedPosicaoRadio.equals(guardaRedesRadio))
            posicao = "GUARDA_REDES";
        else if(selectedPosicaoRadio.equals(fixoRadio))
            posicao = "FIXO";
        else if(selectedPosicaoRadio.equals(alaRadio))
            posicao = "ALA";
        else
            posicao = "PIVO";

        JogadorDTO jogadorDTO = new JogadorDTO(0L, nome, email, password, posicao, 0, new HashMap<>(), clubeID, new ArrayList<>());
        try {
            jogadorDTO.setId(ApiClient.registarJogador(jogadorDTO));
            model.setAuthenticatedUtilizador(jogadorDTO);
            switchScene("/pt/ul/fc/css/soccernow/view/menu.fxml", "SoccerNow: Menu", model);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro no registo de um novo jogador", e.getMessage());
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
