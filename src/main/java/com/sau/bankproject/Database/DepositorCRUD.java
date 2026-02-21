package com.sau.bankproject.Database;

import com.sau.bankproject.DTO.Account;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepositorCRUD {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/Bank";
    static final String USER = "postgres";
    static final String PASS = "011025";

    // Müşteriye ait tüm hesapları getir
    public List<Account> getAccountsByCustomerId(int customerId) {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT a.* FROM \"account\" a " +
                "JOIN \"depositors\" d ON a.accountid = d.account_id " +
                "WHERE d.customer_id = ?";

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

    // Müşteriye hesap bağla
    // -1: zaten var, -2: accountid yok, 1: başarılı
    public int addDepositor(int customerId, int accountId) {
        // Zaten var mı kontrol et
        String checkQuery = "SELECT 1 FROM \"depositors\" WHERE customer_id = ? AND account_id = ?";
        String checkAccountQuery = "SELECT 1 FROM \"account\" WHERE accountid = ?";
        String insertQuery = "INSERT INTO \"depositors\" (customer_id, account_id) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

            // Account var mı?
            try (PreparedStatement pstmt = conn.prepareStatement(checkAccountQuery)) {
                pstmt.setInt(1, accountId);
                ResultSet rs = pstmt.executeQuery();
                if (!rs.next()) return -2; // Account bulunamadı
            }

            // Bağlantı zaten var mı?
            try (PreparedStatement pstmt = conn.prepareStatement(checkQuery)) {
                pstmt.setInt(1, customerId);
                pstmt.setInt(2, accountId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) return -1; // Zaten bağlı
            }

            // Ekle
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                pstmt.setInt(1, customerId);
                pstmt.setInt(2, accountId);
                return pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Müşteriden hesap bağlantısını kaldır
    public int removeDepositor(int customerId, int accountId) {
        String query = "DELETE FROM \"depositors\" WHERE customer_id = ? AND account_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, customerId);
            pstmt.setInt(2, accountId);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}