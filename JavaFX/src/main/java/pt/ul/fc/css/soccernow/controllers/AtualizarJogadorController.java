package pt.ul.fc.css.soccernow.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.ClubeDTO;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.model.DataModel;

public class AtualizarJogadorController extends ControllerWithModel {

    JogadorDTO jogador;

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
        jogador = (JogadorDTO) super.model.getSelectedUtilizador();

        nomeField.setText(jogador.getNome());
        emailField.setText(jogador.getEmail());

        posicao = new ToggleGroup();
        guardaRedesRadio.setToggleGroup(posicao);
        fixoRadio.setToggleGroup(posicao);
        alaRadio.setToggleGroup(posicao);
        pivoRadio.setToggleGroup(posicao);

        switch (jogador.getPosicao()) {
            case "GUARDA_REDES":
                guardaRedesRadio.setSelected(true);
                break;
            case "FIXO":
                fixoRadio.setSelected(true);
                break;
            case "ALA":
                alaRadio.setSelected(true);
                break;
            case "PIVO":
                pivoRadio.setSelected(true);
                break;
        }

        try {
            List<ClubeDTO> clubes = new ArrayList<>();
            clubes.add(null); // allow empty option
            clubes.addAll(ApiClient.getClubes()); 
            clubeComboBox.setItems(FXCollections.observableArrayList(clubes));

            if(jogador.getClubeID() != null) {
                ClubeDTO clube = clubes.stream()
                    .filter(c -> c != null && jogador.getClubeID().equals(c.getId()))
                    .findFirst().get();
                clubeComboBox.setValue(clube);
            }
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro na atualização de um utilizador", e.getMessage());
        }
    }

    @FXML
    void submeter(ActionEvent event) {
        if(!validateTextField(nomeField, "Nome"))
            return;
        jogador.setNome(nomeField.getText().trim());

        if(!validateTextField(emailField, "E-mail"))
            return;
        jogador.setEmail(emailField.getText().trim());

        jogador.setPassword(passwordField.getText().trim());
        jogador.setClubeID(clubeComboBox.getValue() == null ? 
            null : clubeComboBox.getValue().getId());

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
        jogador.setPosicao(posicao);
        
        try {
            ApiClient.updateJogador(jogador);
            showAlert(AlertType.INFORMATION, "Jogador atualizado", "Jogador atualizado com sucesso",
                String.format("O jogador %s foi atualizado com sucesso", jogador.getNome()));
            back(event);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro na atualização de um jogador", e.getMessage());
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
