package org.albumProjectFinal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.albumProjectFinal.DatabaseConnection;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class loginController implements Initializable {
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;

    private DatabaseConnection connection;

    public loginController(){
        connection = new DatabaseConnection();
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void buttonbtn(ActionEvent event) throws IOException {
        String sql = "INSERT INTO login (username,password) VALUES (?,?)";
        try (PreparedStatement stmt = connection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, usernameTextField.getText());
            stmt.setString(2, passwordTextField.getText());
            stmt.execute();

            switchScene(event, "/fxml/AlbumForm.fxml");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
