package com.example.projectgilbert.presentation;

import com.example.projectgilbert.application.LoginService;
import com.example.projectgilbert.application.ListingService;
import com.example.projectgilbert.application.SortingService;
import com.example.projectgilbert.application.UserService;
import com.example.projectgilbert.entity.Category;
import com.example.projectgilbert.entity.Listing;
import com.example.projectgilbert.entity.Size;
import com.example.projectgilbert.entity.User;
import com.example.projectgilbert.exception.LoginFailedException;
import com.example.projectgilbert.infrastructure.ListingRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
                           @RequestParam(defaultValue = "desc") String direction,
                           @RequestParam(name = "search", required = false) String search) {
        model.addAttribute("currentUser", session.getAttribute("currentUser"));

        List<Listing> listings = listingService.searchListings(search);

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
        model.addAttribute("search", search);

        List<Category> categories = listingService.getAllCategories(); // Henter forældrekategorier og deres underkategorier via ListingService

        // Tilføjer kategorier og underkategorier til model for brug i menu på telfon
        model.addAttribute("categories", categories);

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
        try {
            User loggedIn = loginService.login(user.getEmail(), user.getPassword());
            session.setAttribute("currentUser", loggedIn);
            return "redirect:/home";
        } catch (LoginFailedException e) {
            model.addAttribute("error", "Incorrect email or password");
            return "login";
        }
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
        model.addAttribute("isAdmin", currentUser.isAdmin());

        System.out.println("Current user name: " + currentUser.getFirstName());
        System.out.println("Current user role: " + currentUser.getRole());
        System.out.println("Is admin? " + currentUser.isAdmin());

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
    public String createListing(@ModelAttribute("saleAd") Listing ad,
                                @RequestParam("image") MultipartFile file,
                                HttpSession session,
                                Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        ad.setSellerId(currentUser.getUserId());
        ad.setStatus(Listing.Status.PENDING);
        ad.setFairTrade(false);
        ad.setValidated(false);

        if (file != null && !file.isEmpty()) {
            try {
                String originalFilename = file.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

                String uniqueFilename = UUID.randomUUID().toString() + extension;

                String uploadDir = new File("src/main/resources/static/uploads").getAbsolutePath();

                File destination = new File(uploadDir + "/" + uniqueFilename);
                file.transferTo(destination);

                ad.setImageFileName(uniqueFilename);

            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "There was an error uploading the image.");

                List<Category> allCategories = listingService.getAllCategories();
                List<Size> allSizes = listingService.getAllSizes();
                model.addAttribute("allCategories", allCategories);
                model.addAttribute("allSizes", allSizes);
                model.addAttribute("conditionsList", List.of("NEW", "LIKE_NEW", "GOOD", "FAIR", "POOR"));

                return "createSale";
            }
        }

        listingService.createListing(ad);
        return "redirect:/privateUser";
    }

    @GetMapping("/listings/{id}")
    public String listingPage(@PathVariable Long id, Model model) {
        Listing listing = listingService.getListingById(id);
        model.addAttribute("listing", listing);
        return "listingPage";
    }

    @GetMapping("/adminPanel")
    public String showAdminPanel(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || !currentUser.isAdmin()) {
            return "redirect:/home";
        }
        model.addAttribute("isAdmin", currentUser.isAdmin());

        return "/adminPanel";
    }

    @GetMapping("/newArrivals")
    public String showPendingListings(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || !currentUser.isAdmin()) {
            return "redirect:/home";
        }

        List<Listing> pendingListings = listingService.getPendingListings();
        model.addAttribute("listings", pendingListings);
        model.addAttribute("isAdmin", currentUser.isAdmin());
        return "/newArrivals";
    }

    @PostMapping("/approveListing")
    public String approveListing(@RequestParam("listingId") Long listingId, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || !currentUser.isAdmin()) {
            return "redirect:/home";
        }

        listingService.approveListingById(listingId, currentUser);
        return "redirect:newArrivals";
    }

    @PostMapping("/denyListing")
    public String denyListing(@RequestParam("listingId") Long listingId, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || !currentUser.isAdmin()) {
            return "redirect:/home";
        }

        listingService.denyListingById(listingId, currentUser);
        return "redirect:newArrivals";
    }

}
