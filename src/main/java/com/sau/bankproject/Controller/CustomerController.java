package com.sau.bankproject.Controller;

import com.sau.bankproject.Database.CustomerCRUD;
import com.sau.bankproject.DTO.Customer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.util.Optional;

public class CustomerController {

    @FXML private TextField name;
    @FXML private TextField customerId;
    @FXML private TextField address;
    @FXML private TextField city;

    @FXML
    void clearCustomer(ActionEvent event) {
        customerId.clear();
        name.clear();
        address.clear();
        city.clear();
    }

    @FXML
    public void close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void getCustomer(ActionEvent event) {
        if (!isIdValid()) return;

        int id = Integer.parseInt(customerId.getText());
        CustomerCRUD crud = new CustomerCRUD();
        Optional<Customer> customer = crud.getCustomerById(id);

        if(customer.isPresent()){
            name.setText(customer.get().getName());
            address.setText(customer.get().getAddress());
            city.setText(customer.get().getCity());
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Customer with id " + id + " not found!");
        }
    }

    @FXML
    void saveCustomer(ActionEvent event) {
        if (!isIdValid()) return;

        Customer c = new Customer(
                Integer.parseInt(customerId.getText()),
                name.getText(),
                address.getText(),
                city.getText()
        );

        int res = new CustomerCRUD().insertCustomerById(c);

        if(res > 0) showAlert(Alert.AlertType.INFORMATION, "Success", "Customer saved successfully.");
        else if(res == -1) showAlert(Alert.AlertType.ERROR, "Error", "This ID already exists!");
        else showAlert(Alert.AlertType.ERROR, "Error", "Save failed!");
    }

    @FXML
    void updateCustomer(ActionEvent event) {
        if (!isIdValid()) return;

        Customer c = new Customer(
                Integer.parseInt(customerId.getText()),
                name.getText(),
                address.getText(),
                city.getText()
        );

        int res = new CustomerCRUD().updateCustomerById(c);

        if(res > 0) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer updated successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Update failed!");
        }
    }

    @FXML
    void deleteCustomer(ActionEvent event) {
        if (!isIdValid()) return;

        int id = Integer.parseInt(customerId.getText());
        int res = new CustomerCRUD().deleteCustomerById(id);

        if (res > 0) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer deleted.");
            clearCustomer(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Delete failed!");
        }
    }

    // ID kutusunun boş veya geçersiz olmasını engelleyen kontrol
    private boolean isIdValid() {
        String idStr = customerId.getText();
        if (idStr == null || idStr.trim().isEmpty() || !idStr.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid numeric ID.");
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