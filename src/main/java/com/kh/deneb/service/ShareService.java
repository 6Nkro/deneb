package com.kh.deneb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.deneb.dao.*;
import com.kh.deneb.dto.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ShareService {

    @Autowired
    AccountDAO accountDAO;
    @Autowired
    BookcaseDAO bookcaseDAO;
    @Autowired
    BookDAO bookDAO;
    @Autowired
    BookmarkDAO bookmarkDAO;
    @Autowired
    SubBookcaseDAO subBookcaseDAO;
    @Autowired
    SubBookDAO subBookDAO;
    @Autowired
    SubBookmarkDAO subBookmarkDAO;
    @Autowired
    LikeCountDAO likeCountDAO;
    @Autowired
    ShareCountDAO shareCountDAO;

    @Autowired
    LibraryService libraryService;

    public int getAccountSeq(String user_email, String encrypted_pw) {
        HashMap<String, Object> account = new HashMap<>();
        account.put("user_email", user_email);
        account.put("encrypted_pw", encrypted_pw);
        return accountDAO.selectSeqByEmailAndPw(account);
    }

    @Transactional
    public HashMap<String, Object> createSharePost(int origin_seq, SubBookcaseDTO subBookcase) throws UnsupportedEncodingException, JsonProcessingException {

        int sub_bookcase_seq = subBookcaseDAO.selectNextSeq();

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> book_order = mapper.readValue(bookcaseDAO.selectBookOrderBySeq(origin_seq), ArrayList.class);
        ArrayList<Integer> sub_book_order = new ArrayList<>();

        for (int book_seq : book_order) {
            BookDTO subBook = bookDAO.selectAllBySeq(book_seq);

            ArrayList<Integer> bookmark_order = mapper.readValue(bookDAO.selectBookmarkOrderBySeq(book_seq), ArrayList.class);
            ArrayList<Integer> sub_bookmark_order = new ArrayList<>();
            int sub_book_seq = subBookDAO.selectNextSeq();

            for (int bookmark_seq : bookmark_order) {
                BookmarkDTO subBookmark = bookmarkDAO.selectAllBySeq(bookmark_seq);

                int sub_bookmark_seq = subBookmarkDAO.selectNextSeq();

                subBookmark.setBookmark_seq(sub_bookmark_seq);
                subBookmark.setParent_book_seq(sub_book_seq);
                subBookmarkDAO.insert(subBookmark);
                sub_bookmark_order.add(sub_bookmark_seq);
            }

            subBook.setBook_seq(sub_book_seq);
            subBook.setParent_bookcase_seq(sub_bookcase_seq);
            subBook.setBookmark_order(sub_bookmark_order.toString());
            subBookDAO.insert(subBook);
            sub_book_order.add(sub_book_seq);
        }

        subBookcase.setBookcase_seq(sub_bookcase_seq);
        subBookcase.setBook_order(sub_book_order.toString());
        String share_code = RandomStringUtils.randomAlphanumeric(12);
        while (subBookcaseDAO.selectCountByShareCode(share_code) != 0) {
            share_code = RandomStringUtils.randomAlphanumeric(12);
        }
        subBookcase.setShare_code(share_code);
        subBookcase.setShare_tag(URLDecoder.decode(subBookcase.getShare_tag(), "UTF-8"));

        subBookcaseDAO.insert(subBookcase);

        return getSubBookcase(sub_bookcase_seq);
    }

    public HashMap<String, Object> getSubBookcase(int bookcase_seq) throws JsonProcessingException {
        HashMap<String, Object> data = new HashMap<>();
        data.put("bookcase", subBookcaseDAO.selectAllBySeq(bookcase_seq));
        data.put("bookList", getSubBookList(bookcase_seq));
        return data;
    }

    public List<HashMap<String, Object>> getSubBookcaseList(String user_email, String encrypted_pw) throws JsonProcessingException {
        int account_seq = getAccountSeq(user_email, encrypted_pw);

        ArrayList<Integer> order = subBookcaseDAO.selectSeqByParent(account_seq);

        List<HashMap<String, Object>> bookcaseList = new ArrayList<>();
        for (int bookcase_seq : order) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("bookcase", subBookcaseDAO.selectAllBySeq(bookcase_seq));
            data.put("bookList", getSubBookList(bookcase_seq));
            bookcaseList.add(data);
        }
        return bookcaseList;
    }

    public List<HashMap<String, Object>> getSubBookList(int parent_bookcase_seq) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> order = mapper.readValue(subBookcaseDAO.selectBookOrderBySeq(parent_bookcase_seq), ArrayList.class);

        List<HashMap<String, Object>> bookList = new ArrayList<>();
        for (int book_seq : order) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("book", subBookDAO.selectAllBySeq(book_seq));
            data.put("bookmarkList", getSubBookmarkList(book_seq));
            bookList.add(data);
        }
        return bookList;
    }

    public List<BookmarkDTO> getSubBookmarkList(int parent_book_seq) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> order = mapper.readValue(subBookDAO.selectBookmarkOrderBySeq(parent_book_seq), ArrayList.class);

        List<BookmarkDTO> bookmarkList = new ArrayList<>();
        for (int bookmark_seq : order) {
            bookmarkList.add(subBookmarkDAO.selectAllBySeq(bookmark_seq));
        }
        return bookmarkList;
    }

    @Transactional
    public HashMap<String, Object> getSharePost(String user_email, String user_pw, String share_code) throws JsonProcessingException {

        SubBookcaseDTO subBookcase = subBookcaseDAO.selectAllByShareCode(share_code);
        if (subBookcaseDAO.selectAllByShareCode(share_code) == null) {
            return null;
        }

        int bookcase_seq = bookcaseDAO.selectNextSeq();

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> sub_book_order = mapper.readValue(subBookcase.getBook_order(), ArrayList.class);
        System.out.println(sub_book_order);
        ArrayList<Integer> book_order = new ArrayList<>();

        for (int sub_book_seq : sub_book_order) {
            BookDTO Book = subBookDAO.selectAllBySeq(sub_book_seq);

            ArrayList<Integer> sub_bookmark_order = mapper.readValue(subBookDAO.selectBookmarkOrderBySeq(sub_book_seq), ArrayList.class);
            ArrayList<Integer> bookmark_order = new ArrayList<>();
            int book_seq = bookDAO.selectNextSeq();

            for (int sub_bookmark_seq : sub_bookmark_order) {
                BookmarkDTO Bookmark = subBookmarkDAO.selectAllBySeq(sub_bookmark_seq);

                int bookmark_seq = bookmarkDAO.selectNextSeq();

                Bookmark.setBookmark_seq(bookmark_seq);
                Bookmark.setParent_book_seq(book_seq);
                bookmarkDAO.insert(Bookmark);
                bookmark_order.add(bookmark_seq);
            }

            Book.setBook_seq(book_seq);
            Book.setParent_bookcase_seq(bookcase_seq);
            Book.setBookmark_order(bookmark_order.toString());
            bookDAO.insertClone(Book);
            book_order.add(book_seq);
        }

        int account_seq = getAccountSeq(user_email, user_pw);
        BookcaseDTO bookcase = new BookcaseDTO();
        bookcase.setBookcase_seq(bookcase_seq);
        bookcase.setAccount_seq(account_seq);
        bookcase.setBookcase_name(subBookcase.getBookcase_name());
        bookcase.setBook_order(book_order.toString());

        bookcaseDAO.insertClone(bookcase);

        ArrayList<Integer> bookcase_order = mapper.readValue(accountDAO.selectBookcaseOrderBySeq(account_seq), ArrayList.class);
        bookcase_order.add(bookcase_seq);

        AccountDTO account = new AccountDTO();
        account.setAccount_seq(account_seq);
        account.setBookcase_order(String.valueOf(bookcase_order));
        accountDAO.updateOrderBySeq(account);

        bookcase.setBookcase_seq(subBookcase.getBookcase_seq());
        int share_count = shareCountDAO.selectCountBySeq(bookcase);
        if (share_count == 0) {
            shareCountDAO.insert(bookcase);
        }

        HashMap<String, Object> data = libraryService.getBookcase(bookcase_seq);
        data.put("share_count", share_count);
        return data;
    }

    public HashMap<String, Object> getPostList(int page) {
        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> postList = new HashMap<>();

        params.put("minSeq", page * 10 - 9);
        params.put("maxSeq", page * 10);

        postList.put("postList", subBookcaseDAO.selectAllListBySeqRange(params));
        postList.put("count", subBookcaseDAO.selectCountPublic());

        return postList;
    }

    public int getLikeCount(String user_email, String user_pw, int bookcase_seq, boolean like_already) {

        BookcaseDTO bookcase = new BookcaseDTO();

        bookcase.setAccount_seq(getAccountSeq(user_email, user_pw));
        bookcase.setBookcase_seq(bookcase_seq);

        int result = like_already ? likeCountDAO.delete(bookcase) : likeCountDAO.insert(bookcase);

        return result;
    }

    public HashMap<String, Object> getPostDetail(String user_email, String user_pw, int bookcase_seq) throws JsonProcessingException {

        HashMap<String, Object> detail = new HashMap<>();

        detail.put("bookList", getSubBookList(bookcase_seq));
        BookcaseDTO bookcase = new BookcaseDTO();
        bookcase.setAccount_seq(getAccountSeq(user_email, user_pw));
        bookcase.setBookcase_seq(bookcase_seq);
        detail.put("like_already", likeCountDAO.selectCountBySeq(bookcase) != 0);

        return detail;
    }
}
