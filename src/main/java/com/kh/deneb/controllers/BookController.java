package com.kh.deneb.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.kh.deneb.dto.BookDTO;
import com.kh.deneb.service.BookService;
import com.kh.deneb.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    LibraryService libraryService;

    @GetMapping("/list")
    @ResponseBody
    public String bookList(int parent_bookcase_seq) throws JsonProcessingException {
        return new Gson().toJson(libraryService.getBookList(parent_bookcase_seq));
    }

    @PostMapping("/create")
    @ResponseBody
    public String createBook(BookDTO book) throws JsonProcessingException {
        return new Gson().toJson(bookService.createBook(book));
    }

    @PostMapping("/edit")
    @ResponseBody
    public String editBook(BookDTO book) throws JsonProcessingException {
        return new Gson().toJson(bookService.editBook(book));
    }

    @PostMapping("/order")
    @ResponseBody
    public boolean editBookOrder(int bookcase_seq, String book_order) throws UnsupportedEncodingException {
        return libraryService.editBookOrder(bookcase_seq, book_order);
    }

    @PostMapping("/state")
    @ResponseBody
    public boolean editBookState(int book_seq, char book_open) {
        return bookService.editBookState(book_seq, book_open);
    }

    @PostMapping("/delete")
    @ResponseBody
    public boolean deleteBook(int book_seq) throws JsonProcessingException {
        return libraryService.deleteBook(book_seq);
    }

    @PostMapping("/move")
    @ResponseBody
    public String moveBook(String user_email, String user_pw, int book_seq, int origin_seq, int destination_seq) throws JsonProcessingException {
        return new Gson().toJson(libraryService.moveBook(user_email, user_pw, book_seq, origin_seq, destination_seq));
    }
}
