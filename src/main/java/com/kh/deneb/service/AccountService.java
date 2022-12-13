package com.kh.deneb.service;

import com.google.gson.Gson;
import com.kh.deneb.commons.CommonConstants;
import com.kh.deneb.dao.AccountDAO;
import com.kh.deneb.dao.StorageDAO;
import com.kh.deneb.dto.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
public class AccountService {
    @Autowired
    AccountDAO accountDAO;

    @Autowired
    StorageDAO storageDAO;

    @Transactional
    public boolean isJoin(AccountDTO dto) {
        String user_email = dto.getUser_email();
        String user_pw = dto.getUser_pw();
        String user_name = dto.getUser_name();

        boolean validEmail = user_email.matches(CommonConstants.EMAIL_REGEX);
        boolean validPw = user_pw.matches(CommonConstants.PW_REGEX);
        boolean validName = user_name.matches(CommonConstants.NAME_REGEX);

        if (!validEmail || !validPw || !validName) {
            return false;
        }

        int account_seq = accountDAO.selectNextSeq();
        int storage_seq = storageDAO.selectNextSeq();

        HashMap<String, Object> order = new HashMap<>();
        order.put("order", new int[]{storage_seq});

        HashMap<String, Object> data = new HashMap<>();
        data.put("account_no", account_seq);
        data.put("user_email", user_email);
        data.put("user_pw", user_pw);
        data.put("user_name", user_name);
        data.put("storage_order", new Gson().toJson(order));

        data.put("storage_no", storage_seq);

        return accountDAO.insert(data) == 1 && storageDAO.insertDefault(data) == 1;
    }

    public boolean isExistedEmail(String user_email) {
        return accountDAO.selectCountByEmail(user_email) >= 1;
    }

    public boolean isExistedName(String user_name) {
        return accountDAO.selectCountByName(user_name) >= 1;
    }

    public AccountDTO getAccount(String user_email, String user_pw) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("user_email", user_email);
        data.put("user_pw", user_pw);
        return accountDAO.getAccount(data);
    }
}