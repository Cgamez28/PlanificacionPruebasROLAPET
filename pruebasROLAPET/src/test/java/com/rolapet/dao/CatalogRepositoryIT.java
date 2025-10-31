/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rolapet.dao;

/**
 *
 * @author Cristian_Gamez
 */

import com.rolapet.catalog.Item;
import org.junit.jupiter.api.*;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.*;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CatalogRepositoryIT {

    private JdbcCatalogRepository repo;
    private Connection conn;

    @BeforeEach
    void setUp() throws Exception {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:rolapet;DB_CLOSE_DELAY=-1");
        conn = ds.getConnection();

        try (Statement s = conn.createStatement()) {
            s.execute("CREATE TABLE items (id VARCHAR PRIMARY KEY, name VARCHAR, price INT);");
            s.execute("INSERT INTO items VALUES ('1','Casco',10000);");
        }
        repo = new JdbcCatalogRepository(ds);
    }

    @AfterEach
    void tearDown() throws Exception {
        conn.close();
    }

    @Test
    void shouldFindSeededItem() {
        List<Item> items = repo.findAll();
        assertThat(items).hasSize(1);
        assertThat(items.get(0).getName()).isEqualTo("Casco");
    }
}
