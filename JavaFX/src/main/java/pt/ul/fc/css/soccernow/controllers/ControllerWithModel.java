package pt.ul.fc.css.soccernow.controllers;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.ul.fc.css.soccernow.model.DataModel;

public class ControllerWithModel {
    protected DataModel model;
    protected Stage stage;

    public void initModel(Stage stage, DataModel model) {
        if (this.model != null) 
            throw new IllegalStateException("Modelo já foi inicializado");
        this.stage = stage;
        this.model = model;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void switchScene(String fxmlPath, String title, DataModel model) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene newScene = new Scene(loader.load());

            ControllerWithModel controller = loader.getController();
            controller.initModel(stage,model);

            stage.setTitle(title);
            stage.setScene(newScene);
            stage.show();
        } catch (IOException | ClassCastException e) {
            e.printStackTrace();
        }
    }

    public void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(header);

        Label contentLabel = new Label(content);
        contentLabel.setWrapText(true);
        alert.getDialogPane().setContent(contentLabel);

        alert.showAndWait();
    }

    protected boolean validateTextField(TextField field, String fieldName) {
        String text = field.getText().trim();
        if(text.isBlank()) {
            showAlert(AlertType.ERROR, "Erro!", "Campo obrigatório vazio", 
                String.format("%s não pode ser vazio", fieldName));
            return false;
        }
        return true;
    }

    protected boolean validateComboBox(ComboBox<?> comboBox, String comboBoxName) {
        Object item = comboBox.getValue();
        if(item == null) {
            showAlert(AlertType.ERROR, "Erro!", "Campo obrigatório vazio", 
                String.format("Por favor selecione uma opção para %s", comboBoxName));
            return false;
        }
        return true;
    }
}
