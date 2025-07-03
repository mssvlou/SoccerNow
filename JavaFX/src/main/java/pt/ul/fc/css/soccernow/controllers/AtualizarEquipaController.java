package pt.ul.fc.css.soccernow.controllers;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.model.DataModel;

public class AtualizarEquipaController extends ControllerWithModel {

    private EquipaDTO equipa;

    @FXML
    private TextField nomeField;

    List<ComboBox<JogadorDTO>> jogadorComboBoxes;

    @FXML
    private ComboBox<JogadorDTO> jogador1ComboBox;

    @FXML
    private ComboBox<JogadorDTO> jogador2ComboBox;

    @FXML
    private ComboBox<JogadorDTO> jogador3ComboBox;

    @FXML
    private ComboBox<JogadorDTO> jogador4ComboBox;

    @FXML
    private ComboBox<JogadorDTO> jogador5ComboBox;

    // override initModel to avoid NullPointerException
    @Override
    public void initModel(Stage stage, DataModel model) {
        if (super.model != null) 
            throw new IllegalStateException("Model already initialized");
        super.stage = stage;
        super.model = model;

        jogadorComboBoxes = List.of(jogador1ComboBox, jogador2ComboBox, jogador3ComboBox, jogador4ComboBox, jogador5ComboBox);

        equipa = super.model.getSelectedEquipa();
        nomeField.setText(equipa.getNome());

        try {
            List<JogadorDTO> jogadores = ApiClient.getJogadoresByClube(equipa.getClubeID());
            List<JogadorDTO> jogadoresEquipa = jogadores.stream()
                .filter(j -> equipa.getJogadorIDs().contains(j.getId())).toList();
            for(int i = 0; i < jogadorComboBoxes.size(); i++) {
                ComboBox<JogadorDTO> comboBox = jogadorComboBoxes.get(i);
                comboBox.setItems(FXCollections.observableArrayList(jogadores));
                comboBox.setValue(jogadoresEquipa.get(i));
            }
        } catch(Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro na obtenção dos jogadores", e.getMessage());
        }
    }

    @FXML
    void submeter(ActionEvent event) {
        if(!validateTextField(nomeField, "Nome"))
            return;
        equipa.setNome(nomeField.getText().trim());

        List<Long> jogadorIDs = jogadorComboBoxes.stream()
            .map(ComboBox::getValue).filter(j -> j != null).map(JogadorDTO::getId).toList();
        equipa.setJogadorIDs(jogadorIDs);

        try {
            ApiClient.updateEquipa(equipa);
            showAlert(AlertType.INFORMATION, "Equipa atualizada", "Equipa atualizada com sucesso",
                String.format("A equipa com o nome %s foi atualizada com sucesso", equipa.getNome()));
            back(event);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro na atualização da equipa", e.getMessage());
        }
    }

    @FXML
    void cancelar(ActionEvent event) {
        back(event);
    }

    @FXML
    void back(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/gerir_equipas.fxml", "SoccerNow: Gestão de equipas", model);
    }

    @FXML
    void quit(ActionEvent event) {
        System.exit(0);
    }
}
