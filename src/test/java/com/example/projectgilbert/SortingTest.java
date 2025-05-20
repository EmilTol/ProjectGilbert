package com.example.projectgilbert;

import com.example.projectgilbert.application.SortingService;
import com.example.projectgilbert.entity.Listing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class SortingTest {

    private SortingService sortingService;

    private Listing expensiveOld; // Høj pris, ældste dato
    private Listing midMid; // Mellem pris, mellem dato
    private Listing cheapNew; // Lav pris, nyeste dato
    private List<Listing> listings;

    @BeforeEach
    void setUp() {
        sortingService = new SortingService();

        expensiveOld = new Listing();
        expensiveOld.setListingId(1L); // L fordi vi bruger en Long xD, gav mig slet ikke mange problemer
        expensiveOld.setItemType("Expensive Coat");
        expensiveOld.setPrice(new BigDecimal("200.00"));
        expensiveOld.setCreatedAt(LocalDateTime.now().minusDays(2));

        midMid = new Listing();
        midMid.setListingId(2L);
        midMid.setItemType("Okay Sweater");
        midMid.setPrice(new BigDecimal("100.00"));
        midMid.setCreatedAt(LocalDateTime.now().minusDays(1));

        cheapNew = new Listing();
        cheapNew.setListingId(3L);
        cheapNew.setItemType("Cheap Hat");
        cheapNew.setPrice(new BigDecimal("50.00"));
        cheapNew.setCreatedAt(LocalDateTime.now());

        listings = new ArrayList<>();
        listings.add(expensiveOld);
        listings.add(midMid);
        listings.add(cheapNew);
    }

    @Test
    void testPriceAsc() { // tester pris lav til høj
        List<Listing> sorted = sortingService.byPrice(listings, SortingService.Direction.ASC);

        assertEquals(3, sorted.size());
        assertEquals(cheapNew.getListingId(), sorted.get(0).getListingId());
        assertEquals(midMid.getListingId(), sorted.get(1).getListingId());
        assertEquals(expensiveOld.getListingId(), sorted.get(2).getListingId());

        System.out.println("Pris lav til høj korrekt");
    }

    @Test
    void testPriceDesc() { // Tester pris høj til lav
        List<Listing> sorted = sortingService.byPrice(listings, SortingService.Direction.DESC);

        assertEquals(3, sorted.size());
        assertEquals(expensiveOld.getListingId(), sorted.get(0).getListingId());
        assertEquals(midMid.getListingId(), sorted.get(1).getListingId());
        assertEquals(cheapNew.getListingId(), sorted.get(2).getListingId());

        System.out.println("Høj til lav pris korrekt");
    }

    @Test
    void testDateAsc() { // Tester gammel til ny
        List<Listing> sorted = sortingService.byDate(listings, SortingService.Direction.ASC);

        assertEquals(3, sorted.size());
        assertEquals(expensiveOld.getListingId(), sorted.get(0).getListingId());
        assertEquals(midMid.getListingId(), sorted.get(1).getListingId());
        assertEquals(cheapNew.getListingId(), sorted.get(2).getListingId());

        System.out.println("Gammel til ny korrekt");
    }

    @Test
    void testDateDesc() { // Tester ny til gammel
        List<Listing> sorted = sortingService.byDate(listings, SortingService.Direction.DESC);

        assertEquals(3, sorted.size());
        assertEquals(cheapNew.getListingId(), sorted.get(0).getListingId());
        assertEquals(midMid.getListingId(), sorted.get(1).getListingId());
        assertEquals(expensiveOld.getListingId(), sorted.get(2).getListingId());

        System.out.println("Ny til gammel korrekt");
    }
}