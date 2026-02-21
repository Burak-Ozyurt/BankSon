package com.sau.bankproject.Controller;

import com.sau.bankproject.Database.CustomerCRUD;
import com.sau.bankproject.Database.DepositorCRUD;
import com.sau.bankproject.DTO.Account;
import com.sau.bankproject.DTO.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.Optional;

public class DepositorController {

    @FXML private TextField customerIdField;
    @FXML private TextField accountIdField;
    @FXML private ListView<String> accountsListView;

    @FXML
    void getCustomerAccounts(ActionEvent event) {
        if (!isCustomerIdValid()) return;

        int customerId = Integer.parseInt(customerIdField.getText());

        // Önce müşterinin var olup olmadığını kontrol et
        CustomerCRUD customerCRUD = new CustomerCRUD();
        Optional<Customer> customer = customerCRUD.getCustomerById(customerId);

        if (customer.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Bu ID'ye sahip bir müşteri bulunamadı!");
            accountsListView.getItems().clear();
            return;
        }

        // Müşteri varsa hesaplarını getir
        DepositorCRUD depositorCRUD = new DepositorCRUD();
        List<Account> accounts = depositorCRUD.getAccountsByCustomerId(customerId);

        if (accounts.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Bilgi",
                    "Müşteri bulundu ancak henüz bağlı bir hesap yok.\nHesap eklemek için Account ID girip ADD butonuna tıklayın.");
            accountsListView.getItems().clear();
        } else {
            ObservableList<String> items = FXCollections.observableArrayList();
            for (Account acc : accounts) {
                items.add("Hesap No: " + acc.getId() +
                        " | Şube: " + acc.getBranch() +
                        " | Bakiye: " + acc.getBalance());
            }
            accountsListView.setItems(items);
        }
    }

    @FXML
    void addDepositor(ActionEvent event) {
        if (!isCustomerIdValid()) return;
        if (!isAccountIdValid()) return;

        int customerId = Integer.parseInt(customerIdField.getText());
        int accountId = Integer.parseInt(accountIdField.getText());

        // Müşteri var mı?
        CustomerCRUD customerCRUD = new CustomerCRUD();
        if (customerCRUD.getCustomerById(customerId).isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Bu ID'ye sahip bir müşteri bulunamadı!");
            return;
        }

        DepositorCRUD depositorCRUD = new DepositorCRUD();
        int result = depositorCRUD.addDepositor(customerId, accountId);

        if (result > 0) {
            showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Hesap müşteriye başarıyla bağlandı!");
            getCustomerAccounts(event); // Listeyi güncelle
        } else if (result == -1) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Bu hesap zaten bu müşteriye bağlı!");
        } else if (result == -2) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Girilen Account ID veritabanında bulunamadı!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Hata", "İşlem başarısız oldu.");
        }
    }

    @FXML
    void removeDepositor(ActionEvent event) {
        if (!isCustomerIdValid()) return;
        if (!isAccountIdValid()) return;

        int customerId = Integer.parseInt(customerIdField.getText());
        int accountId = Integer.parseInt(accountIdField.getText());

        DepositorCRUD depositorCRUD = new DepositorCRUD();
        int result = depositorCRUD.removeDepositor(customerId, accountId);

        if (result > 0) {
            showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Hesap bağlantısı silindi!");
            getCustomerAccounts(event); // Listeyi güncelle
        } else {
            showAlert(Alert.AlertType.ERROR, "Hata", "Silinecek bağlantı bulunamadı.");
        }
    }

    @FXML
    void clearDepositor(ActionEvent event) {
        customerIdField.clear();
        accountIdField.clear();
        accountsListView.getItems().clear();
    }

    private boolean isCustomerIdValid() {
        String text = customerIdField.getText();
        if (text == null || text.trim().isEmpty() || !text.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen geçerli bir Customer ID girin.");
            return false;
        }
        return true;
    }

    private boolean isAccountIdValid() {
        String text = accountIdField.getText();
        if (text == null || text.trim().isEmpty() || !text.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen geçerli bir Account ID girin.");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}