package com.sau.bankproject.Controller;

import com.sau.bankproject.Database.AccountCRUD;
import com.sau.bankproject.DTO.Account;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.Optional;

public class AccountController {

    @FXML
    private TextField accountid;

    @FXML
    private TextField branch;

    @FXML
    private TextField balance;

    @FXML
    void clearAccount(ActionEvent event) {
        accountid.clear();
        branch.clear();
        balance.clear();
    }

    @FXML
    public void close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void deleteAccount(ActionEvent event) {
        if (!isIdValid()) return; // ID geçersizse işlemi durdurur

        int id = Integer.parseInt(accountid.getText());
        AccountCRUD crudOperations = new AccountCRUD();
        int result = crudOperations.deleteAccountById(id);

        if (result > 0) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account with id " + id + " deleted.");
            clearAccount(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Account could not be deleted.");
        }
    }

    @FXML
    void getAccount(ActionEvent event) {
        if (!isIdValid()) return;

        int id = Integer.parseInt(accountid.getText());
        AccountCRUD crudOperations = new AccountCRUD();
        Optional<Account> account = crudOperations.getAccountById(id);

        if (account.isPresent()) {
            accountid.setText(Integer.toString(account.get().getId()));
            branch.setText(account.get().getBranch()); // DTO'dan branch bilgisini çeker
            balance.setText(Double.toString(account.get().getBalance()));
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Account with id " + id + " not found.");
        }
    }

    @FXML
    void saveAccount(ActionEvent event) {
        if (!isIdValid()) return;

        try {
            Account account = new Account();
            account.setId(Integer.parseInt(accountid.getText()));
            account.setBranch(branch.getText()); // Şube bilgisini UI'dan alır
            account.setBalance(Double.parseDouble(balance.getText()));

            AccountCRUD crudOperations = new AccountCRUD();
            int res = crudOperations.insertAccountById(account);

            if (res > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account saved.");
            } else if (res == -1) {
                showAlert(Alert.AlertType.ERROR, "Error", "An account with this ID already exists.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Save failed.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid balance.");
        }
    }

    @FXML
    void updateAccount(ActionEvent event) {
        if (!isIdValid()) return;

        try {
            Account account = new Account();
            account.setId(Integer.parseInt(accountid.getText()));
            account.setBranch(branch.getText());
            account.setBalance(Double.parseDouble(balance.getText()));

            AccountCRUD crudOperations = new AccountCRUD();
            int res = crudOperations.updateAccountById(account);

            if (res > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account updated.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Update failed.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid balance.");
        }
    }

    // ID kontrolü yapan ve hata varsa işlemi durduran yardımcı metod
    private boolean isIdValid() {
        String idText = accountid.getText();
        if (idText == null || idText.trim().isEmpty() || !idText.matches("\\d+") || Integer.parseInt(idText) <= 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid positive ID.");
            return false;
        }
        return true;
    }

    // Alert göstermek için yardımcı metod
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}