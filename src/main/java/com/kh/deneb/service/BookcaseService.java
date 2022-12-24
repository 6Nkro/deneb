package com.kh.deneb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.deneb.dao.AccountDAO;
import com.kh.deneb.dao.BookcaseDAO;
import com.kh.deneb.dto.AccountDTO;
import com.kh.deneb.dto.BookcaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class BookcaseService {

    @Autowired
    BookcaseDAO bookcaseDAO;

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    LibraryService libraryService;

    @Transactional
    public HashMap<String, Object> createBookcase(String user_email, String encrypted_pw, String bookcase_name) throws JsonProcessingException {
        int bookcase_seq = bookcaseDAO.selectNextSeq();
        int account_seq = libraryService.getAccountSeq(user_email, encrypted_pw);

        BookcaseDTO bookcase = new BookcaseDTO();
        bookcase.setBookcase_seq(bookcase_seq);
        bookcase.setAccount_seq(account_seq);
        bookcase.setBookcase_name(bookcase_name);
        bookcaseDAO.insert(bookcase);

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> order = mapper.readValue(accountDAO.selectBookcaseOrderBySeq(account_seq), ArrayList.class);
        order.add(bookcase_seq);

        AccountDTO account = new AccountDTO();
        account.setAccount_seq(account_seq);
        account.setBookcase_order(String.valueOf(order));
        accountDAO.updateOrderBySeq(account);

        return libraryService.getBookcase(bookcase_seq);
    }

    public BookcaseDTO editBookcase(int bookcase_seq, String bookcase_name) {
        BookcaseDTO bookcase = new BookcaseDTO();
        bookcase.setBookcase_seq(bookcase_seq);
        bookcase.setBookcase_name(bookcase_name);
        bookcaseDAO.updateNameBySeq(bookcase);
        return bookcaseDAO.selectAllBySeq(bookcase_seq);
    }
}