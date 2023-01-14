package com.kh.deneb.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.kh.deneb.dto.AccountDTO;
import com.kh.deneb.dto.BookcaseDTO;
import com.kh.deneb.service.BookcaseService;
import com.kh.deneb.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/bookcase")
public class BookcaseController {
    @Autowired
    BookcaseService bookcaseService;

    @Autowired
    LibraryService libraryService;

    @GetMapping("/list")
    @ResponseBody
    public String bookCaseList(int account_seq) throws JsonProcessingException {
        return new Gson().toJson(libraryService.getBookcaseList(account_seq));
    }

    @PostMapping("/create")
    @ResponseBody
    public String createBookcase(BookcaseDTO bookcase) throws JsonProcessingException {
        return new Gson().toJson(bookcaseService.createBookcase(bookcase));
    }

    @PostMapping("/delete")
    @ResponseBody
    public boolean deleteBookcase(int bookcase_seq) throws JsonProcessingException {
        return libraryService.deleteBookcase(bookcase_seq);
    }

    @PostMapping("/name")
    @ResponseBody
    public String editBookcaseName(BookcaseDTO bookcase) {
        return new Gson().toJson(bookcaseService.editBookcase(bookcase));
    }

    @PostMapping("/order")
    @ResponseBody
    public boolean editBookcaseOrder(AccountDTO account) {
        return libraryService.editBookcaseOrder(account);
    }
}