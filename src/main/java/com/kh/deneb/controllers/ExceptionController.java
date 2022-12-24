package com.kh.deneb.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExceptionController implements ErrorController {
    @GetMapping("/error")
    public String redirectRoot() {
        return "index";
    }

    public String getErrorPath() {
        return null;
    }
}