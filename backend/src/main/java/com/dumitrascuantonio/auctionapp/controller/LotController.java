package com.dumitrascuantonio.auctionapp.controller;

import com.dumitrascuantonio.auctionapp.dto.request.LotRequest;
import com.dumitrascuantonio.auctionapp.dto.response.LotWithImageResponse;
import com.dumitrascuantonio.auctionapp.entity.Bet;
import com.dumitrascuantonio.auctionapp.entity.Image;
import com.dumitrascuantonio.auctionapp.entity.Lot;
import com.dumitrascuantonio.auctionapp.entity.User;
import com.dumitrascuantonio.auctionapp.entity.enumeration.Role;
import com.dumitrascuantonio.auctionapp.exception.ImageNotFoundException;
import com.dumitrascuantonio.auctionapp.service.BetService;
import com.dumitrascuantonio.auctionapp.service.ImageService;
import com.dumitrascuantonio.auctionapp.service.LotService;
import com.dumitrascuantonio.auctionapp.service.UserService;

import java.io.File;
import java.io.FileInputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/lot")
@Slf4j
public class LotController {

    @Autowired
    private LotService lotService;

    @Autowired
    private BetService betService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;


    @GetMapping(value = {"/all", "/all/{active}"})
    public String showLots(@PathVariable Optional<Boolean> active,
                          Model model) {
        List<Lot> lots;
        if (active.isPresent()) {
            if (active.get()) {
                lots = lotService.getAllLots(true);
            } else {
                lots = lotService.getAllLots(false);
            }
        } else {
            lots = lotService.getAllLots(true);
        }

        List<LotWithImageResponse> lotsWithImages = imageService.getLotsWithImages(lots);

        model.addAttribute("lotsWithImages", lotsWithImages);
        return "lots";
    }

    @GetMapping("/{id}")
    public String showLot(@PathVariable("id") Long id, Principal principal, Model model) {
        Lot lot  = lotService.getLotById(id);

        try {
            Image image = imageService.getImageToLot(lot.getId());
            if (image.getImageBytes().length != 0) {
                model.addAttribute("image", Base64.encodeBase64String(image.getImageBytes()));
            }
        } catch (ImageNotFoundException | IOException e) {
            log.debug(e.getMessage());
        }

        List<Bet> bets = betService.getAllBetsForLot(id);
        lot.setBets(bets);

        model.addAttribute("lot", lot);

        User currUser = userService.getUserByPrincipal(principal);
        model.addAttribute("currUserId", currUser.getId());
        if (currUser.getRoles().contains(Role.ROLE_ADMIN)) {
            model.addAttribute("isAdmin", true);
        }
        return "lot";
    }

    @Secured({"ROLE_ADMIN", "ROLE_AUCTIONEER"})
    @GetMapping("/create")
    public String showLotCreateForm(Model model) {

        model.addAttribute("lotRequest", new LotRequest());
        return "lotCreateForm";
    }

    @Secured({"ROLE_ADMIN", "ROLE_AUCTIONEER"})
    @PostMapping("/create")
    public String processLotCreation(@Valid @ModelAttribute LotRequest lotRequest,
                                     BindingResult bindingResult,
                                     @RequestParam("image") MultipartFile file,
                                     Principal principal) throws IOException {

        if (bindingResult.hasErrors()) {
            return "lotCreateForm";
        }

        Lot lot = lotService.createLot(lotRequest, principal);

        imageService.uploadImageToLot(file, lot.getId());

        return "redirect:/lot/" + lot.getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteLot(@PathVariable("id") long id,
                            Principal principal) {
        lotService.deleteLot(principal, id);
        return "lotDeleteSuccess";
    }

    @GetMapping("/search")
    public String findLotsByKeyword(@RequestParam("keyword") String keyword,
                                    Model model) throws IOException {
        List<Lot> lots = lotService.getLotsByKeyword(keyword);

        List<LotWithImageResponse> lotsWithImages = imageService.getLotsWithImages(lots);

        model.addAttribute("lotsWithImages", lotsWithImages);
        return "lots";
    }
}
