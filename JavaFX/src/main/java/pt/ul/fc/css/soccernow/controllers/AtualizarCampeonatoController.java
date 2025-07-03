package pt.ul.fc.css.soccernow.controllers;

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
import javafx.stage.Stage;
import pt.ul.fc.css.soccernow.api.ApiClient;
import pt.ul.fc.css.soccernow.dto.CampeonatoDTO;
import pt.ul.fc.css.soccernow.dto.ClubeDTO;
import pt.ul.fc.css.soccernow.model.DataModel;

public class AtualizarCampeonatoController extends ControllerWithModel {

    private CampeonatoDTO campeonato;

    @FXML
    private TextField nomeField;

    @FXML
    private ToggleGroup formato;
    
    @FXML
    private RadioButton pontosRadio;

    @FXML
    private RadioButton eliminatoriaRadio;

    @FXML
    private ToggleGroup concluido;
    
    @FXML
    private RadioButton simRadio;

    @FXML
    private RadioButton naoRadio;

    @FXML
    private ListView<ClubeDTO> listClubes;
    
    @FXML
    private ListView<ClubeDTO> listClubesSelected;

    // override initModel to avoid NullPointerException
    @Override
    public void initModel(Stage stage, DataModel model) {
        if (super.model != null) 
            throw new IllegalStateException("Model already initialized");
        super.stage = stage;
        super.model = model;

        initUI();
    }

    private void initUI() {
        campeonato = super.model.getSelectedCampeonato();
        nomeField.setText(campeonato.getNome());

        formato = new ToggleGroup();
        pontosRadio.setToggleGroup(formato);
        eliminatoriaRadio.setToggleGroup(formato);

        switch (campeonato.getFormato()) {
            case "PONTOS":
                pontosRadio.setSelected(true);
                break;
            case "ELIMINATORIA":
                eliminatoriaRadio.setSelected(true);
                break;
        }

        concluido = new ToggleGroup();
        simRadio.setToggleGroup(concluido);
        naoRadio.setToggleGroup(concluido);

        if(campeonato.isConcluido())
            simRadio.setSelected(true);
        else
            naoRadio.setSelected(true);

        try {
            List<ClubeDTO> clubes = ApiClient.getClubes();
            ObservableList<ClubeDTO> selectedClubes = FXCollections.observableArrayList(
                clubes.stream().filter(clube -> campeonato.getClubes().contains(clube.getId())).toList());

            listClubes.setItems(FXCollections.observableArrayList(clubes));
            listClubesSelected.setItems(selectedClubes);

            listClubes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            listClubes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if(newSelection != null && !selectedClubes.contains(newSelection))
                    selectedClubes.add(newSelection);
            });

            listClubesSelected.setOnMouseClicked(event -> {
                ClubeDTO selectedItem = listClubesSelected.getSelectionModel().getSelectedItem();
                if (selectedItem != null)
                    selectedClubes.remove(selectedItem);
            });
        } catch(Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro na obtenção de dados", e.getMessage());
        }
    }

    @FXML
    void submeter(ActionEvent event) {
        if(!validateTextField(nomeField, "Nome"))
            return;
        campeonato.setNome(nomeField.getText().trim());
        
        campeonato.setFormato(((RadioButton) this.formato.getSelectedToggle()).equals(pontosRadio) ? "PONTOS" : "ELIMINATORIA");
        campeonato.setConcluido(((RadioButton) this.concluido.getSelectedToggle()).equals(simRadio));
        campeonato.setClubes(listClubesSelected.getItems().stream().map(ClubeDTO::getId).toList());

        try {
            ApiClient.updateCampeonato(campeonato);
            showAlert(AlertType.INFORMATION, "Campeonato atualizado", "Campeonato atualizado com sucesso",
                String.format("O campeonato %s foi atualizado com sucesso", campeonato.getNome()));
            back(event);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro!", "Erro na atualização do campeonato", e.getMessage());
        }
    }
    
    @FXML
    void cancelar(ActionEvent event) {
        back(event);
    }

    @FXML
    void back(ActionEvent event) {
        switchScene("/pt/ul/fc/css/soccernow/view/gerir_campeonatos.fxml", "SoccerNow: Gestão de campeonatos", model);
    }

    @FXML
    void quit(ActionEvent event) {
        System.exit(0);
    }
}
