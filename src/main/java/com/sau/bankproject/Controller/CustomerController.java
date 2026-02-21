package com.sau.bankproject.Controller;

import com.sau.bankproject.Database.CustomerCRUD;
import com.sau.bankproject.DTO.Customer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Optional;

public class CustomerController {

    @FXML
    private TextField name;

    @FXML
    private TextField customerId;

    @FXML
    private TextField address;

    // 1. Şehir için FXML bağlantısını ekle
    @FXML
    private TextField city;

    @FXML
    private Button clearCustomer;

    @FXML
    private Button close;

    @FXML
    private Button deleteCustomer;

    @FXML
    private Button getCustomer;

    @FXML
    private Button saveCustomer;

    @FXML
    void clearCustomer(ActionEvent event) {
        customerId.setText("");
        name.setText("");
        address.setText("");
        city.setText(""); // Temizleme işlemine ekle
    }

    @FXML
    public void close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void deleteCustomer(ActionEvent event) {
        checkId(customerId.getText(), event);
        CustomerCRUD crudOperations = new CustomerCRUD();
        int id = Integer.parseInt(customerId.getText());
        int result = crudOperations.deleteCustomerById(id);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Customer with id " + customerId.getText() + " deleted");
        alert.showAndWait();
        clearCustomer(event);
    }

    @FXML
    void getCustomer(ActionEvent event) {
        checkId(customerId.getText(), event);
        CustomerCRUD crudOperations = new CustomerCRUD();
        int id = Integer.parseInt(customerId.getText());
        Optional<Customer> customer = crudOperations.getCustomerById(id);
        if(customer.isPresent()){
            customerId.setText(Integer.toString(customer.get().getId()));
            name.setText(customer.get().getName());
            address.setText(customer.get().getAddress());
            city.setText(customer.get().getCity()); // Şehri ekrana yazdır
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Customer with id " + id + " not found");
            alert.showAndWait();
        }
    }

    @FXML
    void saveCustomer(ActionEvent event) {
        checkId(customerId.getText(), event);
        Customer customer = new Customer();
        customer.setId(Integer.parseInt(customerId.getText()));
        customer.setName(name.getText());
        customer.setAddress(address.getText());
        customer.setCity(city.getText()); // Şehri nesneye set et

        CustomerCRUD crudOperations = new CustomerCRUD();
        int res = crudOperations.insertCustomerById(customer);
        if(res > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Customer with id " + customerId.getText() + " saved");
            alert.showAndWait();
        } else if(res == -1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There another customer with id: " + customerId.getText());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on save customer!");
            alert.showAndWait();
        }
    }

    @FXML
    void updateCustomer(ActionEvent event) {
        checkId(customerId.getText(), event);
        Customer customer = new Customer();
        customer.setId(Integer.parseInt(customerId.getText()));
        customer.setName(name.getText());
        customer.setAddress(address.getText());
        customer.setCity(city.getText()); // Şehri nesneye set et

        CustomerCRUD crudOperations = new CustomerCRUD();
        int res = crudOperations.updateCustomerById(customer);
        if(res > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Customer with id " + customerId.getText() + " id updated");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on update customer!");
            alert.showAndWait();
        }
    }

    public void checkId(String id, ActionEvent event) {
        if (id.isEmpty() || Integer.parseInt(id) <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Id is wrong!");
            alert.showAndWait();
            clearCustomer(event);
        }
    }
}