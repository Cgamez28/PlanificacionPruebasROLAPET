package com.rolapet.catalog;

// src/main/java/com/rolapet/catalog/CatalogRepository.java

import java.util.List;
import java.util.Optional;

public interface CatalogRepository {
    List<Item> findAll();
    Optional<Item> findById(String id);
    Item save(Item item);
}
