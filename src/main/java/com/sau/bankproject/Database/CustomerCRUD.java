package com.sau.bankproject.Database;

import com.sau.bankproject.DTO.Customer;
import java.sql.*;
import java.util.Optional;


public class CustomerCRUD {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/Bank";
    static final String USER = "postgres";
    static final String PASS = "011025";

    public Optional<Customer> getCustomerById(int id) {
        Customer customer = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM customer WHERE id = " + id;
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setName(resultSet.getString("name"));
                customer.setAddress(resultSet.getString("address"));
                customer.setCity(resultSet.getString("city"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.ofNullable(customer);
    }

    public int insertCustomerById(Customer customer) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();

            // DÜZELTME: city için tırnak ve virgül eklemeleri yapıldı
            String params = customer.getId() + ", " +
                    "'" + customer.getName() + "', " +
                    "'" + customer.getAddress() + "', " +
                    "'" + customer.getCity() + "'";

            if(getCustomerById(customer.getId()).isPresent()) {
                result = -1;
            } else {
                String query = "INSERT INTO customer (id, name, address, city) VALUES (" + params + ");";
                result = statement.executeUpdate(query);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public int deleteCustomerById(int id) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();
            String query = "DELETE FROM customer WHERE id = " + id;
            result = statement.executeUpdate(query);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }

    public int updateCustomerById(Customer customer) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();

            if(getCustomerById(customer.getId()).isPresent()) {
                // DÜZELTME: city kolonu UPDATE sorgusuna eklendi
                String query = "UPDATE customer SET " +
                        "name = '" + customer.getName() + "', " +
                        "address = '" + customer.getAddress() + "', " +
                        "city = '" + customer.getCity() + "' " +
                        "WHERE id = " + customer.getId() + ";";

                result = statement.executeUpdate(query);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}