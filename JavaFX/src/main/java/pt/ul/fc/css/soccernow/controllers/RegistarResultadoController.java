package pt.ul.fc.css.soccernow.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.dto.EstatisticasDTO;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.dto.JogoDTO;

public class RegistarResultadoController extends ControllerWithModel {
    
    @FXML
    private ComboBox<JogoDTO> jogoComboBox;

    private List<ComboBox<String>> cartaoComboBoxes;

    @FXML private Label equipa1Label;
    @FXML private Label equipa1Pontos;
    private List<Label> jogadoresEquipa1;
    private List<Spinner<Integer>> spinnersEquipa1;

    @FXML private Label jogador1Label;
    @FXML private Spinner<Integer> jogador1Spinner;
    @FXML private ComboBox<String> jogador1Cartao;

    @FXML private Label jogador2Label;
    @FXML private Spinner<Integer> jogador2Spinner;
    @FXML private ComboBox<String> jogador2Cartao;

    @FXML private Label jogador3Label;
    @FXML private Spinner<Integer> jogador3Spinner;
    @FXML private ComboBox<String> jogador3Cartao;

    @FXML private Label jogador4Label;
    @FXML private Spinner<Integer> jogador4Spinner;
    @FXML private ComboBox<String> jogador4Cartao;

    @FXML private Label jogador5Label;
    @FXML private Spinner<Integer> jogador5Spinner;
    @FXML private ComboBox<String> jogador5Cartao;

    @FXML private Label equipa2Label;
    @FXML private Label equipa2Pontos;
    private List<Label> jogadoresEquipa2;
    private List<Spinner<Integer>> spinnersEquipa2;

    @FXML private Label jogador6Label;
    @FXML private Spinner<Integer> jogador6Spinner;
    @FXML private ComboBox<String> jogador6Cartao;

    @FXML private Label jogador7Label;
    @FXML private Spinner<Integer> jogador7Spinner;
    @FXML private ComboBox<String> jogador7Cartao;

    @FXML private Label jogador8Label;
    @FXML private Spinner<Integer> jogador8Spinner;
    @FXML private ComboBox<String> jogador8Cartao;

    @FXML private Label jogador9Label;
    @FXML private Spinner<Integer> jogador9Spinner;
    @FXML private ComboBox<String> jogador9Cartao;

    @FXML private Label jogador10Label;
    @FXML private Spinner<Integer> jogador10Spinner;
    @FXML private ComboBox<String> jogador10Cartao;

    @FXML
    public void initialize() throws Exception {
        jogadoresEquipa1 = List.of(jogador1Label, jogador2Label, jogador3Label, jogador4Label, jogador5Label);
        spinnersEquipa1 = List.of(jogador1Spinner, jogador2Spinner, jogador3Spinner, jogador4Spinner, jogador5Spinner);
        
        jogadoresEquipa2 = List.of(jogador6Label, jogador7Label, jogador8Label, jogador9Label, jogador10Label);
        spinnersEquipa2 = List.of(jogador6Spinner, jogador7Spinner, jogador8Spinner, jogador9Spinner, jogador10Spinner);
        
        for(int i = 0; i < 5; i++) {
            Spinner<Integer> spinner1 = spinnersEquipa1.get(i);
            spinner1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99));
            spinner1.valueProperty().addListener((obs, oldValue, newValue) -> {
                int pontos = Integer.parseInt(equipa1Pontos.getText().split(" ")[0]) + newValue - oldValue;
                equipa1Pontos.setText(String.format("%d golos", pontos));
            });

