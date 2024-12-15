
package org.albumProjectFinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage)throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/loginForm.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    DatabaseConnection db = new DatabaseConnection();
    public static void main(String[] args) {

        launch(args);
    }
}
