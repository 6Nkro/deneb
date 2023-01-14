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
    public HashMap<String, Object> createBookcase(BookcaseDTO bookcase) throws JsonProcessingException {
        int bookcase_seq = bookcaseDAO.selectNextSeq();

        bookcase.setBookcase_seq(bookcase_seq);
        bookcaseDAO.insert(bookcase);

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> order = mapper.readValue(accountDAO.selectBookcaseOrderBySeq(bookcase.getAccount_seq()), ArrayList.class);
        order.add(bookcase_seq);

        AccountDTO account = new AccountDTO();
        account.setAccount_seq(bookcase.getAccount_seq());
        account.setBookcase_order(String.valueOf(order));
        accountDAO.updateOrderBySeq(account);

        return libraryService.getBookcase(bookcase_seq);
    }

    public BookcaseDTO editBookcase(BookcaseDTO bookcase) {
        bookcaseDAO.updateNameBySeq(bookcase);
        return bookcaseDAO.selectAllBySeq(bookcase.getBookcase_seq());
    }
}