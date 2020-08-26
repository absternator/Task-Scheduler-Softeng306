package team17.GUI;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class visualiser extends Application {

    public visualiser() {
        super();
    }

    public void startVisualisation(String[] args) {
        launch(args);
    }

    @FXML
    AnchorPane ganttChartContainer;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader =new FXMLLoader();
            loader.setLocation(getClass().getResource("view.fxml"));
            Parent root=loader.load();
            primaryStage.setScene(new Scene(root, 1000, 750));





            primaryStage.show();
        }catch (Exception e){

        }


    }
}
