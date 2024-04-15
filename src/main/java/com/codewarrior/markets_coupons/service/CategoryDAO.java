package com.codewarrior.markets_coupons.service;

import com.codewarrior.markets_coupons.model.Market;
import com.codewarrior.markets_coupons.model.VoucherCategory;
import com.codewarrior.markets_coupons.util.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private Connection connection;

    // Constructor
    public CategoryDAO(Connection connection) {
        this.connection = connection;
    }

    public CategoryDAO() {
        System.out.println("CVDAO INITITALIZED !");
        this.connection = DataBase.getConnection();
    }

    // Create operation
    public void createCategory(VoucherCategory category) throws SQLException {
        String sql = "INSERT INTO voucher_category (titre, discription) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getTitre());
            statement.setString(2, category.getDescription());
            statement.executeUpdate();
        }
    }

    // Read operation - Get all categories
    public List<VoucherCategory> getAllCategories() throws SQLException {
        List<VoucherCategory> categories = new ArrayList<>();
        String sql = "SELECT id, titre, description FROM voucher_category";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titre = resultSet.getString("titre");
                String description = resultSet.getString("description");
                VoucherCategory category = new VoucherCategory(id, titre, description);
                categories.add(category);
            }
        }
        return categories;
    }

    // Read operation - Get category by ID
    public VoucherCategory getCategoryById(int categoryId) throws SQLException {
        String sql = "SELECT titre, description FROM voucher_category WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String titre = resultSet.getString("titre");
                    String description = resultSet.getString("description");
                    return new VoucherCategory(categoryId, titre, description);
                }
            }
        }
        return null; // Return null if category with given ID not found
    }

    // Update operation
    public void updateCategory(VoucherCategory category) throws SQLException {
        String sql = "UPDATE voucher_category SET titre = ?, description = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getTitre());
            statement.setString(2, category.getDescription());
            statement.setInt(3, category.getId());
            statement.executeUpdate();
        }
    }

    // Delete operation
    public void deleteCategory(int categoryId) throws SQLException {
        String sql = "DELETE FROM voucher_category WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            statement.executeUpdate();
        }
    }

    public ObservableList<VoucherCategory> getAllCategoryOb() {

        ObservableList<VoucherCategory> categories = FXCollections.observableArrayList();
        String query = "SELECT * FROM voucher_category";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                VoucherCategory category = new VoucherCategory();
                category.setId(resultSet.getInt("id"));
                category.setTitre(resultSet.getString("titre"));
                category.setDescription(resultSet.getString("discription"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}
