package com.example.projectgilbert.presentation;

import com.example.projectgilbert.application.LoginService;
import com.example.projectgilbert.application.ListingService;
import com.example.projectgilbert.application.SortingService;
import com.example.projectgilbert.application.UserService;
import com.example.projectgilbert.entity.Category;
import com.example.projectgilbert.entity.Listing;
import com.example.projectgilbert.entity.Size;
import com.example.projectgilbert.entity.User;
import com.example.projectgilbert.infrastructure.ListingRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    private final LoginService loginService;
    private final ListingService listingService;
    private final UserService userService;
    private final ListingRepository listingRepository;
    private final SortingService sortingService;

    @Autowired
    public MainController(LoginService loginService, ListingService listingService, UserService userService, ListingRepository listingRepository, SortingService sortingService) {
        this.loginService  = loginService;
        this.listingService = listingService;
        this.userService = userService;
        this.listingRepository = listingRepository;
        this.sortingService = sortingService;
    }


    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String showHome(HttpSession session, Model model,
                           @RequestParam(defaultValue = "createdAt") String sortBy,
                           @RequestParam(defaultValue = "desc") String direction) {
        model.addAttribute("currentUser", session.getAttribute("currentUser"));

        List<Listing> listings = listingService.getAllListings(); // Henter alle tilgængelige listings

        SortingService.Direction sortDirection;
        if (direction.equalsIgnoreCase("asc")) {
            sortDirection = SortingService.Direction.ASC;
        } else {
            sortDirection = SortingService.Direction.DESC;
        }

        if (sortBy.equalsIgnoreCase("price")) {
            listings = sortingService.byPrice(listings, sortDirection);
        } else {
            listings = sortingService.byDate(listings, sortDirection);
        }

        model.addAttribute("listings", listings);

        List<Category> parentCategories = listingService.getAllCategories(); // Henter forældrekategorier og deres underkategorier via ListingService
        // Opretter et map, der mapper forældrekategorier til deres underkategorier
        Map<Long, List<Category>> subCategoriesMap = listingService.getSubCategoriesForAllParents(parentCategories);

        // Tilføjer kategorier og underkategorier til model for brug i menu på telfon
        model.addAttribute("categories", parentCategories);
        model.addAttribute("subCategoriesMap", subCategoriesMap);

        // Tilføjer de aktuelle sorteringsparametre til model for brug i Thymeleaf
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        return "home";
    }



    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpSession session, Model model) {
        User loggedIn = loginService.login(user.getEmail(), user.getPassword()); //Logger ind ved hjælp af loginservice
        if (loggedIn != null) {
            session.setAttribute("currentUser", loggedIn); // gemmer den user som loggede ind i session
            return "redirect:/home";
        }
        model.addAttribute("error", "Incorrect email or password");
        return "login";
    }


    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        if (!model.containsAttribute("user")) { // Hvis vi ikke har en "user" så opretter vi en tom ny user og sender til signup
            model.addAttribute("user", new User());
        }
        return "signup";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           @RequestParam("confirmPassword") String confirmPassword,
                           Model model) {
        boolean success = loginService.register(user, confirmPassword); // opret bruger, henter user inform og tjekker at confirm password er korrekt
        if (success) { return "redirect:/login"; } // Hvis oprettelse gik godt går vi til login
        model.addAttribute("registrationError", "Password confirmation mismatch or email already in use");
        return "signup";
    }

    @GetMapping("/privateUser")
    public String showPrivateUser(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            model.addAttribute("needAuth", true);
            return "home";
        }
        List<Listing> userListings = listingService.getListingsForUser(currentUser.getUserId());

        model.addAttribute("user", currentUser);
        model.addAttribute("userListings", userListings);

        return "privateUser";
    }
    @GetMapping("/editUser")
    public String showEditUser(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            model.addAttribute("needAuth", true);
            return "home";
        }
        model.addAttribute("user", currentUser);

        return "editUser";
    }
    @PostMapping("/editUser")
    public String editUser(HttpSession session, Model model, @RequestParam (required = false) String email,
                           @RequestParam (required = false) String password,
                           @RequestParam (required = false) String firstName,
                           @RequestParam (required = false) String lastName,
                           @RequestParam (required = false) String phoneNumber) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/home";
        }
        userService.updateUser(currentUser.getUserId(), email, password, firstName, lastName, phoneNumber );

        //sørger for at "updatere" informationerne på siden
        User updatedUser = userService.getUserById(currentUser.getUserId());
        session.setAttribute("currentUser", updatedUser);

        return "redirect:/privateUser";

    }

    @GetMapping("/createSale")
    public String showCreateListingForm(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("saleAd", new Listing());

        // Hent alle kategorier fra databasen
        List<Category> allCategories = listingService.getAllCategories();
        List<Size> allSizes = listingService.getAllSizes();

        model.addAttribute("allCategories", allCategories);
        model.addAttribute("allSizes", allSizes);
        model.addAttribute("conditionsList", List.of("NEW", "LIKE_NEW", "GOOD", "FAIR", "POOR"));

        return "createSale";
    }

    @PostMapping("/createSale")
    public String createListing(@ModelAttribute("saleAd") Listing ad, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        ad.setSellerId(currentUser.getUserId());
        ad.setStatus("PENDING");
        ad.setFairTrade(false);
        ad.setValidated(false);

        listingService.createListing(ad);
        return "redirect:/privateUser";
    }

    @GetMapping("/listings/{id}")
    public String listingPage(@PathVariable Long id, Model model) {
        Listing listing = listingService.getListingById(id);
        model.addAttribute("listing", listing);
        return "listingPage";
    }
}
