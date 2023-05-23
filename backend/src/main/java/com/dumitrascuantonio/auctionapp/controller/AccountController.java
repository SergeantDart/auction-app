package com.dumitrascuantonio.auctionapp.controller;

import com.dumitrascuantonio.auctionapp.entity.Lot;
import com.dumitrascuantonio.auctionapp.entity.User;
import com.dumitrascuantonio.auctionapp.entity.WinningBet;
import com.dumitrascuantonio.auctionapp.service.LotService;
import com.dumitrascuantonio.auctionapp.service.UserService;
import com.dumitrascuantonio.auctionapp.service.WinningBetService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private LotService lotService;
    
    @Autowired
    private WinningBetService winningBetService;
    
    @GetMapping("/")
    public String showAccount(Principal principal,
                                Model model){

        User currUser = userService.getUserByPrincipal(principal);
        model.addAttribute("user", currUser);

        return "myAccount";
    }

    @GetMapping("/createdLots")
    public String showCreatedLots(Principal principal,
                                  Model model){

        List<Lot> activeLots = lotService.getAllLotsForUser(principal, true);
        List<Lot> completeLots = lotService.getAllLotsForUser(principal, false);

        model.addAttribute("activeLots", activeLots);
        model.addAttribute("completeLots", completeLots);

        return "accountLots";
    }

    @GetMapping("/participatedLots")
    public String showLotsUserParticipateIn(Principal principal,
                                            Model model){

        List<Lot> participatedLots = lotService.getLotsUserParticipatedIn(principal, true);
        model.addAttribute("participatedLots", participatedLots);

        return "accountLots";
    }

    @GetMapping("/wonLots")
    public String showLotsUserWon(Principal principal,
                                  Model model){
        List<WinningBet> winningBets = winningBetService.getAllWinningBetsForUser(principal);
        List<Lot> wonLots = new ArrayList<>();
        winningBets.stream()
                .forEach(winningBet -> wonLots.add(winningBet.getBet().getLot()));
        model.addAttribute("wonLots", wonLots);

        return "accountLots";
    }

}
