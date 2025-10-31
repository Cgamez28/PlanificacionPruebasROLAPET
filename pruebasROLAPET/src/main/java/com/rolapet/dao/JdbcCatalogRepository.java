package com.rolapet.dao;

import com.rolapet.catalog.CatalogRepository;
import com.rolapet.catalog.Item;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de CatalogRepository.
 * Implementación mínima suficiente para los tests de integración de ejemplo.
 */
public class JdbcCatalogRepository implements CatalogRepository {

    private final DataSource dataSource;

    public JdbcCatalogRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT id, name, price FROM items";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Item it = new Item(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getInt("price")
                );
                items.add(it);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading items", e);
        }
        return items;
    }

    @Override
    public Optional<Item> findById(String id) {
        String sql = "SELECT id, name, price FROM items WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Item it = new Item(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("price")
                    );
                    return Optional.of(it);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding item by id", e);
        }
    }

    @Override
    public Item save(Item item) {
        // Intenta INSERT; si falla por PK duplicada, hacer UPDATE (simple)
        String insert = "INSERT INTO items (id, name, price) VALUES (?, ?, ?)";
        String update = "UPDATE items SET name = ?, price = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setString(1, item.getId());
                ps.setString(2, item.getName());
                ps.setInt(3, item.getPrice());
                ps.executeUpdate();
                return item;
            } catch (SQLException insertEx) {
                // intento de update
                try (PreparedStatement ps2 = conn.prepareStatement(update)) {
                    ps2.setString(1, item.getName());
                    ps2.setInt(2, item.getPrice());
                    ps2.setString(3, item.getId());
                    ps2.executeUpdate();
                    return item;
                } catch (SQLException updateEx) {
                    throw new RuntimeException("Error saving item (insert/update)", updateEx);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving item", e);
        }
    }
}
