package com.codewarrior.markets_coupons.service;

import com.codewarrior.markets_coupons.model.Voucher;
import com.codewarrior.markets_coupons.util.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherDAO {

    private final Connection connection;

    public VoucherDAO() {
        System.out.println("MDAO INITITALIZED !");
        this.connection = DataBase.getConnection();
    }

    // Method to insert a new voucher into the database
    public void addVoucher(Voucher voucher) {
        String query = "INSERT INTO voucher (category_id, user_won_id, market_related_id, code, expiration, value, usage_limit, type, is_valid, is_given_to_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, voucher.getCategoryId());
            statement.setInt(2, voucher.getUserWonId());
            statement.setInt(3, voucher.getMarketRelatedId());
            statement.setString(4, voucher.getCode());
            statement.setDate(5, new java.sql.Date(voucher.getExpiration().getTime()));
            statement.setDouble(6, voucher.getValue());
            statement.setInt(7, voucher.getUsageLimit());
            statement.setString(8, voucher.getType());
            statement.setBoolean(9, voucher.isValid());
            statement.setBoolean(10, voucher.isGivenToUser());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                voucher.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all vouchers from the database
    public List<Voucher> getAllVouchers() {
        List<Voucher> vouchers = new ArrayList<>();
        String query = "SELECT * FROM voucher";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Voucher voucher = extractVoucherFromResultSet(resultSet);
                vouchers.add(voucher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vouchers;
    }

    public ObservableList<Voucher> getAllVouchersOb() {
        ObservableList<Voucher> vouchers = FXCollections.observableArrayList();
        String query = "SELECT * FROM voucher";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Voucher voucher = new Voucher();
                voucher.setId(resultSet.getInt("id"));
                voucher.setCategoryId(resultSet.getInt("category_id"));
                voucher.setUserWonId(resultSet.getInt("user_won_id"));
                voucher.setMarketRelatedId(resultSet.getInt("market_related_id"));
                voucher.setCode(resultSet.getString("code"));
                voucher.setExpiration(resultSet.getDate("expiration"));
                voucher.setValue(resultSet.getDouble("value"));
                voucher.setUsageLimit(resultSet.getInt("usage_limit"));
                voucher.setType(resultSet.getString("type"));
                voucher.setValid(resultSet.getBoolean("is_valid"));
                voucher.setGivenToUser(resultSet.getBoolean("is_given_to_user"));
                vouchers.add(voucher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vouchers;
    }

    // Method to update an existing voucher
    public void updateVoucher(Voucher voucher) {
        String query = "UPDATE voucher SET category_id = ?, user_won_id = ?, market_related_id = ?, code = ?, expiration = ?, value = ?, usage_limit = ?, type = ?, is_valid = ?, is_given_to_user = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, voucher.getCategoryId());
            statement.setInt(2, voucher.getUserWonId());
            statement.setInt(3, voucher.getMarketRelatedId());
            statement.setString(4, voucher.getCode());
            statement.setDate(5, new java.sql.Date(voucher.getExpiration().getTime()));
            statement.setDouble(6, voucher.getValue());
            statement.setInt(7, voucher.getUsageLimit());
            statement.setString(8, voucher.getType());
            statement.setBoolean(9, voucher.isValid());
            statement.setBoolean(10, voucher.isGivenToUser());
            statement.setInt(11, voucher.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a voucher by ID
    public void deleteVoucher(int id) {
        String query = "DELETE FROM voucher WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to extract Voucher object from ResultSet
    private Voucher extractVoucherFromResultSet(ResultSet resultSet) throws SQLException {
        Voucher voucher = new Voucher();
        voucher.setId(resultSet.getInt("id"));
        voucher.setCategoryId(resultSet.getInt("category_id"));
        voucher.setUserWonId(resultSet.getInt("user_won_id"));
        voucher.setMarketRelatedId(resultSet.getInt("market_related_id"));
        voucher.setCode(resultSet.getString("code"));
        voucher.setExpiration(resultSet.getDate("expiration"));
        voucher.setValue(resultSet.getDouble("value"));
        voucher.setUsageLimit(resultSet.getInt("usage_limit"));
        voucher.setType(resultSet.getString("type"));
        voucher.setValid(resultSet.getBoolean("is_valid"));
        voucher.setGivenToUser(resultSet.getBoolean("is_given_to_user"));
        return voucher;
    }
}
