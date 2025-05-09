package com.example.projectgilbert.entity;

import java.util.List;

public class Product {
    private String name;
    private List<Product> subcategories;

    public Product(String name, List<Product> subcategories) {
        this.name = name;
        this.subcategories = subcategories;
    }

    public String getName() {
        return name;
    }

    public List<Product> getSubcategories() {
        return subcategories;
    }
}
