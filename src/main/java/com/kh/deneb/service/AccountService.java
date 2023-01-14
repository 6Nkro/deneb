package com.kh.deneb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kh.deneb.commons.CommonConstants;
import com.kh.deneb.commons.CommonUtils;
import com.kh.deneb.dao.AccountDAO;
import com.kh.deneb.dao.BookcaseDAO;
import com.kh.deneb.dto.AccountDTO;
import com.kh.deneb.dto.BookcaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;

@Service
public class AccountService {
    @Autowired
    AccountDAO accountDAO;

    @Autowired
    BookcaseDAO bookcaseDAO;

    @Autowired
    LibraryService libraryService;

    @Transactional
    public HashMap<String, Object> getDefaultAccount(AccountDTO account) throws JsonProcessingException {
        boolean validEmail = account.getUser_email().matches(CommonConstants.EMAIL_REGEX);
        boolean validPw = account.getUser_pw().matches(CommonConstants.PW_REGEX);
        boolean validName = account.getUser_name().matches(CommonConstants.NAME_REGEX);

        if (!validEmail || !validPw || !validName) {
            return null;
        }

        int account_seq = accountDAO.selectNextSeq();
        int bookcase_seq = bookcaseDAO.selectNextSeq();

        account.setAccount_seq(account_seq);
        account.setUser_pw(CommonUtils.getSHA512(account.getUser_pw()));
        account.setBookcase_order(Arrays.toString(new int[]{bookcase_seq}));

        BookcaseDTO bookcase = new BookcaseDTO();
        bookcase.setBookcase_seq(bookcase_seq);
        bookcase.setAccount_seq(account_seq);

        accountDAO.insert(account);
        bookcaseDAO.insertDefault(bookcase);

        HashMap<String, Object> data = new HashMap<>();
        data.put("account", account);
        data.put("library", libraryService.getBookcaseList(account.getAccount_seq()));

        return data;
    }

    public boolean isExistedEmail(String user_email) {
        return accountDAO.selectCountByEmail(user_email) >= 1;
    }

    public boolean isExistedName(String user_name) {
        return accountDAO.selectCountByName(user_name) >= 1;
    }

    public HashMap<String, Object> getAccount(String user_email, String user_pw) throws JsonProcessingException {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUser_email(user_email);
        accountDTO.setUser_pw(CommonUtils.getSHA512(user_pw));

        AccountDTO account = accountDAO.selectAllByEmailAndPw(accountDTO);
        if (account == null) {
            return null;
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("account", account);
        data.put("library", libraryService.getBookcaseList(account.getAccount_seq()));
        return data;
    }
}