            Spinner<Integer> spinner2 = spinnersEquipa2.get(i);
            spinner2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99));
            spinner2.valueProperty().addListener((obs, oldValue, newValue) -> {
                int pontos = Integer.parseInt(equipa2Pontos.getText().split(" ")[0]) + newValue - oldValue;
                equipa2Pontos.setText(String.format("%d golos", pontos));
            });
        }
        
        cartaoComboBoxes = List.of(
            jogador1Cartao, jogador2Cartao, jogador3Cartao, jogador4Cartao, jogador5Cartao,
            jogador6Cartao, jogador7Cartao, jogador8Cartao, jogador9Cartao, jogador10Cartao
        );

        for(ComboBox<String> cartaoComboBox : cartaoComboBoxes) {
            cartaoComboBox.setItems(FXCollections.observableArrayList(List.of("Nenhum", "Amarelo", "Vermelho")));
            cartaoComboBox.setValue("Nenhum");
        }
        List<JogoDTO> jogos = ApiClient.getJogos().stream().filter(jogo -> !jogo.getEstadoAtual().equals("CONCLUIDO") && !jogo.getEstadoAtual().equals("CANCELADO")).toList();
        jogoComboBox.setItems(FXCollections.observableArrayList(jogos));
    }

    @FXML
    void updateInfo() throws Exception {
        Map<Long,String> nomesJogadores = ApiClient.getUtilizadores().stream()
            .filter(u -> u instanceof JogadorDTO).map(u -> (JogadorDTO) u)
            .collect(Collectors.toMap(JogadorDTO::getId, JogadorDTO::getNome));

        JogoDTO jogo = jogoComboBox.getValue();
        if(jogo != null) {
            EquipaDTO equipa1 = ApiClient.getEquipa(jogo.getEquipaIDs().get(0));
            EquipaDTO equipa2 = ApiClient.getEquipa(jogo.getEquipaIDs().get(1));

            equipa1Label.setText(equipa1.getNome());
            equipa2Label.setText(equipa2.getNome());

            for(int i = 0; i < 5; i++) {
                jogadoresEquipa1.get(i).setText(nomesJogadores.get(equipa1.getJogadorIDs().get(i)));
                jogadoresEquipa2.get(i).setText(nomesJogadores.get(equipa2.getJogadorIDs().get(i)));
            }
        }
    }

    @FXML
    void registar(ActionEvent event) throws Exception {
        if(!validateComboBox(jogoComboBox, "Jogo")) 
            return;
        JogoDTO jogo = jogoComboBox.getValue() == null ? null : jogoComboBox.getValue();

        Long equipa1ID = jogo.getEquipaIDs().get(0);
        EquipaDTO equipa1 = ApiClient.getEquipa(equipa1ID);
        List<Long> jogadoresEquipa1 = equipa1.getJogadorIDs();

        Long equipa2ID = jogo.getEquipaIDs().get(1);
        EquipaDTO equipa2 = ApiClient.getEquipa(equipa2ID);
        List<Long> jogadoresEquipa2 = equipa2.getJogadorIDs();
        
        Map<Long, Integer> resultado = new HashMap<>();

        int pontosEquipa1 = Integer.parseInt(equipa1Pontos.getText().split(" ")[0]);
        resultado.put(equipa1ID, pontosEquipa1);

        int pontosEquipa2 = Integer.parseInt(equipa2Pontos.getText().split(" ")[0]);
        resultado.put(equipa2ID, pontosEquipa2);

        Long equipaVitoriosa = null;
        if(pontosEquipa1 != pontosEquipa2)
            equipaVitoriosa = pontosEquipa1 > pontosEquipa2 ? equipa1ID : equipa2ID;

        Map<Long, Integer> golos = new HashMap<>();
        Map<Long, String> cartoes = new HashMap<>();
        for(int i = 0; i < 5; i++) {
            Long jogador1ID = jogadoresEquipa1.get(i);
            Long jogador2ID = jogadoresEquipa2.get(i);

            golos.put(jogador1ID, spinnersEquipa1.get(i).getValue());
            golos.put(jogador2ID, spinnersEquipa2.get(i).getValue());

            String cartao1 = cartaoComboBoxes.get(i).getValue().toUpperCase();
            String cartao2 = cartaoComboBoxes.get(i+5).getValue().toUpperCase();
            if(!cartao1.equals("NENHUM"))
                cartoes.put(jogador1ID, cartao1);
            if(!cartao2.equals("NENHUM"))
                cartoes.put(jogador2ID, cartao2);
        }

        EstatisticasDTO estatisticas = new EstatisticasDTO(jogo.getId(), resultado, equipaVitoriosa, golos, cartoes);
        try {
            ApiClient.registarResultado(estatisticas);
            showAlert(AlertType.INFORMATION, "Estatísticas registadas", "Estatísticas registadas com sucesso",
                "As estatísticas foram registadas com sucesso");
            back(event);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro no registo do resultado", e.getMessage());
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
