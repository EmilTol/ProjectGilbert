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


    public void createListing(Listing ad) {
        if (ad.getModel() == null || ad.getModel().isEmpty()) {
            throw new IllegalArgumentException("Model is required");
        }
        listingRepository.save(ad);
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
    public List<Category> getAllCategories() {
        return listingRepository.findAllCategoriesFlat();
    }

    public List<Size> getAllSizes() {
        return listingRepository.findAllSizes();
    }

}