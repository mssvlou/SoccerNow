package pt.ul.fc.css.soccernow.controllers;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.dto.JogoDTO;

public class GerirEquipasController extends ControllerWithModel {
    
    @FXML
    private ListView<EquipaDTO> listEquipas;

    @FXML
    private Label nomeLabel;

    @FXML
    private Label clubeLabel;

    @FXML
    private ListView<JogadorDTO> jogadoresList;

    @FXML
    private ListView<JogoDTO> jogosList;

    @FXML
    private Label vitoriasLabel;

    @FXML
    private Label empatesLabel;

    @FXML
    private Label derrotasLabel;

    @FXML
    public void initialize() throws Exception {
        refreshList();
        listEquipas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            try {
                setSelectedEquipa(newSelection);
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Erro!", "Erro na verificação de uma equipa", e.getMessage());
            }
        });
    }

    private void setSelectedEquipa(EquipaDTO equipa) throws Exception {
        if(equipa != null) {
            Long equipaID = equipa.getId();
            Long clubeID = equipa.getClubeID();

            nomeLabel.setText(equipa.getNome());
            clubeLabel.setText(ApiClient.getClube(clubeID).toString());

            vitoriasLabel.setText(String.valueOf(equipa.getNumVitorias()));
            empatesLabel.setText(String.valueOf(equipa.getNumEmpates()));
            derrotasLabel.setText(String.valueOf(equipa.getNumDerrotas()));

            List<JogadorDTO> jogadores = ApiClient.getJogadoresByClube(clubeID).stream()
                .filter(jogador -> jogador.getEquipaIDs().contains(equipaID)).toList();
            jogadoresList.setItems(FXCollections.observableArrayList(jogadores));

            List<JogoDTO> jogos = ApiClient.getJogos().stream()
                .filter(jogo -> jogo.getEquipaIDs().contains(equipaID)).toList();
            jogosList.setItems(FXCollections.observableArrayList(jogos));
        } else {
            nomeLabel.setText("");
            clubeLabel.setText("");
            vitoriasLabel.setText("");
            empatesLabel.setText("");
            derrotasLabel.setText("");
            jogadoresList.getItems().clear();
            jogosList.getItems().clear();
        }
    }

    @FXML
    void atualizar(ActionEvent event) {
        EquipaDTO selected = listEquipas.getSelectionModel().getSelectedItem();
        if(selected != null) {
            model.setSelectedEquipa(selected);
            switchScene("/pt/ul/fc/css/soccernow/view/atualizar_equipa.fxml", "SoccerNow: Atualizar equipa", model);
        } else {
            showAlert(AlertType.ERROR, "Erro!", "Erro na atualização de uma equipa",
                "Por favor selecione uma equipa para atualizar");
        }
    }

    @FXML
    void remover(ActionEvent event) throws Exception {
        EquipaDTO equipa = listEquipas.getSelectionModel().getSelectedItem();
        if(equipa != null) {
            ApiClient.removeEquipa(equipa.getId());
            showAlert(AlertType.INFORMATION, "Equipa removida", "Equipa removida com sucesso",
                String.format("A equipa com o nome %s foi removida com sucesso", equipa.getNome()));
            listEquipas.getSelectionModel().clearSelection();
            refreshList();
        }
    }

    private void refreshList() throws Exception {
        listEquipas.setItems(FXCollections.observableArrayList(ApiClient.getEquipas()));
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
