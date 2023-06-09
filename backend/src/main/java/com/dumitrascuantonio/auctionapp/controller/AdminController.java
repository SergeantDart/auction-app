package com.dumitrascuantonio.auctionapp.controller;

import com.dumitrascuantonio.auctionapp.entity.User;
import com.dumitrascuantonio.auctionapp.entity.enumeration.Role;
import com.dumitrascuantonio.auctionapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String showUsers(Model model){
        List<User> users = userService.getFirst10Users();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/users/search")
    public String findUsers(@RequestParam("keyword") String keyword,
                            Model model){
        List<User> users = userService.getUsersByKeyword(keyword);

        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/user/{id}")
    public String showUser(@PathVariable("id") Long id,
                           Model model,
                           @RequestParam(name = "success", required = false) boolean success){
        User user = userService.getUserById(id);

        Role[] allRoles = Role.values();
        model.addAttribute("allRoles", allRoles);
        if(success){
            model.addAttribute("success", success);
        }
        model.addAttribute("user", user);
        return "user";
    }

    @PostMapping("/user/{id}")
    public String changeUser(@PathVariable("id") Long id,
                             @ModelAttribute User user){
        User prevUser = userService.getUserById(id);
        if(prevUser.getRoles().contains(Role.ROLE_ADMIN)){
            user.getRoles().add(Role.ROLE_ADMIN);
        }
        user.getRoles().add(Role.ROLE_USER);
        prevUser.setRoles(user.getRoles());
        userService.updateUser(prevUser);
        return "redirect:/admin/user/" + id + "?success=true";
    }
}
