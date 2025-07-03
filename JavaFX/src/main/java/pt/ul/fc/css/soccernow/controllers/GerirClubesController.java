package pt.ul.fc.css.soccernow.controllers;

import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.CampeonatoDTO;
import pt.ul.fc.css.soccernow.dto.ClubeDTO;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;

public class GerirClubesController extends ControllerWithModel {
    
    @FXML
    private ListView<ClubeDTO> listClubes;

    @FXML
    private Label nomeLabel;

    @FXML
    private Label conquistasLabel;

    @FXML
    private ListView<JogadorDTO> jogadoresList;

    @FXML
    private ListView<EquipaDTO> equipasList;

    @FXML
    private ListView<CampeonatoDTO> campeonatosList;

    @FXML
    public void initialize() throws Exception {
        refreshList();
        listClubes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            try {
                setSelectedClube(newSelection);
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Erro!", "Erro na verificação de um clube", e.getMessage());
            }
        });
    }

    private void setSelectedClube(ClubeDTO clube) throws Exception {
        if(clube != null) {
            Long clubeID = clube.getId();

            nomeLabel.setText(clube.getNome());
            jogadoresList.setItems(FXCollections.observableArrayList(ApiClient.getJogadoresByClube(clubeID)));

            List<EquipaDTO> equipas = ApiClient.getEquipas().stream()
                .filter(equipa -> equipa.getClubeID().equals(clubeID)).toList();
            equipasList.setItems(FXCollections.observableArrayList(equipas));

            List<CampeonatoDTO> campeonatos = ApiClient.getCampeonatos().stream()
                .filter(campeonato -> campeonato.getClubes().contains(clubeID)).toList();
            campeonatosList.setItems(FXCollections.observableArrayList(campeonatos));

            Map<String,Boolean> conquistas = clube.getConquistas();
                conquistasLabel.setText(String.format("• Primeiro lugar: %s\n• Segundo lugar: %s\n• Terceiro lugar: %s",
                    conquistas.get("PRIMEIRO_LUGAR") ? "✔" : "✘" , conquistas.get("SEGUNDO_LUGAR") ? "✔" : "✘", conquistas.get("TERCEIRO_LUGAR") ? "✔" : "✘"));
        } else {
            nomeLabel.setText("");
            jogadoresList.getItems().clear();
            equipasList.getItems().clear();
            campeonatosList.getItems().clear();
            conquistasLabel.setText("");
        }
    }

    @FXML
    void atualizar(ActionEvent event) {
        ClubeDTO selected = listClubes.getSelectionModel().getSelectedItem();
        if(selected != null) {
            model.setSelectedClube(selected);
            switchScene("/pt/ul/fc/css/soccernow/view/atualizar_clube.fxml", "SoccerNow: Atualizar clube", model);
        } else {
            showAlert(AlertType.ERROR, "Erro!", "Erro na atualização de um clube",
                "Por favor selecione um clube para atualizar");
        }
    }

    @FXML
    void remover(ActionEvent event) throws Exception {
        ClubeDTO clube = listClubes.getSelectionModel().getSelectedItem();
        if(clube != null) {
            try {
                ApiClient.removeClube(clube.getId());
                showAlert(AlertType.INFORMATION, "Clube removido", "Clube removido com sucesso",
                    String.format("O clube com o nome %s foi removido com sucesso", clube.getNome()));
                listClubes.getSelectionModel().clearSelection();
                refreshList();
            } catch(RuntimeException e) {
                showAlert(AlertType.ERROR, "Erro!", "Erro ao remover clube!", e.getMessage());
            }
        }
    }

    private void refreshList() throws Exception {
        listClubes.setItems(FXCollections.observableArrayList(ApiClient.getClubes()));
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
