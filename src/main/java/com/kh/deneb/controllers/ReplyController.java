package com.kh.deneb.controllers;

import com.google.gson.Gson;
import com.kh.deneb.dto.ReplyDTO;
import com.kh.deneb.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    ReplyService replyService;

    @PostMapping("/send")
    @ResponseBody
    public String sendReply(ReplyDTO reply) {
        return new Gson().toJson(replyService.sendReply(reply));
    }

    @PostMapping("/delete")
    @ResponseBody
    public String deleteReply(ReplyDTO reply) {
        return new Gson().toJson(replyService.deleteReply(reply));
    }

    @GetMapping("/list")
    @ResponseBody
    public String getReplyList(int parent_bookcase_seq) {
        return new Gson().toJson(replyService.getReplyList(parent_bookcase_seq));
    }
}
