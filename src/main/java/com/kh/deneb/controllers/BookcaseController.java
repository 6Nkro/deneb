package com.kh.deneb.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.kh.deneb.service.BookcaseService;
import com.kh.deneb.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/bookcase")
public class BookcaseController {
    @Autowired
    BookcaseService bookcaseService;

    @Autowired
    LibraryService libraryService;

    @GetMapping("/list")
    @ResponseBody
    public String bookCaseList(String user_email, String user_pw) throws JsonProcessingException {
        return new Gson().toJson(libraryService.getBookcaseList(user_email, user_pw));
    }

    @PostMapping("/create")
    @ResponseBody
    public String createBookcase(String user_email, String user_pw, String bookcase_name) throws JsonProcessingException {
        return new Gson().toJson(bookcaseService.createBookcase(user_email, user_pw, bookcase_name));
    }

    @PostMapping("/delete")
    @ResponseBody
    public boolean deleteBookcase(int bookcase_seq) throws JsonProcessingException {
        return libraryService.deleteBookcase(bookcase_seq);
    }

    @PostMapping("/name")
    @ResponseBody
    public String editBookcaseName(int bookcase_seq, String bookcase_name) {
        return new Gson().toJson(bookcaseService.editBookcase(bookcase_seq, bookcase_name));
    }

    @PostMapping("/order")
    @ResponseBody
    public boolean editBookcaseOrder(String user_email, String user_pw, String bookcase_order) throws UnsupportedEncodingException {
        return libraryService.editBookcaseOrder(user_email, user_pw, bookcase_order);
    }
}