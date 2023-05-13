package com.dumitrascuantonio.auctionapp.controller;

import com.dumitrascuantonio.auctionapp.dto.SignUpRequest;
import com.dumitrascuantonio.auctionapp.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    @GetMapping("/sign-up")
    public String showSignUpForm(Model model) {
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "signUp";
    }

    @PostMapping("/sign-up")
    public String processSignUp(@Valid @ModelAttribute SignUpRequest signUpRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
           return "signUp";
        }
        userService.createUser(signUpRequest);

        return "redirect:/login";
    }
}
