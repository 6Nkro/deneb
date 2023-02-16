package com.kh.deneb.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.kh.deneb.dto.AccountDTO;
import com.kh.deneb.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping("/chkEmail")
    @ResponseBody
    public boolean emailCheck(String user_email) {
        return accountService.isExistedEmail(user_email);
    }

    @GetMapping("/chkName")
    @ResponseBody
    public boolean nameCheck(String user_name) {
        return accountService.isExistedName(user_name);
    }

    @PostMapping("/join")
    @ResponseBody
    public String accountJoin(AccountDTO account) throws JsonProcessingException {
        return new Gson().toJson(accountService.getDefaultAccount(account));
    }

    @PostMapping("/login")
    @ResponseBody
    public String accountLogin(String user_email, String user_pw) throws JsonProcessingException {
        log.info(user_email);
        return new Gson().toJson(accountService.getAccount(user_email, user_pw));
    }
}
