package com.example.projectgilbert.presentation;

import com.example.projectgilbert.application.LoginService;
import com.example.projectgilbert.application.ProductService;
import com.example.projectgilbert.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private final LoginService loginService;
    private final ProductService productService;

    @Autowired
    public MainController(LoginService loginService, ProductService productService) {

        this.loginService = loginService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/login";
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
        User loggedIn = loginService.login(user.getEmail(), user.getPassword());
        if (loggedIn != null) {
            session.setAttribute("currentUser", loggedIn);
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @PostMapping("/register") // Bare kopieret, ting skal fjernes, er bare for træt ligenu
    public String register(@ModelAttribute User user, @RequestParam("confirmPassword") String confirmPassword, Model model) {
        boolean success = loginService.register(user, confirmPassword);
        if (success) {
            return "redirect:/login";
        } else {
            model.addAttribute("registrationError", "Forkert bekræft kodeord eller email allerede i brug");
            model.addAttribute("showRegistrationModal", true);
            model.addAttribute("user", new User());
            return "login";
        }
    }

    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        model.addAttribute("categories", productService.getTopCategories());
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        return "home";
    }

    @GetMapping("/privateUser")
    public String showPrivateUser(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        return "privateUser";
    }


}
