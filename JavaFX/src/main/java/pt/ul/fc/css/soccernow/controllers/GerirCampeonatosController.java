package pt.ul.fc.css.soccernow.controllers;

import java.util.ArrayList;
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
import pt.ul.fc.css.soccernow.dto.JogoDTO;

public class GerirCampeonatosController extends ControllerWithModel {
    
    @FXML
    private ListView<CampeonatoDTO> listCampeonatos;

    @FXML
    private Label nomeLabel;

    @FXML
    private Label formatoLabel;

    @FXML
    private Label concluidoLabel;

    @FXML
    private ListView<ClubeDTO> clubesList;

    @FXML
    private ListView<JogoDTO> jogosList;

    @FXML
    private ListView<String> pontuacoes;

    @FXML
    private ListView<String> classificacoes;

    @FXML
    public void initialize() throws Exception {
        refreshList();
        listCampeonatos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            try {
                setSelectedCampeonato(newSelection);
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Erro!", "Erro na verificação de um campeonato", e.getMessage());
            }
        });
    }

    private void setSelectedCampeonato(CampeonatoDTO campeonato) throws Exception {
        if(campeonato != null) {
            nomeLabel.setText(campeonato.getNome());
            formatoLabel.setText(campeonato.getFormato());
            concluidoLabel.setText(campeonato.isConcluido() ? "✔" : "✘");

            List<ClubeDTO> clubes = ApiClient.getClubes().stream()
                .filter(clube -> campeonato.getClubes().contains(clube.getId())).toList();
            clubesList.setItems(FXCollections.observableArrayList(clubes));

            List<JogoDTO> jogos = ApiClient.getJogos().stream()
                .filter(jogo -> campeonato.getJogos().contains(jogo.getId())).toList();
            jogosList.setItems(FXCollections.observableArrayList(jogos));

            pontuacoes.setItems(FXCollections.observableArrayList(mapToStringList(campeonato.getPontuacao())));
            classificacoes.setItems(FXCollections.observableArrayList(mapToStringList(campeonato.getClassificacao())));
        } else {
            nomeLabel.setText("");
            formatoLabel.setText("");
            concluidoLabel.setText("");
            clubesList.getItems().clear();
            jogosList.getItems().clear();
            pontuacoes.getItems().clear();
            classificacoes.getItems().clear();
        }
    }

    @FXML
    void atualizar(ActionEvent event) {
        CampeonatoDTO selected = listCampeonatos.getSelectionModel().getSelectedItem();
        if(selected != null) {
            model.setSelectedCampeonato(selected);
            switchScene("/pt/ul/fc/css/soccernow/view/atualizar_campeonato.fxml", "SoccerNow: Atualizar campeonato", model);
        } else {
            showAlert(AlertType.ERROR, "Erro!", "Erro na atualização de um campeonato",
                "Por favor selecione um campeonato para atualizar");
        }
    }

    @FXML
    void remover(ActionEvent event) throws Exception {
        CampeonatoDTO campeonato = listCampeonatos.getSelectionModel().getSelectedItem();
        if(campeonato != null) {
            try {
                ApiClient.removeCampeonato(campeonato.getId());
                showAlert(AlertType.INFORMATION, "Campeonato removido", "Campeonato removido com sucesso",
                    String.format("O campeonato com o nome %s foi removido com sucesso", campeonato.getNome()));
                listCampeonatos.getSelectionModel().clearSelection();
                refreshList();
            } catch(RuntimeException e) {
                showAlert(AlertType.ERROR, "Erro!", "Erro ao remover campeonato!", e.getMessage());
            }
        }
    }

    private void refreshList() throws Exception {
        listCampeonatos.setItems(FXCollections.observableArrayList(ApiClient.getCampeonatos()));
    }

    private List<String> mapToStringList(Map<Long,Integer> map) {
        List<String> entryList = new ArrayList<>();
        for(Map.Entry<Long,Integer> entry : map.entrySet()) {
            String nomeClube = clubesList.getItems().stream()
                .filter(clube -> clube.getId().equals(entry.getKey()))
                .findFirst().get().getNome();
            entryList.add(String.format("%s: %d", nomeClube, entry.getValue()));
        }
        return entryList;
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
