package pt.ul.fc.css.soccernow.controllers;

import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.dto.JogoDTO;
import pt.ul.fc.css.soccernow.dto.UtilizadorDTO;

public class GerirUtilizadoresController extends ControllerWithModel {

    @FXML
    private ListView<UtilizadorDTO> listUtilizadores;

    @FXML
    private Label nomeLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private VBox jogadorInfo;

    @FXML
    private Label posicaoLabel;

    @FXML
    private Label golosLabel;

    @FXML
    private Label cartoesLabel;

    @FXML
    private Label clubeLabel;

    @FXML
    private ListView<EquipaDTO> equipasList;

    @FXML
    private VBox arbitroInfo;

    @FXML
    private Label certificadoLabel;

    @FXML
    private ListView<JogoDTO> jogosList;

    @FXML
    public void initialize() throws Exception {
        refreshList();
        listUtilizadores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            try {
                setSelectedUtilizador(newSelection);
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Erro!", "Erro na verificação de um utilizador", e.getMessage());
            }
        });
    }

    private void setSelectedUtilizador(UtilizadorDTO utilizador) throws Exception{
        if(utilizador != null) {
            nomeLabel.setText(utilizador.getNome());
            emailLabel.setText(utilizador.getEmail());

            if(utilizador instanceof JogadorDTO jogador) {
                posicaoLabel.setText(jogador.getPosicao());
                golosLabel.setText(String.valueOf(jogador.getNumGolosMarcados()));

                Map<String,Integer> cartoes = jogador.getCartoesRecebidos();
                cartoesLabel.setText(String.format("• Amarelos: %d\n• Vermelhos: %d",
                    cartoes.getOrDefault("AMARELO", 0), cartoes.getOrDefault("VERMELHO",0)));

                Long clubeID = jogador.getClubeID();
                if(clubeID != null)
                    clubeLabel.setText(ApiClient.getClube(clubeID).toString());
                else
                    clubeLabel.setText("Sem clube");

                List<EquipaDTO> equipas = ApiClient.getEquipas().stream()
                    .filter(equipa -> jogador.getEquipaIDs().contains(equipa.getId())).toList();
                equipasList.setItems(FXCollections.observableArrayList(equipas));

                jogadorInfo.setVisible(true);
                arbitroInfo.setVisible(false);
            } else {
                ArbitroDTO arbitro = (ArbitroDTO) utilizador;

                certificadoLabel.setText(arbitro.isCertificado() ? "✔" : "✘");

                List<JogoDTO> jogos = ApiClient.getJogos().stream()
                    .filter(jogo -> arbitro.getJogoIDs().contains(jogo.getId())).toList();
                jogosList.setItems(FXCollections.observableArrayList(jogos));

                jogadorInfo.setVisible(false);
                arbitroInfo.setVisible(true);
            }
        } else {
            nomeLabel.setText("");
            emailLabel.setText("");

            jogadorInfo.setVisible(false);
            arbitroInfo.setVisible(false);
        }
    }

    @FXML
    void atualizar(ActionEvent event) {
        UtilizadorDTO selected = listUtilizadores.getSelectionModel().getSelectedItem();
        if(selected != null) {
            model.setSelectedUtilizador(selected);
            if(selected instanceof JogadorDTO)
                switchScene("/pt/ul/fc/css/soccernow/view/atualizar_jogador.fxml", "SoccerNow: Atualizar jogador", model);
            else
                switchScene("/pt/ul/fc/css/soccernow/view/atualizar_arbitro.fxml", "SoccerNow: Atualizar árbitro", model);
        } else {
            showAlert(AlertType.ERROR, "Erro!", "Erro na atualização de um utilizador",
                "Por favor selecione um utilizador para atualizar");
        }
    }

    @FXML
    void remover(ActionEvent event) throws Exception {
        UtilizadorDTO utilizador = listUtilizadores.getSelectionModel().getSelectedItem();
        if(utilizador != null) {
            try {
                if(utilizador instanceof JogadorDTO)
                    ApiClient.removeJogador(utilizador.getId());
                else
                    ApiClient.removeArbitro(utilizador.getId());
                showAlert(AlertType.INFORMATION, "Utilizador removido", "Utilizador removido com sucesso",
                    String.format("O utilizador com o nome %s foi removido com sucesso", utilizador.getNome()));
                listUtilizadores.getSelectionModel().clearSelection();
            } catch(RuntimeException e) {
                showAlert(AlertType.ERROR, "Erro!", "Erro ao remover utilizador!", e.getMessage());
            }
            refreshList();
        }
    }

    private void refreshList() throws Exception {
        listUtilizadores.setItems(FXCollections.observableArrayList(ApiClient.getUtilizadores()));
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