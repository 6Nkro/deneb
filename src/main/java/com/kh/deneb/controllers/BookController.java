package com.kh.deneb.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.kh.deneb.dto.BookDTO;
import com.kh.deneb.dto.BookcaseDTO;
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
    public boolean editBookOrder(BookcaseDTO bookcase) {
        return libraryService.editBookOrder(bookcase);
    }

    @PostMapping("/state")
    @ResponseBody
    public boolean editBookState(BookDTO book) {
        return bookService.editBookState(book);
    }

    @PostMapping("/index")
    @ResponseBody
    public int editBookIndex(BookDTO book) {
        return bookService.editBookIndex(book);
    }

    @PostMapping("/delete")
    @ResponseBody
    public boolean deleteBook(int book_seq) throws JsonProcessingException {
        return libraryService.deleteBook(book_seq);
    }

    @PostMapping("/move")
    @ResponseBody
    public String moveBook(int account_seq, int book_seq, int origin_seq, int destination_seq) throws JsonProcessingException {
        return new Gson().toJson(libraryService.moveBook(account_seq, book_seq, origin_seq, destination_seq));
    }
}
