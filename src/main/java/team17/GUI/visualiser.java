package team17.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class visualiser extends Application {

    public visualiser() {
        super();
    }



    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader =new FXMLLoader();
            loader.setLocation(getClass().getResource("view.fxml"));
            Parent root=loader.load();
            primaryStage.setScene(new Scene(root, 1000, 800));
            primaryStage.show();
        }catch (Exception e){

        }


    }
}
