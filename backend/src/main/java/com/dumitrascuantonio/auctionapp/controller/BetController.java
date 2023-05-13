package com.dumitrascuantonio.auctionapp.controller;

import com.dumitrascuantonio.auctionapp.dto.request.BetRequest;
import com.dumitrascuantonio.auctionapp.entity.Lot;
import com.dumitrascuantonio.auctionapp.entity.User;
import com.dumitrascuantonio.auctionapp.exception.BetCreateException;
import com.dumitrascuantonio.auctionapp.service.BetService;
import com.dumitrascuantonio.auctionapp.service.LotService;
import com.dumitrascuantonio.auctionapp.service.UserService;
import java.security.Principal;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bet")
@Slf4j
public class BetController {

    @Autowired
    private LotService lotService;

    @Autowired
    private BetService betService;

    @Autowired
    private UserService userService;

    @GetMapping("/create/{lotId}")
    public String showBetCreationForm(@PathVariable("lotId") Long lotId,
                                      Model model, Principal principal) {
        Lot lot = lotService.getLotById(lotId);
        User user = userService.getUserByPrincipal(principal);

        if (lot.getUser().equals(user)) {
            throw new BetCreateException("You cannot bet on your lot.");
        }

        model.addAttribute("betRequest", new BetRequest());
        model.addAttribute("lotId", lotId);

        return "betCreateForm";
    }

    @PostMapping("/create/{lotId}")
    public String processBetCreation(@PathVariable("lotId") Long lotId, @Valid @ModelAttribute BetRequest betRequest,
                                     BindingResult bindingResult, Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "bet-create-form";
        }

        try {
            betService.createBet(lotId, betRequest, principal);
        } catch (BetCreateException e) {
            log.error(e.getMessage());
            model.addAttribute("betError", e.getMessage());
        }

        return "redirect:/lot" + lotId;
    }

    @GetMapping("/delete/{id}")
    public String deleteBet(@PathVariable("id") Long id, Principal principal) {
        Long lotId = betService.getBetById(id).getLot().getId();
        betService.deleteBet(id, principal);
        return "redirect:/lot" + lotId;
    }
}
