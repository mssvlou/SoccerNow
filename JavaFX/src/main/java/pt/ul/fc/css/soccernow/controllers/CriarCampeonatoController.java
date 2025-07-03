package pt.ul.fc.css.soccernow.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.CampeonatoDTO;
import pt.ul.fc.css.soccernow.dto.ClubeDTO;

public class CriarCampeonatoController extends ControllerWithModel {

    @FXML
    private TextField nomeField;

    @FXML
    private ToggleGroup formato;
    
    @FXML
    private RadioButton pontosRadio;

    @FXML
    private RadioButton eliminatoriaRadio;

    @FXML
    private ListView<ClubeDTO> listClubes;
    
    @FXML
    private ListView<ClubeDTO> listSelected;

    @FXML
    public void initialize() throws Exception {
        formato = new ToggleGroup();
        pontosRadio.setToggleGroup(formato);
        eliminatoriaRadio.setToggleGroup(formato);

        List<ClubeDTO> clubes = ApiClient.getClubes();
        ObservableList<ClubeDTO> selected = FXCollections.observableArrayList();

        listClubes.setItems(FXCollections.observableArrayList(clubes));
        listSelected.setItems(selected);

        listClubes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listClubes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null && !selected.contains(newSelection))
                selected.add(newSelection);
        });

        listSelected.setOnMouseClicked(event -> {
            ClubeDTO selectedItem = listSelected.getSelectionModel().getSelectedItem();
            if (selectedItem != null)
                selected.remove(selectedItem);
        });
    }

    @FXML
    void criar(ActionEvent event) {
        if(!validateTextField(nomeField, "Nome"))
            return;
        String nome = nomeField.getText().trim();
        
        String formato = ((RadioButton) this.formato.getSelectedToggle()).equals(pontosRadio) ? "PONTOS" : "ELIMINATORIA";
        List<Long> clubeIDs = listSelected.getItems().stream().map(ClubeDTO::getId).toList();
        
        CampeonatoDTO campeonatoDTO = new CampeonatoDTO(0L, nome, formato, false, clubeIDs, new ArrayList<>(), new HashMap<>(), new HashMap<>());
        try {
            ApiClient.criarCampeonato(campeonatoDTO);
            showAlert(AlertType.INFORMATION, "Campeonato criado", "Campeonato criado com sucesso",
                String.format("O campeonato com o nome %s foi criado com sucesso", nome));
            back(event);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro na criação de um novo campeonato", e.getMessage());
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
