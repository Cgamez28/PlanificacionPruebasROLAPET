/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rolapet.catalog;

import java.util.List;

/**
 *
 * @author Cristian_Gamez
 */
// src/main/java/com/rolapet/catalog/CatalogService.java
public class CatalogService {
    private final CatalogRepository repo;
    public CatalogService(CatalogRepository repo) { this.repo = repo; }

    public List<Item> getAllItems() {
        return repo.findAll();
    }

    public Item addItem(Item item) {
        if (item.getName() == null || item.getName().isBlank()) {
            throw new IllegalArgumentException("Name required");
        }
        return repo.save(item);
    }
}
