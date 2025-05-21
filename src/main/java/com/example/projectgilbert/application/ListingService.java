package com.example.projectgilbert.application;
import com.example.projectgilbert.entity.Category;
import com.example.projectgilbert.entity.Listing;
import com.example.projectgilbert.entity.Size;
import com.example.projectgilbert.entity.User;
import com.example.projectgilbert.infrastructure.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListingService {

    private final ListingRepository listingRepository;
    private final SortingService sortingService;

    @Autowired
    public ListingService(ListingRepository listingRepository, SortingService sortingService) {
        this.listingRepository = listingRepository;
        this.sortingService = sortingService;
    }

    //opretter ny listing
    public void createListing(Listing ad) {
        //tjekker at input for model ikke er null/empty
        if (ad.getModel() == null || ad.getModel().isEmpty()) {
            throw new IllegalArgumentException("Model is required");
        }
        if (ad.getBrand() == null || ad.getBrand().isEmpty()) {
            throw new IllegalArgumentException("Brand is required");
        }
        
        //gemmer input og opretter ny listing
        listingRepository.save(ad);
    }

    //henter alle listings fra en bruger med status approved
    public List<Listing> getApprovedListingsForUser(Long userId) {
        return listingRepository.findListingsBySellerIdAndStatus(userId, "APPROVED");
    }

    //henter alle listings fra en bruger med status sold
    public List<Listing> getSoldListingsForUser(Long userId) {
        return listingRepository.findListingsBySellerIdAndStatus(userId, "SOLD");
    }

    //henter alle listings fra en bruger med status pending
    public List<Listing> getPendingListingsForUser(Long userId) {
        return listingRepository.findListingsBySellerIdAndStatus(userId, "PENDING");
    }

    public Listing getListingById(Long id) {
        return listingRepository.findListingById(id);
    }

    //henter alle categorier til brug i oprettelse af ny listing
    public List<Category> getAllCategories() {
        return listingRepository.findAllCategoriesFlat();
    }

    //henter alle størrelser til brug i oprettelse af ny listing
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
    //henter alle Listings med status pending
    public List<Listing> getPendingListings() {
        return listingRepository.findByStatus(Listing.Status.PENDING);
    }

    //til godkendelse af listings med status pending
    public void approveListingById(Long listingId, User currentUser) {
        //tjekker om den logget ind bruger er admin
        if (currentUser == null || !currentUser.isAdmin()) {
            throw new SecurityException("Only admins can approve listings.");
        }
        Listing listing = listingRepository.findListingById(listingId);

        //tjekker at en listing er valgt og har statuspending
        if (listing != null && listing.getStatus() == Listing.Status.PENDING) {

            //listing status bliver updateret til approved
            listing.setStatus(Listing.Status.APPROVED);
            listingRepository.update(listing);
        }
    }

    //til afvisning af listings med status pending
    public void denyListingById(Long listingId, User currentUser) {
        //tjekker om den logget ind bruger er admin
        if (currentUser == null || !currentUser.isAdmin()) {
            throw new SecurityException("Only admins can deny listings.");
        }

        Listing listing = listingRepository.findListingById(listingId);

        //tjekker at en listing er valgt og har statuspending
        if (listing != null && listing.getStatus() == Listing.Status.PENDING) {

            //listing status bliver updateret til removed
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

    public List<Listing> getListingsByCategory(Long categoryId) {
        List<Listing> raw = listingRepository.findByCategoryId(categoryId);
        return sortingService.byDate(raw, SortingService.Direction.DESC);
    }

    // Ny metode til forsiden, viser kun godkendte listings baseret på søgning eller kategori
    public List<Listing> getApprovedListings(String query, Long categoryId) {
        List<Listing> raw;

        if (categoryId != null) {
            raw = getListingsByCategory(categoryId);
        } else {
            raw = searchListings(query);
        }

        // Filtrer så kun APPROVED listings vises
        return raw.stream()
                .filter(listing -> listing.getStatus() == Listing.Status.APPROVED)
                .collect(Collectors.toList());
    }

    public List<Listing> getActiveListingsByUserId(Long userId) {
        return listingRepository.findByUserIdAndStatus(userId, Listing.Status.APPROVED);
    }

    public List<Listing> getSoldListingsByUserId(Long userId) {
        return listingRepository.findByUserIdAndStatus(userId, Listing.Status.SOLD);
    }

}