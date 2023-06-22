package com.example.musicmate.controller;

import com.example.musicmate.entity.User;
import com.example.musicmate.repository.UserRepository;
import com.example.musicmate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.regex.Pattern;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam String gender,
            @RequestParam int age,
            Model model
    ) {
        try {
            userService.registerUser(email, password, name, gender, age);
            model.addAttribute("message", "Регистрация прошла успешно");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "registration";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            Model model
    ) {
        User user = userService.authenticateUser(email, password);

        if (user != null) {
            model.addAttribute("message", "Авторизация успешна");
        } else {
            model.addAttribute("error", "Неверный email или пароль");
        }

        return "login";
    }
}
