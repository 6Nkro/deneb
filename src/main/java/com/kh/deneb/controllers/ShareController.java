package com.kh.deneb.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.kh.deneb.dto.BookcaseDTO;
import com.kh.deneb.dto.SubBookcaseDTO;
import com.kh.deneb.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/share")
public class ShareController {

    @Autowired
    ShareService shareService;

    @PostMapping("/create")
    @ResponseBody
    public String createSharePost(int origin_seq, SubBookcaseDTO subBookcase) throws UnsupportedEncodingException, JsonProcessingException {
        return new Gson().toJson(shareService.createSharePost(origin_seq, subBookcase));
    }

    @PostMapping("/import")
    @ResponseBody
    public String importSharePost(String user_email, String user_pw, String share_code) throws JsonProcessingException {
        return new Gson().toJson(shareService.getSharePost(user_email, user_pw, share_code));
    }


    @PostMapping("/like")
    @ResponseBody
    public int likePost(String user_email, String user_pw, int bookcase_seq, boolean like_already) {
        return shareService.getLikeCount(user_email, user_pw, bookcase_seq, like_already);
    }

    @GetMapping("/list")
    @ResponseBody
    public String getPostList(int page) {
        return new Gson().toJson(shareService.getPostList(page));
    }

    @GetMapping("/search")
    @ResponseBody
    public String getPostSearchList(int page, String search_type, String search_text) {
        return new Gson().toJson(shareService.getPostSearchList(page, search_type, search_text));
    }

    @GetMapping("/detail")
    @ResponseBody
    public String getBookList(BookcaseDTO bookcase) throws JsonProcessingException {
        return new Gson().toJson(shareService.getPostDetail(bookcase));
    }

    @GetMapping("/myshare")
    @ResponseBody
    public String getShareList(int account_seq) {
        return new Gson().toJson(shareService.getShareList(account_seq));
    }

    @GetMapping("/chart")
    @ResponseBody
    public String getChartList() {
        return new Gson().toJson(shareService.getChartList());
    }

    @PostMapping("/delete")
    @ResponseBody
    public String getShareList(int account_seq, int bookcase_seq) {
        return new Gson().toJson(shareService.deleteShare(account_seq, bookcase_seq));
    }
}
