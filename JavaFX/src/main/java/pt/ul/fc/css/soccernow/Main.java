package pt.ul.fc.css.soccernow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.ul.fc.css.soccernow.controllers.InitController;
import pt.ul.fc.css.soccernow.model.DataModel;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        FXMLLoader initLoader = new FXMLLoader(getClass().getResource("/pt/ul/fc/css/soccernow/view/init.fxml"));
        root.setCenter(initLoader.load());
        InitController initController = initLoader.getController();

        DataModel model = new DataModel();
        initController.initModel(primaryStage,model);

        primaryStage.setScene(new Scene(root,494,307));
        primaryStage.setTitle("SoccerNow");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
