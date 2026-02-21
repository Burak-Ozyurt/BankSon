package com.sau.bankproject.Controller;

import com.sau.bankproject.Database.AccountCRUD;
import com.sau.bankproject.DTO.Account;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Optional;

public class AccountController {

    @FXML
    private TextField accountId; // carId -> accountId

    @FXML
    private TextField balance; // brand -> balance (Örn: Bakiye)

    @FXML
    private TextField type; // model -> type (Örn: Vadeli/Vadesiz)

    @FXML
    private TextField customerId; // plate -> customerId (Hesabın sahibi)

    @FXML
    private Button clearAccount;

    @FXML
    private Button close;

    @FXML
    private Button deleteAccount;

    @FXML
    private Button getAccount;

    @FXML
    private Button saveAccount;

    @FXML
    void clearAccount(ActionEvent event) {
        accountId.setText("");
        balance.setText("");
        type.setText("");
        customerId.setText("");
    }

    @FXML
    public void close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void deleteAccount(ActionEvent event) {
        checkId(accountId.getText(), event);
        AccountCRUD crudOperations = new AccountCRUD();
        int id = Integer.parseInt(accountId.getText());
        int result = crudOperations.deleteAccountById(id);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Account with id " + id + " deleted");
        alert.showAndWait();
        clearAccount(event);
    }

    @FXML
    void getAccount(ActionEvent event) {
        checkId(accountId.getText(), event);
        AccountCRUD crudOperations = new AccountCRUD();
        int id = Integer.parseInt(accountId.getText());
        Optional<Account> account = crudOperations.getAccountById(id);

        if(account.isPresent()){
            accountId.setText(Integer.toString(account.get().getId()));
            balance.setText(String.valueOf(account.get().getBalance())); // DTO'na göre güncelle

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Account with id " + id + " not found");
            alert.showAndWait();
        }
    }

    @FXML
    void saveAccount(ActionEvent event) {
        checkId(accountId.getText(), event);
        Account account = new Account();
        account.setId(Integer.parseInt(accountId.getText()));
        account.setBalance(Double.parseDouble(balance.getText()));

        AccountCRUD crudOperations = new AccountCRUD();
        int res = crudOperations.insertAccountById(account);

        if(res > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Account with id " + accountId.getText() + " saved");
            alert.showAndWait();
        } else if(res == -1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There another account with id: " + accountId.getText());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on save account!");
            alert.showAndWait();
        }
    }

    @FXML
    void updateAccount(ActionEvent event) {
        checkId(accountId.getText(), event);
        Account account = new Account();
        account.setId(Integer.parseInt(accountId.getText()));
        account.setBalance(Double.parseDouble(balance.getText()));

        AccountCRUD crudOperations = new AccountCRUD();
        int res = crudOperations.updateAccountById(account);

        if(res > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Account with id " + accountId.getText() + " updated");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on update account!");
            alert.showAndWait();
        }
    }

    public void checkId(String id, ActionEvent event) {
        if (id.isEmpty() || Integer.parseInt(id) <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Id is wrong!");
            alert.showAndWait();
            clearAccount(event);
        }
    }
}