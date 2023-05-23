package com.dumitrascuantonio.auctionapp.controller;

import com.dumitrascuantonio.auctionapp.dto.SignUpRequest;
import com.dumitrascuantonio.auctionapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;


    @GetMapping("/login")
    public String showLoginForm(Model model){
        return "login";
    }


    @GetMapping("/signup")
    public String showSignUpForm(Model model){
        model.addAttribute("signUpRequest", new SignUpRequest());

        return "signup";
    }

    @PostMapping("/signup")
    public String processSignUp(@Valid @ModelAttribute SignUpRequest signUpRequest, BindingResult bindingResult){

        if (userService.getUserByEmail(signUpRequest.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "error.user", "An account already exists for this email.");
        }
        if (userService.getUserByUsername(signUpRequest.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "error.user", "An account already exists for this username.");
        }

        if (!bindingResult.hasErrors()) {
            userService.createUser(signUpRequest);
        }

        return !bindingResult.hasErrors() ? "redirect:/login" : "signup";
    }
}
