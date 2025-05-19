package com.example.projectgilbert.application;
import com.example.projectgilbert.entity.Category;
import com.example.projectgilbert.entity.Listing;
import com.example.projectgilbert.entity.Size;
import com.example.projectgilbert.entity.User;
import com.example.projectgilbert.infrastructure.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ListingService {

    private final ListingRepository listingRepository;
    private final SortingService sortingService;

    @Autowired
    public ListingService(ListingRepository listingRepository, SortingService sortingService) {
        this.listingRepository = listingRepository;
        this.sortingService = sortingService;
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

//    public Map<Long, List<Category>> getSubCategoriesForAllParents(List<Category> parentCategories) { // Opretter en HashMap til at lagre underkategorier for hver forældrekategori
//        Map<Long, List<Category>> subCategoriesMap = new HashMap<>(); // Nøglen er forældrekategoriens ID, og værdien er en liste af underkategorier
//
//        for (Category parent : parentCategories) { // går igennem alle forældrekategorier for at hente deres underkategorier
//            List<Category> subCategories = listingRepository.findSubCategoriesByParentId(parent.getCategoryId()); // Henter underkategorier for den aktuelle forældrekategori ved hjælp af repository
//            subCategoriesMap.put(parent.getCategoryId(), subCategories); // Tilføjer underkategorierne til HashMap med forældrekategoriens ID som nøgle
//        }
//        return subCategoriesMap; // Returnerer HashMap med alle underkategorier organiseret efter forældrekategorier
//    }
    public List<Listing> getPendingListings() {
        return listingRepository.findByStatus(Listing.Status.PENDING);
    }

    public void approveListingById(Long listingId, User currentUser) {
        if (currentUser == null || !currentUser.isAdmin()) {
            throw new SecurityException("Only admins can approve listings.");
        }

        Listing listing = listingRepository.findListingById(listingId);
        if (listing != null && listing.getStatus() == Listing.Status.PENDING) {
            listing.setStatus(Listing.Status.APPROVED);
            listingRepository.update(listing);
        }
    }

    public void denyListingById(Long listingId, User currentUser) {
        if (currentUser == null || !currentUser.isAdmin()) {
            throw new SecurityException("Only admins can deny listings.");
        }

        Listing listing = listingRepository.findListingById(listingId);
        if (listing != null && listing.getStatus() == Listing.Status.PENDING) {
            listing.setStatus(Listing.Status.REMOVED);
            listingRepository.update(listing);
        }
    }

    public List<Listing> searchListings(String query) { // Søg efter lister baseret på en query

        List<Listing> raw; // Midlertidig liste til data som ikke er sorteret ( rå data )

        if (query == null || query.trim().isEmpty()) { // Tjek om query er null eller tom
            raw = listingRepository.findAllListings(); // Hent alle lister, hvis ingen query
        } else {
            String like = "%" + query.trim().toLowerCase() + "%"; // Forbered LIKE-mønster f.eks &Gucci%
            raw = listingRepository.findByLikePattern(like); // Søg med LIKE-mønster i model, brand eller description
        }

        return sortingService.byDate(raw, SortingService.Direction.DESC); // Sorter resultater efter dato (nyeste først)
    }
}