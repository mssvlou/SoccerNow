package pt.ul.fc.css.soccernow.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.ClubeDTO;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;

public class CriarEquipaController extends ControllerWithModel {

    @FXML
    private TextField nomeField;

    @FXML
    private ComboBox<ClubeDTO> clubeComboBox;

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
    
    @FXML
    public void initialize() throws Exception {
        clubeComboBox.setItems(FXCollections.observableArrayList(ApiClient.getClubes()));
        jogadorComboBoxes = List.of(jogador1ComboBox, jogador2ComboBox, jogador3ComboBox, jogador4ComboBox, jogador5ComboBox);
    }

    @FXML
    void criar(ActionEvent event) {
        if(!validateTextField(nomeField, "Nome"))
            return;
        String nome = nomeField.getText().trim();

        if(!validateComboBox(clubeComboBox, "Clube"))
            return;
        Long clubeID = clubeComboBox.getValue().getId();
        List<Long> jogadorIDs = jogadorComboBoxes.stream()
            .map(ComboBox::getValue).filter(j -> j != null).map(JogadorDTO::getId).toList();

        EquipaDTO equipaDTO = new EquipaDTO(0L, nome, 0, 0, 0, clubeID, jogadorIDs, new ArrayList<>());
        try {
            ApiClient.criarEquipa(equipaDTO);
            showAlert(AlertType.INFORMATION, "Equipa criada", "Equipa criada com sucesso",
                String.format("A equipa com o nome %s foi criada com sucesso", nome));
            back(event);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro na criação de uma nova equipa", e.getMessage());
        }
    }

    @FXML
    void updateJogadores(ActionEvent event) throws Exception {
        if(clubeComboBox.getValue() != null ) {
            Long clubeID = clubeComboBox.getValue().getId();
            List<JogadorDTO> jogadores = ApiClient.getJogadoresByClube(clubeID);
            for(ComboBox<JogadorDTO> jogadorComboBox : jogadorComboBoxes) 
                jogadorComboBox.setItems(FXCollections.observableArrayList(jogadores));
        }
    }

    @FXML
    void back(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/menu.fxml", "SoccerNow: Menu", model);
    }

    @FXML
    void quit(ActionEvent event) {
        System.exit(0);
    }
}
