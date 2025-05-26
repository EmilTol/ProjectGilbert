package com.example.projectgilbert.application;

import com.example.projectgilbert.entity.Listing;
import com.example.projectgilbert.infrastructure.ListingRepository;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.mockito.Mockito.*;

class ListingServiceTest {


    @Test
    void createListing_validInput() {

        ListingRepository mockRepo = mock(ListingRepository.class);
        SortingService mockSortingService = mock(SortingService.class);

        ListingService listingService = new ListingService(mockRepo, mockSortingService);

    Listing ad = new Listing();
    ad.setBrand("Only");
    ad.setModel("Extra cool");
    ad.setItemType("T-shirt");
    ad.setPrice(new BigDecimal(200));
    ad.setColor("Black");
    ad.setMaterials("Cotton");
    ad.setDescription("Some description");
    ad.setStatus(Listing.Status.PENDING);
    ad.setValidated(false);
    ad.setFairTrade(false);

    listingService.createListing(ad);

    verify(mockRepo,times(1)).save(ad);

        System.out.println("Alt gemt korrekt");

    }

}