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

    public HashMap<String, Object> getBookcase(int bookcase_seq) throws JsonProcessingException {
        HashMap<String, Object> data = new HashMap<>();
        data.put("bookcase", bookcaseDAO.selectAllBySeq(bookcase_seq));
        data.put("bookList", getBookList(bookcase_seq));
        return data;
    }

    public List<HashMap<String, Object>> getBookcaseList(int account_seq) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> bookcase_order = mapper.readValue(accountDAO.selectBookcaseOrderBySeq(account_seq), ArrayList.class);


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bookcase_order.size(); i++) {
            sb.append(",").append(bookcase_order.get(i)).append(",").append(i);
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("account_seq", account_seq);
        params.put("order", sb);

        List<BookcaseDTO> bookcaseList = bookcaseDAO.selectAllByParent(params);

        sb.setLength(0);
        int order = 0;
        for (BookcaseDTO bookcase : bookcaseList) {
            ArrayList<Integer> book_order = mapper.readValue(bookcase.getBook_order(), ArrayList.class);
            if (book_order.size() > 0) {
                for (int book_seq : book_order) {
                    sb.append(",").append(book_seq).append(",").append(order);
                    order++;
                }
            }
        }

        List<BookDTO> allBookList = new ArrayList<>();
        if (sb.length() > 0) {
            params.clear();
            params.put("parent_bookcase_seq", bookcase_order.toString()
                    .replace("[", "")
                    .replace("]", ""));
            params.put("order", sb);
            allBookList = bookDAO.selectAllByParent(params);
        }

        sb.setLength(0);
        order = 0;
        for (BookDTO book : allBookList) {
            ArrayList<Integer> bookmark_order = mapper.readValue(book.getBookmark_order(), ArrayList.class);
            if (bookmark_order.size() > 0) {
                for (int bookmark_seq : bookmark_order) {
                    sb.append(",").append(bookmark_seq).append(",").append(order);
                    order++;
                }
            }
        }

        List<BookmarkDTO> allBookmarkList = new ArrayList<>();
        if (sb.length() > 0) {
            StringBuilder book_order = new StringBuilder();
            for (BookcaseDTO bookcase : bookcaseList) {
                if (!bookcase.getBook_order().equals("[]")) {
                    book_order.append(bookcase.getBook_order()
                            .replace("[", "")
                            .replace("]", "")).append(",");
                }
            }
            if (book_order.charAt(book_order.length() - 1) == ',') {
                book_order.setLength(book_order.length() - 1);
            }
            params.clear();
            params.put("parent_book_seq", book_order);
            params.put("order", sb);
            allBookmarkList = bookmarkDAO.selectAllByParent(params);
        }

        List<HashMap<String, Object>> bookcaseMapList = new ArrayList<>();
        int bookIndex = 0;
        int bookmarkIndex = 0;
        for (BookcaseDTO bookcase : bookcaseList) {
            HashMap<String, Object> bookcaseMap = new HashMap<>();
            List<HashMap<String, Object>> bookMapList = new ArrayList<>();
            for (int j = bookIndex; j < allBookList.size(); j++) {
                if (bookcase.getBookcase_seq() != allBookList.get(j).getParent_bookcase_seq()) {
                    break;
                }
                HashMap<String, Object> bookMap = new HashMap<>();
                List<BookmarkDTO> bookmarkList = new ArrayList<>();
                for (int i = bookmarkIndex; i < allBookmarkList.size(); i++) {
                    if (allBookList.get(j).getBook_seq() == allBookmarkList.get(i).getParent_book_seq()) {
                        bookmarkList.add(allBookmarkList.get(i));
                    } else {
                        break;
                    }
                    bookmarkIndex++;
                }
                bookMap.put("book", allBookList.get(j));
                bookMap.put("bookmarkList", bookmarkList);
                bookMapList.add(bookMap);
                bookIndex++;
            }
            bookcaseMap.put("bookcase", bookcase);
            bookcaseMap.put("bookList", bookMapList);
            bookcaseMapList.add(bookcaseMap);
        }
        return bookcaseMapList;
    }

    public HashMap<String, Object> getBook(int book_seq) throws JsonProcessingException {
        HashMap<String, Object> data = new HashMap<>();
        data.put("book", bookDAO.selectAllBySeq(book_seq));
        data.put("bookmarkList", getBookmarkList(book_seq));
        return data;
    }

    public List<HashMap<String, Object>> getBookList(int parent_bookcase_seq) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> book_order = mapper.readValue(bookcaseDAO.selectBookOrderBySeq(parent_bookcase_seq), ArrayList.class);

        List<BookDTO> bookList = new ArrayList<>();
        if (book_order.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < book_order.size(); i++) {
                sb.append(",").append(book_order.get(i)).append(",").append(i);
            }
            HashMap<String, Object> params = new HashMap<>();
            params.put("parent_bookcase_seq", parent_bookcase_seq);
            params.put("order", sb);

            bookList = bookDAO.selectAllByParent(params);
        }

        StringBuilder sb = new StringBuilder();
        for (BookDTO book : bookList) {
            ArrayList<Integer> bookmarkOrder = mapper.readValue(book.getBookmark_order(), ArrayList.class);
            if (bookmarkOrder.size() > 0) {
                for (int i = 0; i < bookmarkOrder.size(); i++) {
                    sb.append(",").append(bookmarkOrder.get(i)).append(",").append(i);
                }
            }
        }

        List<BookmarkDTO> allBookmarkList = new ArrayList<>();
        if (sb.length() > 0) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("parent_book_seq", book_order.toString()
                    .replace("[", "")
                    .replace("]", ""));
            params.put("order", sb);
            allBookmarkList = bookmarkDAO.selectAllByParent(params);
        }

        List<HashMap<String, Object>> bookMapList = new ArrayList<>();
        int bookmarkIndex = 0;
        for (BookDTO book : bookList) {
            HashMap<String, Object> data = new HashMap<>();
            List<BookmarkDTO> bookmarkList = new ArrayList<>();
            for (int i = bookmarkIndex; i < allBookmarkList.size(); i++) {
                if (book.getBook_seq() == allBookmarkList.get(i).getParent_book_seq()) {
                    bookmarkList.add(allBookmarkList.get(i));
                } else {
                    break;
                }
                bookmarkIndex++;
            }
            data.put("book", book);
            data.put("bookmarkList", bookmarkList);
            bookMapList.add(data);
        }
        return bookMapList;
    }

    public BookmarkDTO getBookmark(int bookmark_seq) {
        return bookmarkDAO.selectAllBySeq(bookmark_seq);
    }

    public List<BookmarkDTO> getBookmarkList(int parent_book_seq) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> bookmark_order = mapper.readValue(bookDAO.selectBookmarkOrderBySeq(parent_book_seq), ArrayList.class);

        List<BookmarkDTO> bookmarkList = new ArrayList<>();
        if (bookmark_order.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bookmark_order.size(); i++) {
                sb.append(",").append(bookmark_order.get(i)).append(",").append(i);
            }
            HashMap<String, Object> params = new HashMap<>();
            params.put("parent_book_seq", parent_book_seq);
            params.put("order", sb);

            bookmarkList = bookmarkDAO.selectAllByParent(params);
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

    public boolean editBookcaseOrder(AccountDTO account) {
        return accountDAO.updateOrderBySeq(account) == 1;
    }

    public boolean editBookOrder(BookcaseDTO bookcase) {
        return bookcaseDAO.updateOrderBySeq(bookcase) == 1;
    }

    @Transactional
    public boolean editBookmarkOrder(BookDTO book) {
        book.setVideo_index(0);
        bookDAO.updateIndexBySeq(book);
        return bookDAO.updateOrderBySeq(book) == 1;
    }

    @Transactional
    public List<HashMap<String, Object>> moveBook(int account_seq, int book_seq, int origin_seq, int destination_seq) throws JsonProcessingException {

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

        return getBookcaseList(account_seq);
    }
}