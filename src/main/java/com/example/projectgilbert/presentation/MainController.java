package com.example.projectgilbert.presentation;

import com.example.projectgilbert.application.LoginService;
import com.example.projectgilbert.application.ProductService;
import com.example.projectgilbert.entity.Category;
import com.example.projectgilbert.entity.Listing;
import com.example.projectgilbert.entity.Size;
import com.example.projectgilbert.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {

    private final LoginService loginService;
    private final ProductService productService;

    @Autowired
    public MainController(LoginService loginService, ProductService productService) {
        this.loginService  = loginService;
        this.productService = productService;
    }


    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String showHome(HttpSession session, Model model) {
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
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
        List<Listing> userListings = productService.getListingsForUser(currentUser.getUserId());
        model.addAttribute("user", currentUser);
        model.addAttribute("userListings", userListings);
        return "privateUser";
    }

    @GetMapping("/createSale")
    public String showCreateListingForm(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("saleAd", new Listing());
        model.addAttribute("mainCategories", productService.getAllMainCategories());
        model.addAttribute("conditionsList", List.of("NEW", "LIKE_NEW", "GOOD", "FAIR", "POOR"));
        // Tilføj også kategori og størrelse fra din service hvis nødvendigt
        return "createSale";
    }

    @PostMapping("/createSale")
    public String createListing(@ModelAttribute("saleAd") Listing ad, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        //den her sætter SellerId som hvem end der er logget ind
        ad.setSellerId(currentUser.getUserId());
        //setter dem her da de ikke skal indtastes
        ad.setStatus("PENDING");
        ad.setFairTrade(false);
        ad.setValidated(false);

        productService.createListing(ad);
        return "redirect:/privateUser";
    }
    //dette mapper ikke til en html fil, bliver brugt i noget JavaScript, bruges til dynamisk dropdowns
    @GetMapping("/subcategories")
    @ResponseBody
    public List<Category> getSubCategories(@RequestParam Long parentId) {
        return productService.getSubCategories(parentId);
    }

    @GetMapping("/sizes")
    @ResponseBody
    public List<Size> getSizes(@RequestParam Long categoryId) {
        return productService.getSizesForCategory(categoryId);
    }
}
