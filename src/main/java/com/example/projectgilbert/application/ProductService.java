package com.example.projectgilbert.application;
import com.example.projectgilbert.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {


    public List<Product> getTopCategories() {
        Product sneakers = new Product("Sneakers", List.of());
        Product shoes = new Product("Shoes", List.of(sneakers));
        Product woman = new Product("Woman", List.of(shoes));
        return List.of(woman);
    }
}
