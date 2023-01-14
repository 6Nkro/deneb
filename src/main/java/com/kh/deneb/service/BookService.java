package com.kh.deneb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.deneb.dao.BookDAO;
import com.kh.deneb.dao.BookcaseDAO;
import com.kh.deneb.dto.BookDTO;
import com.kh.deneb.dto.BookcaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class BookService {

    @Autowired
    BookDAO bookDAO;

    @Autowired
    BookcaseDAO bookcaseDAO;

    @Autowired
    LibraryService libraryService;

    public HashMap<String, Object> createBook(BookDTO book) throws JsonProcessingException {
        int book_seq = bookDAO.selectNextSeq();
        int parent_bookcase_seq = book.getParent_bookcase_seq();

        book.setBook_seq(book_seq);
        bookDAO.insert(book);

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> order = mapper.readValue(bookcaseDAO.selectBookOrderBySeq(parent_bookcase_seq), ArrayList.class);
        order.add(book_seq);

        BookcaseDTO bookcase = new BookcaseDTO();
        bookcase.setBookcase_seq(parent_bookcase_seq);
        bookcase.setBook_order(String.valueOf(order));
        bookcaseDAO.updateOrderBySeq(bookcase);

        return libraryService.getBook(book_seq);
    }

    public boolean editBookState(BookDTO book) {
        return bookDAO.updateStateBySeq(book) == 1;
    }

    public HashMap<String, Object> editBook(BookDTO book) throws JsonProcessingException {
        bookDAO.updateBySeq(book);
        return libraryService.getBook(book.getBook_seq());
    }

    public int editBookIndex(BookDTO book) {
        return bookDAO.updateIndexBySeq(book);
    }
}
