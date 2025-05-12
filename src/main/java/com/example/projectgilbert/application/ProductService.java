package com.example.projectgilbert.application;
import com.example.projectgilbert.entity.Category;
import com.example.projectgilbert.entity.Product;
import com.example.projectgilbert.entity.SaleAdvertisement;
import com.example.projectgilbert.entity.Size;
import com.example.projectgilbert.infrastructure.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getTopCategories() {
        Product sneakers = new Product("Sneakers", List.of());
        Product shoes = new Product("Shoes", List.of(sneakers));
        Product woman = new Product("Woman", List.of(shoes));
        return List.of(woman);
    }

    public void createListing(SaleAdvertisement ad) {
        if (ad.getModel() == null || ad.getModel().isEmpty()) {
            throw new IllegalArgumentException("Model is required");
        }
        productRepository.save(ad);
    }

    public List<Category> getAllMainCategories() {
        return productRepository.findAllCategories();
    }


    public List<Category> getSubCategories(Long parentId) {
        return productRepository.findSubCategoriesByParentId(parentId);
    }


    public List<Size> getSizesForCategory(Long categoryId) {
        return productRepository.findSizesByCategoryId(categoryId);
    }

    public List<SaleAdvertisement> getListingsForUser(Long userId) {
        return productRepository.findListingsBySellerId(userId);
    }

}