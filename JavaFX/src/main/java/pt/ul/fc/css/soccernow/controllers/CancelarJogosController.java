package pt.ul.fc.css.soccernow.controllers;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.CampeonatoDTO;
import pt.ul.fc.css.soccernow.dto.JogoDTO;

public class CancelarJogosController extends ControllerWithModel {
    
    @FXML
    private ListView<CampeonatoDTO> listCampeonatos;

    @FXML
    private ListView<JogoDTO> listJogos;

    @FXML
    public void initialize() throws Exception {
        listCampeonatos.setItems(FXCollections.observableArrayList(ApiClient.getCampeonatos()));
        listCampeonatos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            try {
                refreshJogoList(newSelection);
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Erro!", "Erro na verificação dos jogos de um campeonato", e.getMessage());
            }
        });
    }

    @FXML
    void cancelar(ActionEvent event) throws Exception {
        CampeonatoDTO campeonato = listCampeonatos.getSelectionModel().getSelectedItem();
        JogoDTO jogo = listJogos.getSelectionModel().getSelectedItem();
        if(campeonato != null && jogo != null) {
            try {
                ApiClient.cancelarJogo(campeonato.getId(),jogo.getId());
                showAlert(AlertType.INFORMATION, "Jogo cancelado", "Jogo cancelado com sucesso",
                    String.format("O jogo do campeonato %s foi cancelado com sucesso", campeonato.getNome()));
                refreshJogoList(campeonato); 
            } catch(RuntimeException e) {
                showAlert(AlertType.ERROR, "Erro!", "Erro ao cancelar jogo!", e.getMessage());
            }
        }
    }

    private void refreshJogoList(CampeonatoDTO campeonato) throws Exception {
        List<JogoDTO> jogosCampeonato = ApiClient.getJogos().stream()
            .filter(jogo -> campeonato.getJogos().contains(jogo.getId()))
            .filter(jogo -> jogo.getEstadoAtual().equals("AGENDADO")).toList();
        listJogos.setItems(FXCollections.observableArrayList(jogosCampeonato));
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
