package com.dumitrascuantonio.auctionapp.controller;

import com.dumitrascuantonio.auctionapp.dto.response.LotWithImageResponse;
import com.dumitrascuantonio.auctionapp.entity.Lot;
import com.dumitrascuantonio.auctionapp.service.ImageService;
import com.dumitrascuantonio.auctionapp.service.LotService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private LotService lotService;

    @GetMapping({"/", "/index", "/home"})
    public String showHomePage(Model model) {
        List<Lot> first10Lots = lotService.getFirst10Lots();

        List<LotWithImageResponse> lotsWithImage = imageService.getLotsWithImages(first10Lots);
        model.addAttribute("lotsWithImage", lotsWithImage);

        return "index";
    }
}
