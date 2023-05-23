package com.dumitrascuantonio.auctionapp.controller;

import com.dumitrascuantonio.auctionapp.dto.request.BetRequest;
import com.dumitrascuantonio.auctionapp.entity.Lot;
import com.dumitrascuantonio.auctionapp.entity.User;
import com.dumitrascuantonio.auctionapp.exception.BetCreateException;
import com.dumitrascuantonio.auctionapp.service.BetService;
import com.dumitrascuantonio.auctionapp.service.LotService;
import com.dumitrascuantonio.auctionapp.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import javax.validation.Valid;
import java.security.Principal;
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
    public String showBetCreationForm(@PathVariable("lotId") long lotId,
                                      Model model,
                                      Principal principal){

        Lot lot = lotService.getLotById(lotId);
        User user = userService.getUserByPrincipal(principal);

        if(lot.getUser().equals(user)){
            throw new BetCreateException("You can't bet on your lot.");
        }

        model.addAttribute("betRequest", new BetRequest());
        model.addAttribute("lotId", lotId);

        return "betCreateForm";
    }

    @PostMapping("/create/{lotId}")
    public String processBetCreation(@PathVariable("lotId") long lotId,
                                     @Valid @ModelAttribute BetRequest betRequest,
                                     BindingResult bindingResult,
                                     Model model,
                                     Principal principal){

        if (bindingResult.hasErrors()) {
            return "betCreateForm";
        }

        try {
            betService.createBet(lotId, betRequest, principal);
        }
        catch (BetCreateException ex){
            log.error(ex.getMessage());
            model.addAttribute("betError", ex.getMessage());
            return "betCreateForm";
        }

        return "redirect:/lot/" + lotId;
    }

    @GetMapping("/delete/{id}")
    public String deleteBet(@PathVariable("id") long id,
                            Principal principal) {
        long lotId = betService.getBetById(id).getLot().getId();
        betService.deleteBet(id, principal);
        return "redirect:/lot/" + lotId;
    }

}
