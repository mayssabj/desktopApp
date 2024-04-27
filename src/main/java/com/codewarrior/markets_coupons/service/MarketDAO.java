package com.codewarrior.markets_coupons.service;

import com.codewarrior.markets_coupons.model.Market;
import com.codewarrior.markets_coupons.util.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class MarketDAO {

    private final Connection connection;

    public MarketDAO() {
        System.out.println("MDAO INITITALIZED !");
        this.connection = DataBase.getConnection();
    }

    // Method to insert a new market into the database
    public void addMarket(Market market) {
        String query = "INSERT INTO market (name, address, region, city, zip_code, image) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, market.getName());
            statement.setString(2, market.getAddress());
            statement.setString(3, market.getCity());
            statement.setString(4, market.getRegion());
            statement.setInt(5, market.getZipCode());
            statement.setString(6, market.getImage());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all markets from the database
    public List<Market> getAllMarkets() {
        List<Market> markets = new ArrayList<>();
        String query = "SELECT * FROM market";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Market market = new Market();
                market.setId(resultSet.getInt("id"));
                market.setName(resultSet.getString("name"));
                market.setImage(resultSet.getString("image"));
                market.setAddress(resultSet.getString("address"));
                market.setCity(resultSet.getString("city"));
                market.setRegion(resultSet.getString("region"));
                market.setZipCode(resultSet.getInt("zip_code"));
                markets.add(market);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return markets;
    }
    public ObservableList<Market> getAllMarketsOb() {
        ObservableList<Market> markets = FXCollections.observableArrayList();
        String query = "SELECT * FROM market";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Market market = new Market();
                market.setId(resultSet.getInt("id"));
                market.setName(resultSet.getString("name"));
                market.setImage(resultSet.getString("image"));
                market.setAddress(resultSet.getString("address"));
                market.setCity(resultSet.getString("city"));
                market.setRegion(resultSet.getString("region"));
                market.setZipCode(resultSet.getInt("zip_code"));
                markets.add(market);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return markets;
    }

    // get market by ID
    public Market getMarketById(int id) {
        String query = "SELECT * FROM market WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Market market = new Market();
                    market.setId(resultSet.getInt("id"));
                    market.setName(resultSet.getString("name"));
                    market.setAddress(resultSet.getString("address"));
                    market.setCity(resultSet.getString("city"));
                    market.setRegion(resultSet.getString("region"));
                    market.setZipCode(resultSet.getInt("zip_code"));
                    return market;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Market getMarketByName(String name) {
        String query = "SELECT * FROM market WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Market market = new Market();
                    market.setId(resultSet.getInt("id"));
                    market.setName(resultSet.getString("name"));
                    market.setAddress(resultSet.getString("address"));
                    market.setCity(resultSet.getString("city"));
                    market.setRegion(resultSet.getString("region"));
                    market.setZipCode(resultSet.getInt("zip_code"));
                    return market;
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to update an existing market
    public void updateMarket(Market market) {
        System.out.println("in marketDAO :> "+ market.toString());
        String query = "UPDATE market SET name = ?, address = ?, city = ?, region = ?, zip_code = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, market.getName());
            statement.setString(2, market.getAddress());
            statement.setString(3, market.getCity());
            statement.setString(4, market.getRegion());
            statement.setInt(5, market.getZipCode());
            statement.setInt(6, market.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a market by ID
    public void deleteMarket(int id) {
        String query = "DELETE FROM market WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
