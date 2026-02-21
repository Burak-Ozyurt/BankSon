package com.sau.bankproject.Database;

import com.sau.bankproject.DTO.Account;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepositorCRUD {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/Bank";
    static final String USER = "postgres";
    static final String PASS = "011025"; // Diğer CRUD dosyalarınla uyumlu

    // Belirli bir Müşteriye ait tüm hesapları getirir
    public List<Account> getAccountsByCustomerId(int customerId) {
        List<Account> accounts = new ArrayList<>();
        // account ve depositor tablolarını accountid üzerinden join yapıyoruz
        String query = "SELECT a.* FROM \"account\" a " +
                "JOIN \"depositor\" d ON a.accountid = d.accountid " +
                "WHERE d.customerid = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Account acc = new Account();
                acc.setId(rs.getInt("accountid"));
                acc.setBranch(rs.getString("branch"));
                acc.setBalance(rs.getDouble("balance"));
                accounts.add(acc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }
}