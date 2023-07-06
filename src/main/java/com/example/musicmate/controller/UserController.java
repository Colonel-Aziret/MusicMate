package com.example.musicmate.controller;


import com.example.musicmate.dto.NewPasswordUser;
import com.example.musicmate.entity.Token;
import com.example.musicmate.entity.User;
import com.example.musicmate.repository.UserRepository;
import com.example.musicmate.service.EmailSenderService;
import com.example.musicmate.service.TokenService;
import com.example.musicmate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerUser(user.getEmail(), user.getPassword(), user.getName(), user.getGender(), user.getAge());
            model.addAttribute("message", "Регистрация прошла успешно");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "login";
    }


//    @GetMapping("/login")
//    public String showLoginForm(Model model) {
//        model.addAttribute("user", new User());
//        return "login";
//    }

//    @PostMapping("/login")
//    public String login(@ModelAttribute("user") User user, Model model) {
//        User authenticatedUser = userService.authenticateUser(user.getEmail(), user.getPassword());
//
//        if (authenticatedUser != null) {
//            return "redirect:/"; // Перенаправление на главную страницу при успешной аутентификации
//        } else {
//            model.addAttribute("error", "Неправильный логин или пароль"); // Вывод ошибки через Thymeleaf
//            return "login";
//        }
//    }


    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }


    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
             RedirectAttributes redirectAttributes
    ) {
        User user = userService.authenticateUser(email, password);

        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Неверный email или пароль");
            return "redirect:/login";
        }
        return "redirect:/";
    }




//    @PostMapping("/login")
//    public String loginUser(
//            @RequestParam String email,
//            @RequestParam String password,
//            Model model
//    ) {
//        User user = userService.authenticateUser(email, password);
//
//        if (user != null) {
//            model.addAttribute("message", "Авторизация успешна");
//
//        } else {
//            model.addAttribute("error", "Неверный email или пароль");
//            return "redirect:/login";
//        }
//        return "redirect:/";
//    }

    @GetMapping(value = "/reset-password")
    public String resetPasswordPage() {
        return "reset-password";
    }

    @PostMapping(value = "/password-recovery-email")
    public ModelAndView getEmailForResetPassword(@RequestParam String email) throws MessagingException {
        ModelAndView modelAndView = new ModelAndView("change-password");
        User saved = userService.findByEmail(email);
        Token token = tokenService.saveToken(saved, tokenService.makeToken());
        emailSenderService.sendEmail(saved.getEmail(), "Введите данный токен, чтобы сбросить ваш пароль: " + String.valueOf(token.getToken()), "Восстановление пароля");
        NewPasswordUser newPasswordUser = new NewPasswordUser();
        newPasswordUser.setEmail(saved.getEmail());
        modelAndView.addObject("reset", newPasswordUser);
        return modelAndView;
    }

    @PostMapping(value = "/new-password-user")
    public String newPassword(@ModelAttribute(name = "reset") NewPasswordUser newPasswordUser) {
        try {
            User user = userService.findByEmail(newPasswordUser.getEmail());
            Token byUserAndToken = tokenService.findByUserAndToken(user, newPasswordUser.getToken());
            if (newPasswordUser.getPassword().equals(newPasswordUser.getRepeatPassword())) {
                user.setPassword(passwordEncoder.encode(newPasswordUser.getPassword()));
                userService.update(user);
                tokenService.deleteToken(byUserAndToken);
                return "redirect:/login";
            } else {
                return "change-password";
            }
        } catch (Exception e) {
            return "change-password";
        }
    }

}
