package com.dumitrascuantonio.auctionapp.controller;

import com.dumitrascuantonio.auctionapp.exception.BetCreateException;
import com.dumitrascuantonio.auctionapp.exception.BetDeleteException;
import com.dumitrascuantonio.auctionapp.exception.BetNotFoundException;
import com.dumitrascuantonio.auctionapp.exception.ImageNotFoundException;
import com.dumitrascuantonio.auctionapp.exception.LotCreateException;
import com.dumitrascuantonio.auctionapp.exception.LotDeleteException;
import com.dumitrascuantonio.auctionapp.exception.LotNotFoundException;
import com.dumitrascuantonio.auctionapp.exception.UserNotFoundException;
import com.dumitrascuantonio.auctionapp.exception.WinningBetCreateException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandlerController {

    @ExceptionHandler({BetCreateException.class, BetDeleteException.class, LotCreateException.class,
            LotDeleteException.class, WinningBetCreateException.class})
    public String handle400CustomExceptions(Exception ex, Model model){
        log.error(ex.getMessage());
        model.addAttribute("error", 400);
        return "error";
    }

    @ExceptionHandler({BetNotFoundException.class, ImageNotFoundException.class, LotNotFoundException.class,
    UserNotFoundException.class})
    public String handle404CustomExceptions(Exception ex, Model model){
        log.error(ex.getMessage());
        model.addAttribute("error", 404);
        return "error";
    }
}
