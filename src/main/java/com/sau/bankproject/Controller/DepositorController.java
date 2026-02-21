package com.sau.bankproject.Controller;

import com.sau.bankproject.DTO.Account;
import com.sau.bankproject.Database.DepositorCRUD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.util.List;

public class DepositorController {

    @FXML
    private TextField customerIdField;

    @FXML
    private ListView<String> accountsListView;

    @FXML
    void getCustomerAccounts(ActionEvent event) {
        String idText = customerIdField.getText();

        // ID Geçerlilik Kontrolü
        if (idText == null || idText.trim().isEmpty() || !idText.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen geçerli bir Customer ID girin.");
            return;
        }

        int id = Integer.parseInt(idText);
        DepositorCRUD crud = new DepositorCRUD();
        List<Account> accounts = crud.getAccountsByCustomerId(id);

        if (accounts.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Bilgi", "Bu müşteriye ait herhangi bir hesap bulunamadı.");
            accountsListView.getItems().clear();
        } else {
            ObservableList<String> items = FXCollections.observableArrayList();
            for (Account acc : accounts) {
                items.add("Hesap No: " + acc.getId() + " | Şube: " + acc.getBranch() + " | Bakiye: " + acc.getBalance());
            }
            accountsListView.setItems(items);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}