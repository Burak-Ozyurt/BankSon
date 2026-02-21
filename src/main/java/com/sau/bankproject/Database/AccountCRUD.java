package com.sau.bankproject.Database;

import com.sau.bankproject.DTO.Account;
import java.sql.*;
import java.util.Optional;

public class AccountCRUD {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/Bank";
    static final String USER = "postgres";
    static final String PASS = "011025";

    // Bir hesabı ID ile getir
    public Optional<Account> getAccountById(int id) {
        Account account = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM account WHERE id = " + id;
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                account = new Account();
                account.setId(resultSet.getInt("id"));
                account.setBranch(resultSet.getString("branch"));
                account.setBalance(resultSet.getDouble("balance"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.ofNullable(account);
    }

    // Yeni hesap ekle
    public int insertAccountById(Account account) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            if(getAccountById(account.getId()).isPresent()) {
                result = -1; // Zaten var
            } else {
                String query = "INSERT INTO account (id, branch, balance) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                    pstmt.setInt(1, account.getId());
                    pstmt.setString(2, account.getBranch());
                    pstmt.setDouble(3, account.getBalance());
                    result = pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // Hesabı sil
    public int deleteAccountById(int id) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "DELETE FROM account WHERE id = " + id;
            Statement statement = connection.createStatement();
            result = statement.executeUpdate(query);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }

    // Hesabı güncelle
    public int updateAccountById(Account account) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            if(getAccountById(account.getId()).isPresent()) {
                String query = "UPDATE account SET branch = ?, balance = ? WHERE id = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                    pstmt.setString(1, account.getBranch());
                    pstmt.setDouble(2, account.getBalance());
                    pstmt.setInt(3, account.getId());
                    result = pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}