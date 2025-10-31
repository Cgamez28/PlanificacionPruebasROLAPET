/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rolapet.catalog;

/**
 *
 * @author Cristian_Gamez
 */
import java.util.Objects;

/**
 * POJO que representa un item del catálogo.
 */
public class Item {
    private String id;
    private String name;
    private int price; // precio en la unidad monetaria que estén usando (ej: centavos o pesos)

    public Item() {
    }

    public Item(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public Item setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }

    public int getPrice() {
        return price;
    }

    public Item setPrice(int price) {
        this.price = price;
        return this;
    }

    // equals y hashCode basados en id (clave primaria lógica)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString legible para logs y debugging
    @Override
    public String toString() {
        return "Item{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", price=" + price +
               '}';
    }
}
