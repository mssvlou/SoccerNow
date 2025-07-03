package pt.ul.fc.css.soccernow.controllers;

import java.time.LocalTime;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;
import pt.ul.fc.css.soccernow.dto.CampeonatoDTO;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.dto.JogoDTO;
import pt.ul.fc.css.soccernow.dto.LocalDTO;

public class CriarJogoController extends ControllerWithModel {

    @FXML
    private DatePicker dataPicker;

    @FXML
    private ComboBox<String> horasComboBox;

    @FXML
    private ComboBox<String> minutosComboBox;

    @FXML
    private ComboBox<LocalDTO> localComboBox;

    @FXML
    private Button criarLocal;

    @FXML
    private ComboBox<CampeonatoDTO> campeonatoComboBox;

    @FXML
    private ComboBox<EquipaDTO> equipa1ComboBox;

    @FXML
    private ComboBox<EquipaDTO> equipa2ComboBox;

    @FXML
    private ListView<ArbitroDTO> listArbitros;

    @FXML
    private ListView<ArbitroDTO> listSelected;

    @FXML
    private ComboBox<ArbitroDTO> arbitroPrincipalComboBox;

    @FXML
    public void initialize() throws Exception {
        for (int i = 6; i < 24; i++)
            horasComboBox.getItems().add(String.format("%02d", i));
        for (int i = 0; i < 60; i++)
            minutosComboBox.getItems().add(String.format("%02d", i));
        
        horasComboBox.getSelectionModel().select("06");
        minutosComboBox.getSelectionModel().select("00");

        localComboBox.setItems(FXCollections.observableArrayList(ApiClient.getLocais()));
        campeonatoComboBox.setItems(FXCollections.observableArrayList(ApiClient.getCampeonatos()));

        List<EquipaDTO> equipas = ApiClient.getEquipas();
        equipa1ComboBox.setItems(FXCollections.observableArrayList(equipas));
        equipa2ComboBox.setItems(FXCollections.observableArrayList(equipas));

        List<ArbitroDTO> arbitros = ApiClient.getUtilizadores().stream()
            .filter(utilizador -> utilizador instanceof ArbitroDTO)
            .map(utilizador -> (ArbitroDTO) utilizador).toList();
        ObservableList<ArbitroDTO> selected = FXCollections.observableArrayList();

        listArbitros.setItems(FXCollections.observableArrayList(arbitros));
        listSelected.setItems(selected);
        arbitroPrincipalComboBox.setItems(selected);

        listArbitros.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listArbitros.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null && !selected.contains(newSelection))
                selected.add(newSelection);
        });

        listSelected.setOnMouseClicked(event -> {
            ArbitroDTO selectedItem = listSelected.getSelectionModel().getSelectedItem();
            if (selectedItem != null)
                selected.remove(selectedItem);
        });
    }
    
    @FXML
    void criarJogo(ActionEvent event) {
        if (dataPicker.getValue() == null) {
            showAlert(AlertType.ERROR, "Erro!", "Campo obrigatório vazio", 
                "Por favor selecione uma data");
            return;
        }
        String data = dataPicker.getValue().toString();

        String horario = LocalTime.parse(horasComboBox.getValue() + ':' + minutosComboBox.getValue()).toString();

        if(!validateComboBox(localComboBox, "Local"))
            return;
        Long localID = localComboBox.getValue().getId();
        
        Long campeonatoID = campeonatoComboBox.getValue() == null ? null : campeonatoComboBox.getValue().getId();

        if(!validateComboBox(equipa1ComboBox, "Equipa 1") || !validateComboBox(equipa2ComboBox, "Equipa 2"))
            return;
        List<Long> equipaIDs = List.of(equipa1ComboBox.getValue().getId(), equipa2ComboBox.getValue().getId());

        List<Long> arbitroIDs = listSelected.getItems().stream().map(ArbitroDTO::getId).toList();
        Long arbitroPrincipalID = arbitroPrincipalComboBox.getValue() == null ? null : arbitroPrincipalComboBox.getValue().getId();
        
        JogoDTO jogoDTO = new JogoDTO(0L, data, horario, localID, "AGENDADO", campeonatoID, arbitroIDs, arbitroPrincipalID, equipaIDs, 0L);
        try {
            ApiClient.criarJogo(jogoDTO);
            showAlert(AlertType.INFORMATION, "Jogo criado", "Jogo criado com sucesso", "O jogo foi criado com sucesso");
            back(event);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro na criação de um novo jogo", e.getMessage());
        }
    }

    @FXML
    void criarLocal(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/criar_local.fxml", "SoccerNow: Criação de locais", model);
    }

    @FXML
    void updateEquipas(ActionEvent event) throws Exception {
        if(campeonatoComboBox.getValue() != null) {
            CampeonatoDTO campeonato = campeonatoComboBox.getValue();
            List<EquipaDTO> equipas = ApiClient.getEquipas().stream()
                .filter(equipa -> campeonato.getClubes().contains(equipa.getClubeID())).toList();
            equipa1ComboBox.setItems(FXCollections.observableArrayList(equipas));
            equipa2ComboBox.setItems(FXCollections.observableArrayList(equipas));
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
