package com.dumitrascuantonio.auctionapp.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@Slf4j
public class CustomErrorController implements ErrorController {

    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            log.error("Error with status code: " + statusCode);
            model.addAttribute("error", statusCode);
        }

        return "error";
    }
}
