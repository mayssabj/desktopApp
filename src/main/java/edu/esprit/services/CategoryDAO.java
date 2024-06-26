package edu.esprit.services;


import edu.esprit.utils.mydb;
import edu.esprit.entities.VoucherCategory;
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
        this.connection = mydb.getInstance().getCon();
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
        String sql = "SELECT id, titre, discription FROM voucher_category";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titre = resultSet.getString("titre");
                String description = resultSet.getString("discription");
                VoucherCategory category = new VoucherCategory(id, titre, description);
                categories.add(category);
            }
        }
        return categories;
    }

    // Read operation - Get category by ID
    public VoucherCategory getCategoryById(int categoryId) throws SQLException {
        String sql = "SELECT titre, discription FROM voucher_category WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String titre = resultSet.getString("titre");
                    String description = resultSet.getString("discription");
                    return new VoucherCategory(categoryId, titre, description);
                }
            }
        }
        return null; // Return null if category with given ID not found
    }

    public VoucherCategory getCategoryByTitle(String title) throws SQLException {
        String sql = "SELECT id, titre, discription FROM voucher_category WHERE titre = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    System.out.println("ena fi vouchercategory DAO : id => "+ id);
                    String titre = resultSet.getString("titre");
                    String description = resultSet.getString("discription");
                    return new VoucherCategory(id,titre, description);
                }
            }
        }
        return null; // Return null if category with given ID not found
    }

    // Update operation
    public void updateCategory(VoucherCategory category) throws SQLException {
        String sql = "UPDATE voucher_category SET titre = ?, discription = ? WHERE id = ?";
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