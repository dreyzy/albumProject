package org.albumProjectFinal.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.albumProjectFinal.Data;
import org.albumProjectFinal.DatabaseConnection;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AlbumController implements Initializable {

    @FXML
    private TextField prodId, albumId, artistId, priceId, stocksId, countryId;
    @FXML
    private ComboBox<String> typeId, statusId;
    @FXML
    private TableView<Data> album_table;
    @FXML
    private TableColumn<Data, String> prodIdcol, albumcol, artistcol, typecol, statuscol, datecol;
    @FXML
    private TableColumn<Data, Double> pricecol;
    @FXML
    private TableColumn<Data, Integer> stockscol;
    @FXML
    private Spinner<Integer> quantityinv;
    @FXML
    private Label albuminv, artistinv, typeinv, priceinv, stocksinv, statusinv;

    private ObservableList<Data> albumList;
    private Connection connection;
    private PreparedStatement prepare;
    private ResultSet result;
    private Alert alert;
    DatabaseConnection databaseConnection;

    private final String[] types = {"CD", "Vinyl"};
    private final String[] statuses = {"Available", "Unavailable"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseConnection = new DatabaseConnection();
        connection = databaseConnection.getConnection();

        if (connection == null) {
            showAlert("Database connection failed!");
            return;
        } else {
            System.out.println("Connection established in AlbumController");
        }

        typeId.setItems(FXCollections.observableArrayList(types));
        statusId.setItems(FXCollections.observableArrayList(statuses));
        quantityinv.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        showAlbumData();
    }



    private void showAlbumData() {
        albumList = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM album";
            prepare = connection.prepareStatement(query);
            result = prepare.executeQuery();

            while (result.next()) {
                albumList.add(new Data(
                        result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("album"),
                        result.getString("artist"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("country")
                ));
            }

            album_table.setItems(albumList);
            prodIdcol.setCellValueFactory(new PropertyValueFactory<>("albumId"));
            albumcol.setCellValueFactory(new PropertyValueFactory<>("albumName"));
            artistcol.setCellValueFactory(new PropertyValueFactory<>("artistName"));  // Bind to the artistName column
            typecol.setCellValueFactory(new PropertyValueFactory<>("albumType"));
            pricecol.setCellValueFactory(new PropertyValueFactory<>("albumPrice"));
            stockscol.setCellValueFactory(new PropertyValueFactory<>("albumStock"));
            statuscol.setCellValueFactory(new PropertyValueFactory<>("albumStatus"));
            datecol.setCellValueFactory(new PropertyValueFactory<>("country"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void saveAction() {
        try {
            String query = "INSERT INTO album (prod_id, album, artist, type, price, stock, status, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            prepare = connection.prepareStatement(query);
            prepare.setString(1, prodId.getText());
            prepare.setString(2, albumId.getText());
            prepare.setString(3, artistId.getText());
            prepare.setString(4, typeId.getValue());
            prepare.setDouble(5, Double.parseDouble(priceId.getText()));
            prepare.setInt(6, Integer.parseInt(stocksId.getText()));
            prepare.setString(7, statusId.getValue());
            prepare.setString(8, countryId.getText());
            prepare.executeUpdate();

            showAlert("Successfully added!");
            clearFields();
            showAlbumData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Edit Album
    @FXML
    private void editAction() {
        Data selectedData = album_table.getSelectionModel().getSelectedItem();
        if (selectedData == null) {
            showAlert("Please select a record to edit.");
            return;
        }

        String priceText = priceId.getText();
        double price = 0.0;

        if (!priceText.isEmpty()) {
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                showAlert("Invalid price format.");
                return;
            }
        } else {
            showAlert("Price cannot be empty.");
            return;
        }

        try {
            String query = "UPDATE album SET album = ?, artist = ?, type = ?, price = ?, stock = ?, status = ?, country = ? WHERE prod_id = ?";
            prepare = connection.prepareStatement(query);
            prepare.setString(1, albumId.getText());
            prepare.setString(2, artistId.getText());
            prepare.setString(3, typeId.getValue());
            prepare.setDouble(4, price);  // Use validated price
            prepare.setInt(5, Integer.parseInt(stocksId.getText()));
            prepare.setString(6, statusId.getValue());
            prepare.setString(7, countryId.getText());
            prepare.setString(8, selectedData.getAlbumId());
            prepare.executeUpdate();

            showAlert("Successfully updated!");
            clearFields();
            showAlbumData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void deleteAction() {
        Data data = album_table.getSelectionModel().getSelectedItem();
        if (data == null) {
            showAlert("Please select a record to delete.");
            return;
        }

        try {
            String query = "DELETE FROM album WHERE id = ?";
            prepare = connection.prepareStatement(query);
            prepare.setInt(1, data.getId());
            prepare.executeUpdate();

            showAlert("Record deleted successfully!");
            showAlbumData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void formClicked() {
        Data selectedData = album_table.getSelectionModel().getSelectedItem();
        int selectedIndex = album_table.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0 || selectedData == null) {
            return;
        }

        prodId.setText(selectedData.getAlbumId());
        albumId.setText(selectedData.getAlbumName());
        artistId.setText(selectedData.getArtistName());
        typeId.setValue(selectedData.getAlbumType());
        statusId.setValue(selectedData.getAlbumStatus());
        priceId.setText(String.valueOf(selectedData.getAlbumPrice()));
        stocksId.setText(String.valueOf(selectedData.getAlbumStock()));
        countryId.setText(selectedData.getCountry());

        albuminv.setText(selectedData.getAlbumName());
        artistinv.setText(selectedData.getArtistName());
        typeinv.setText(selectedData.getAlbumType());
        priceinv.setText(String.format("%.2f", selectedData.getAlbumPrice()));
        stocksinv.setText(String.valueOf(selectedData.getAlbumStock()));
        statusinv.setText(selectedData.getAlbumStatus());
    }

    @FXML
    private void clearAction() {
        clearFields();
    }

    @FXML
    private void buyAction() {
        Data selectedAlbum = album_table.getSelectionModel().getSelectedItem();
        if (selectedAlbum == null) {
            showAlert("Please select an album to purchase.");
            return;
        }

        int quantity = quantityinv.getValue();
        double totalPrice = selectedAlbum.getAlbumPrice() * quantity;

        try {
            String query = "INSERT INTO buy (product_id, album_id, artist_id, quantity, total_price) VALUES (?, ?, ?, ?, ?)";
            prepare = connection.prepareStatement(query);
            prepare.setString(1, selectedAlbum.getAlbumId());
            prepare.setString(2, selectedAlbum.getAlbumName());
            prepare.setString(3, selectedAlbum.getAlbumStatus());
            prepare.setInt(4, quantity);
            prepare.setDouble(5, totalPrice);
            prepare.executeUpdate();

            showAlert("Purchase successful! Total Price: " + totalPrice);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        prodId.clear();
        albumId.clear();
        artistId.clear();
        priceId.clear();
        stocksId.clear();
        countryId.clear();
        typeId.setValue(null);
        statusId.setValue(null);
    }
}
