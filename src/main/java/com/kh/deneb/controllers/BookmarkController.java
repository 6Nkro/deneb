package com.kh.deneb.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.kh.deneb.dto.BookmarkDTO;
import com.kh.deneb.service.BookmarkService;
import com.kh.deneb.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/bookmark")
public class BookmarkController {

    @Autowired
    BookmarkService bookmarkService;

    @Autowired
    LibraryService libraryService;

    @GetMapping("/list")
    @ResponseBody
    public String bookmarkList(int parent_book_seq) throws JsonProcessingException {
        return new Gson().toJson(libraryService.getBookmarkList(parent_book_seq));
    }

    @PostMapping("/create")
    @ResponseBody
    public String createBookmark(BookmarkDTO bookmark) throws JsonProcessingException, UnsupportedEncodingException {
        return new Gson().toJson(bookmarkService.createBookmark(bookmark));
    }

    @PostMapping("/order")
    @ResponseBody
    public boolean editBookOrder(int book_seq, String bookmark_order) throws UnsupportedEncodingException {
        return libraryService.editBookmarkOrder(book_seq, bookmark_order);
    }

    @PostMapping("/move")
    @ResponseBody
    public String moveBookmark(BookmarkDTO bookmark) {
        return new Gson().toJson(bookmarkService.moveBookmark(bookmark));
    }

    @PostMapping("/edit")
    @ResponseBody
    public String editBookmark(BookmarkDTO bookmark) {
        return new Gson().toJson(bookmarkService.editBookmark(bookmark));
    }

    @PostMapping("/delete")
    @ResponseBody
    public boolean deleteBookmark(int bookmark_seq) throws JsonProcessingException {
        return libraryService.deleteBookmark(bookmark_seq);
    }

    @PostMapping("/time")
    @ResponseBody
    public int editVideoTime(BookmarkDTO bookmark) {
        return bookmarkService.editVideoTime(bookmark);
    }
}
