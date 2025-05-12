package com.example.projectgilbert.application;
import com.example.projectgilbert.entity.Category;
import com.example.projectgilbert.entity.Product;
import com.example.projectgilbert.entity.Listing;
import com.example.projectgilbert.entity.Size;
import com.example.projectgilbert.infrastructure.ListingRepository;
import com.example.projectgilbert.infrastructure.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ListingRepository listingRepository;

    public ProductService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public List<Product> getTopCategories() {
        Product sneakers = new Product("Sneakers", List.of());
        Product shoes = new Product("Shoes", List.of(sneakers));
        Product woman = new Product("Woman", List.of(shoes));
        return List.of(woman);
    }

    public void createListing(Listing ad) {
        if (ad.getModel() == null || ad.getModel().isEmpty()) {
            throw new IllegalArgumentException("Model is required");
        }
        listingRepository.save(ad);
    }

    public List<Category> getAllMainCategories() {
        return listingRepository.findAllCategories();
    }


    public List<Category> getSubCategories(Long parentId) {
        return listingRepository.findSubCategoriesByParentId(parentId);
    }


    public List<Size> getSizesForCategory(Long categoryId) {
        return listingRepository.findSizesByCategoryId(categoryId);
    }

    public List<Listing> getListingsForUser(Long userId) {
        return listingRepository.findListingsBySellerId(userId);
    }

    public List<Listing> getAllListings() {
        return listingRepository.findAllListings();
    }


    public Listing getListingById(Long id) {
        return listingRepository.findListingById(id);
    }

}