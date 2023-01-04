package com.kh.deneb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.deneb.dao.AccountDAO;
import com.kh.deneb.dao.BookDAO;
import com.kh.deneb.dao.BookcaseDAO;
import com.kh.deneb.dao.BookmarkDAO;
import com.kh.deneb.dto.AccountDTO;
import com.kh.deneb.dto.BookDTO;
import com.kh.deneb.dto.BookcaseDTO;
import com.kh.deneb.dto.BookmarkDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class LibraryService {

    @Autowired
    AccountDAO accountDAO;
    @Autowired
    BookcaseDAO bookcaseDAO;
    @Autowired
    BookDAO bookDAO;
    @Autowired
    BookmarkDAO bookmarkDAO;

    public int getAccountSeq(String user_email, String encrypted_pw) {
        HashMap<String, Object> account = new HashMap<>();
        account.put("user_email", user_email);
        account.put("encrypted_pw", encrypted_pw);
        return accountDAO.selectSeqByEmailAndPw(account);
    }

    public HashMap<String, Object> getBookcase(int bookcase_seq) throws JsonProcessingException {
        HashMap<String, Object> data = new HashMap<>();
        data.put("bookcase", bookcaseDAO.selectAllBySeq(bookcase_seq));
        data.put("bookList", getBookList(bookcase_seq));
        return data;
    }

    public List<HashMap<String, Object>> getBookcaseList(String user_email, String encrypted_pw) throws JsonProcessingException {
        int account_seq = getAccountSeq(user_email, encrypted_pw);

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> order = mapper.readValue(accountDAO.selectBookcaseOrderBySeq(account_seq), ArrayList.class);

        List<HashMap<String, Object>> bookcaseList = new ArrayList<>();
        for (int bookcase_seq : order) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("bookcase", bookcaseDAO.selectAllBySeq(bookcase_seq));
            data.put("bookList", getBookList(bookcase_seq));
            bookcaseList.add(data);
        }
        return bookcaseList;
    }

    public HashMap<String, Object> getBook(int book_seq) throws JsonProcessingException {
        HashMap<String, Object> data = new HashMap<>();
        data.put("book", bookDAO.selectAllBySeq(book_seq));
        data.put("bookmarkList", getBookmarkList(book_seq));
        return data;
    }

    public List<HashMap<String, Object>> getBookList(int parent_bookcase_seq) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> order = mapper.readValue(bookcaseDAO.selectBookOrderBySeq(parent_bookcase_seq), ArrayList.class);

        List<HashMap<String, Object>> bookList = new ArrayList<>();
        for (int book_seq : order) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("book", bookDAO.selectAllBySeq(book_seq));
            data.put("bookmarkList", getBookmarkList(book_seq));
            bookList.add(data);
        }
        return bookList;
    }

    public BookmarkDTO getBookmark(int bookmark_seq) {
        return bookmarkDAO.selectAllBySeq(bookmark_seq);
    }

    public List<BookmarkDTO> getBookmarkList(int parent_book_seq) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> order = mapper.readValue(bookDAO.selectBookmarkOrderBySeq(parent_book_seq), ArrayList.class);

        List<BookmarkDTO> bookmarkList = new ArrayList<>();
        for (int bookmark_seq : order) {
            bookmarkList.add(bookmarkDAO.selectAllBySeq(bookmark_seq));
        }
        return bookmarkList;
    }

    @Transactional
    public boolean deleteBookcase(int bookcase_seq) throws JsonProcessingException {

        for (int book_seq : bookDAO.selectSeqByParentSeq(bookcase_seq)) {
            for (int bookmark_seq : bookmarkDAO.selectSeqByParentSeq(book_seq)) {
                bookmarkDAO.deleteBySeq(bookmark_seq);
            }
            bookDAO.deleteBySeq(book_seq);
        }

        int account_seq = bookcaseDAO.selectParentBySeq(bookcase_seq);
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> order = mapper.readValue(accountDAO.selectBookcaseOrderBySeq(account_seq), ArrayList.class);
        order.remove(order.indexOf(bookcase_seq));

        AccountDTO account = new AccountDTO();
        account.setAccount_seq(account_seq);
        account.setBookcase_order(String.valueOf(order));

        return bookcaseDAO.deleteBySeq(bookcase_seq) == 1 && accountDAO.updateOrderBySeq(account) == 1;
    }

    @Transactional
    public boolean deleteBook(int book_seq) throws JsonProcessingException {

        for (int bookmark_seq : bookmarkDAO.selectSeqByParentSeq(book_seq)) {
            bookmarkDAO.deleteBySeq(bookmark_seq);
        }

        int bookcase_seq = bookDAO.selectParentBySeq(book_seq);
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> order = mapper.readValue(bookcaseDAO.selectBookOrderBySeq(bookcase_seq), ArrayList.class);
        order.remove(order.indexOf(book_seq));

        BookcaseDTO bookcase = new BookcaseDTO();
        bookcase.setBookcase_seq(bookcase_seq);
        bookcase.setBook_order(String.valueOf(order));


        return bookDAO.deleteBySeq(book_seq) == 1 && bookcaseDAO.updateOrderBySeq(bookcase) == 1;
    }

    @Transactional
    public boolean deleteBookmark(int bookmark_seq) throws JsonProcessingException {

        int book_seq = bookmarkDAO.selectParentBySeq(bookmark_seq);
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> order = mapper.readValue(bookDAO.selectBookmarkOrderBySeq(book_seq), ArrayList.class);
        order.remove(order.indexOf(bookmark_seq));

        BookDTO book = new BookDTO();
        book.setBook_seq(book_seq);
        book.setBookmark_order(String.valueOf(order));

        return bookmarkDAO.deleteBySeq(bookmark_seq) == 1 && bookDAO.updateOrderBySeq(book) == 1;
    }

    public boolean editBookcaseOrder(String user_email, String encrypted_pw, String bookcase_order) throws UnsupportedEncodingException {

        AccountDTO account = new AccountDTO();
        account.setAccount_seq(getAccountSeq(user_email, encrypted_pw));
        account.setBookcase_order("[" + URLDecoder.decode(bookcase_order, "UTF-8") + "]");

        return accountDAO.updateOrderBySeq(account) == 1;
    }

    public boolean editBookOrder(int bookcase_seq, String book_order) throws UnsupportedEncodingException {

        BookcaseDTO bookcase = new BookcaseDTO();
        bookcase.setBookcase_seq(bookcase_seq);
        bookcase.setBook_order("[" + URLDecoder.decode(book_order, "UTF-8") + "]");

        return bookcaseDAO.updateOrderBySeq(bookcase) == 1;
    }

    @Transactional
    public boolean editBookmarkOrder(int book_seq, String bookmark_order) throws UnsupportedEncodingException {

        BookDTO book = new BookDTO();
        book.setBook_seq(book_seq);
        book.setBookmark_order("[" + URLDecoder.decode(bookmark_order, "UTF-8") + "]");
        book.setVideo_index(0);

        bookDAO.updateIndexBySeq(book);
        return bookDAO.updateOrderBySeq(book) == 1;
    }

    @Transactional
    public List<HashMap<String, Object>> moveBook(String user_email, String encrypted_pw, int book_seq, int origin_seq, int destination_seq) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> origin_order = mapper.readValue(bookcaseDAO.selectBookOrderBySeq(origin_seq), ArrayList.class);
        ArrayList<Integer> destination_order = mapper.readValue(bookcaseDAO.selectBookOrderBySeq(destination_seq), ArrayList.class);
        origin_order.remove(origin_order.indexOf(book_seq));
        destination_order.add(book_seq);

        BookcaseDTO bookcase = new BookcaseDTO();
        bookcase.setBookcase_seq(origin_seq);
        bookcase.setBook_order(String.valueOf(origin_order));
        bookcaseDAO.updateOrderBySeq(bookcase);
        bookcase.setBookcase_seq(destination_seq);
        bookcase.setBook_order(String.valueOf(destination_order));
        bookcaseDAO.updateOrderBySeq(bookcase);

        BookDTO book = new BookDTO();
        book.setBook_seq(book_seq);
        book.setParent_bookcase_seq(destination_seq);
        bookDAO.updateParentBySeq(book);

        return getBookcaseList(user_email, encrypted_pw);
    }
}