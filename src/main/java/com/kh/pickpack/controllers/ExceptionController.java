package com.kh.pickpack.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;

public class ExceptionController implements ErrorController {
    @GetMapping("/error")
    public String redirectRoot() {
        return "index";
    }

    public String getErrorPath() {
        return null;
    }
}