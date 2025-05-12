package com.example.projectgilbert.presentation;

import com.example.projectgilbert.application.LoginService;
import com.example.projectgilbert.application.ProductService;
import com.example.projectgilbert.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            model.addAttribute("needAuth", true); // Kalder needAuth for at vise modal som spørger om login eller browse
            return "home";
        }
        model.addAttribute("user", currentUser);
        return "privateUser";
    }
}
