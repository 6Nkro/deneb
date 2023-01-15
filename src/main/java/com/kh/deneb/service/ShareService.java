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
    ReplyDAO replyDAO;
    @Autowired
    LibraryService libraryService;

    public HashMap<String, Object> createSharePost(int origin_seq, SubBookcaseDTO subBookcase) throws UnsupportedEncodingException, JsonProcessingException {

        int sub_bookcase_seq = subBookcaseDAO.selectNextSeq();

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> book_order = mapper.readValue(bookcaseDAO.selectBookOrderBySeq(origin_seq), ArrayList.class);
        ArrayList<Integer> sub_book_order = new ArrayList<>();

        List<BookmarkDTO> sub_bookmarkListAll = new ArrayList<>();

        List<BookDTO> sub_bookList;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < book_order.size(); i++) {
            sb.append(",").append(book_order.get(i)).append(",").append(i);
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("parent_bookcase_seq", origin_seq);
        params.put("order", sb);

        sub_bookList = bookDAO.selectAllByParent(params);

        for (BookDTO sub_book : sub_bookList) {
            int book_seq = sub_book.getBook_seq();
            int sub_book_seq = subBookDAO.selectNextSeq();

            ArrayList<Integer> bookmark_order = mapper.readValue(bookDAO.selectBookmarkOrderBySeq(book_seq), ArrayList.class);
            ArrayList<Integer> sub_bookmark_order = new ArrayList<>();

            List<BookmarkDTO> sub_bookmarkList = new ArrayList<>();
            if (bookmark_order.size() > 0) {
                sb.setLength(0);
                for (int i = 0; i < bookmark_order.size(); i++) {
                    sb.append(",").append(bookmark_order.get(i)).append(",").append(i);
                }
                params.clear();
                params.put("parent_book_seq", book_seq);
                params.put("order", sb);

                sub_bookmarkList = bookmarkDAO.selectAllByParent(params);
            }

            for (BookmarkDTO sub_bookmark : sub_bookmarkList) {
                int sub_bookmark_seq = subBookmarkDAO.selectNextSeq();
                sub_bookmark.setBookmark_seq(sub_bookmark_seq);
                sub_bookmark.setParent_book_seq(sub_book_seq);
                sub_bookmark_order.add(sub_bookmark_seq);
                sub_bookmarkListAll.add(sub_bookmark);
            }

            sub_book.setBook_seq(sub_book_seq);
            sub_book.setParent_bookcase_seq(sub_bookcase_seq);
            sub_book.setBookmark_order(sub_bookmark_order.toString());
            sub_book_order.add(sub_book_seq);
        }

        if (sub_bookmarkListAll.size() > 0) {
            sb.setLength(0);
            for (BookmarkDTO sub_bookmark : sub_bookmarkListAll) {
                sb.append("select ")
                        .append(sub_bookmark.getBookmark_seq()).append(",")
                        .append(sub_bookmark.getParent_book_seq()).append(",")
                        .append("'").append(sub_bookmark.getBookmark_name()).append("',")
                        .append("'").append(sub_bookmark.getBookmark_url()).append("',")
                        .append("'").append(sub_bookmark.getBookmark_memo()).append("',")
                        .append("'").append(sub_bookmark.getVideo_id()).append("',")
                        .append("'").append(sub_bookmark.getVideo_title()).append("',")
                        .append("'").append(sub_bookmark.getVideo_channel()).append("' ")
                        .append("from dual union all ");
            }
            sb.setLength((sb.length() - 11));
            subBookmarkDAO.insertAll(sb.toString());
        }

        sb.setLength(0);
        for (BookDTO sub_book : sub_bookList) {
            sb.append("select ")
                    .append(sub_book.getBook_seq()).append(",")
                    .append(sub_book.getParent_bookcase_seq()).append(",")
                    .append("'").append(sub_book.getBook_name()).append("',")
                    .append("'").append(sub_book.getBook_color()).append("',")
                    .append("'").append(sub_book.getBook_icon()).append("',")
                    .append("'").append(sub_book.getBook_icon_color()).append("',")
                    .append("'").append(sub_book.getBook_type()).append("',")
                    .append("'").append(sub_book.getBookmark_order()).append("' ")
                    .append("from dual union all ");
        }
        sb.setLength((sb.length() - 11));
        subBookDAO.insertAll(sb.toString());

        subBookcase.setBookcase_seq(sub_bookcase_seq);
        subBookcase.setBook_order(sub_book_order.toString());
        String share_code = RandomStringUtils.randomAlphanumeric(12);
        while (subBookcaseDAO.selectCountByShareCode(share_code) != 0) {
            share_code = RandomStringUtils.randomAlphanumeric(12);
        }
        subBookcase.setShare_code(share_code);

        subBookcaseDAO.insert(subBookcase);

        return getSubBookcase(sub_bookcase_seq);
    }

    public HashMap<String, Object> getSubBookcase(int bookcase_seq) throws JsonProcessingException {
        HashMap<String, Object> data = new HashMap<>();
        data.put("bookcase", subBookcaseDAO.selectAllBySeq(bookcase_seq));
        data.put("bookList", getSubBookList(bookcase_seq));
        return data;
    }

    public List<HashMap<String, Object>> getSubBookList(int parent_bookcase_seq) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> bookOrder = mapper.readValue(subBookcaseDAO.selectBookOrderBySeq(parent_bookcase_seq), ArrayList.class);

        List<BookDTO> bookList = new ArrayList<>();
        if (bookOrder.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bookOrder.size(); i++) {
                sb.append(",").append(bookOrder.get(i)).append(",").append(i);
            }
            HashMap<String, Object> params = new HashMap<>();
            params.put("parent_bookcase_seq", parent_bookcase_seq);
            params.put("order", sb);

            bookList = subBookDAO.selectAllByParent(params);
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
            params.put("parent_book_seq", bookOrder.toString()
                    .replace("[", "")
                    .replace("]", ""));
            params.put("order", sb);
            allBookmarkList = subBookmarkDAO.selectAllByParent(params);
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

    public HashMap<String, Object> getSharePost(int account_seq, String share_code) throws JsonProcessingException {

        SubBookcaseDTO subBookcase = subBookcaseDAO.selectAllByShareCode(share_code);
        if (subBookcaseDAO.selectAllByShareCode(share_code) == null) {
            return null;
        }

        int bookcase_seq = bookcaseDAO.selectNextSeq();

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> sub_book_order = mapper.readValue(subBookcaseDAO.selectBookOrderBySeq(subBookcase.getBookcase_seq()), ArrayList.class);
        ArrayList<Integer> book_order = new ArrayList<>();

        List<BookmarkDTO> bookmarkListAll = new ArrayList<>();

        List<BookDTO> bookList;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sub_book_order.size(); i++) {
            sb.append(",").append(sub_book_order.get(i)).append(",").append(i);
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("parent_bookcase_seq", subBookcase.getBookcase_seq());
        params.put("order", sb);


        bookList = subBookDAO.selectAllByParent(params);

        for (BookDTO book : bookList) {
            int sub_book_seq = book.getBook_seq();
            int book_seq = bookDAO.selectNextSeq();

            ArrayList<Integer> sub_bookmark_order = mapper.readValue(subBookDAO.selectBookmarkOrderBySeq(sub_book_seq), ArrayList.class);
            ArrayList<Integer> bookmark_order = new ArrayList<>();

            List<BookmarkDTO> bookmarkList = new ArrayList<>();
            if (sub_bookmark_order.size() > 0) {
                sb.setLength(0);
                for (int i = 0; i < sub_bookmark_order.size(); i++) {
                    sb.append(",").append(sub_bookmark_order.get(i)).append(",").append(i);
                }
                params.clear();
                params.put("parent_book_seq", sub_book_seq);
                params.put("order", sb);

                bookmarkList = subBookmarkDAO.selectAllByParent(params);
            }

            for (BookmarkDTO bookmark : bookmarkList) {
                int bookmark_seq = bookmarkDAO.selectNextSeq();
                bookmark.setBookmark_seq(bookmark_seq);
                bookmark.setParent_book_seq(book_seq);
                bookmark_order.add(bookmark_seq);
                bookmarkListAll.add(bookmark);
            }

            book.setBook_seq(book_seq);
            book.setParent_bookcase_seq(bookcase_seq);
            book.setBookmark_order(bookmark_order.toString());
            book_order.add(book_seq);
        }

        if (bookmarkListAll.size() > 0) {
            sb.setLength(0);
            for (BookmarkDTO bookmark : bookmarkListAll) {
                sb.append("select ")
                        .append(bookmark.getBookmark_seq()).append(",")
                        .append(bookmark.getParent_book_seq()).append(",")
                        .append("'").append(bookmark.getBookmark_name()).append("',")
                        .append("'").append(bookmark.getBookmark_url()).append("',")
                        .append("'").append(bookmark.getBookmark_memo()).append("',")
                        .append("'").append(bookmark.getVideo_id()).append("',")
                        .append("'").append(bookmark.getVideo_title()).append("',")
                        .append("'").append(bookmark.getVideo_channel()).append("',")
                        .append("0 ")
                        .append("from dual union all ");
            }
            sb.setLength((sb.length() - 11));
            bookmarkDAO.insertAll(sb.toString());
        }

        sb.setLength(0);
        for (BookDTO book : bookList) {
            sb.append("select ")
                    .append(book.getBook_seq()).append(",")
                    .append(book.getParent_bookcase_seq()).append(",")
                    .append("'").append(book.getBook_name()).append("',")
                    .append("'").append(book.getBook_color()).append("',")
                    .append("'").append(book.getBook_icon()).append("',")
                    .append("'").append(book.getBook_icon_color()).append("',")
                    .append("'").append(book.getBook_type()).append("',")
                    .append("0,")
                    .append("'Y',")
                    .append("'").append(book.getBookmark_order()).append("' ")
                    .append("from dual union all ");
        }
        sb.setLength((sb.length() - 11));
        bookDAO.insertAll(sb.toString());

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

        params.put("minSeq", page * 12 - 11);
        params.put("maxSeq", page * 12);

        postList.put("postList", subBookcaseDAO.selectAllListBySeqRange(params));
        postList.put("count", subBookcaseDAO.selectCountPublic());

        return postList;
    }

    public HashMap<String, Object> getPostSearchList(int page, String search_type, String search_text) {
        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> postList = new HashMap<>();

        switch (search_type) {
            case "내용":
                search_type = "share_contents";
                break;
            case "태그":
                search_type = "share_tag";
                break;
            case "작성자":
                search_type = "user_name";
                break;
        }

        params.put("minSeq", page * 12 - 11);
        params.put("maxSeq", page * 12);
        params.put("search_type", search_type);
        params.put("search_text", search_text);

        postList.put("postList", subBookcaseDAO.selectSearchListBySeqRange(params));
        postList.put("count", subBookcaseDAO.selectSearchCountPublic(params));

        return postList;
    }

    public int getLikeCount(BookcaseDTO bookcase, boolean like_already) {
        return like_already ? likeCountDAO.delete(bookcase) : likeCountDAO.insert(bookcase);
    }

    public HashMap<String, Object> getPostDetail(BookcaseDTO bookcase) throws JsonProcessingException {

        HashMap<String, Object> detail = new HashMap<>();

        detail.put("bookList", getSubBookList(bookcase.getBookcase_seq()));
        detail.put("like_already", likeCountDAO.selectCountBySeq(bookcase) != 0);

        return detail;
    }

    public List<SubBookcaseDTO> getShareList(int account_seq) {
        return subBookcaseDAO.selectAllListByAccountSeq(account_seq);
    }

    public List<SubBookcaseDTO> getChartList() {
        return subBookcaseDAO.selectAllListSortByPopular();
    }

    @Transactional
    public List<SubBookcaseDTO> deleteShare(int account_seq, int bookcase_seq) {

        for (int book_seq : subBookDAO.selectSeqByParentSeq(bookcase_seq)) {
            for (int bookmark_seq : subBookmarkDAO.selectSeqByParentSeq(book_seq)) {
                subBookmarkDAO.deleteBySeq(bookmark_seq);
            }
            subBookDAO.deleteBySeq(book_seq);
        }

        likeCountDAO.deleteBySeq(bookcase_seq);
        shareCountDAO.deleteBySeq(bookcase_seq);
        replyDAO.deleteBySeq(bookcase_seq);
        subBookcaseDAO.deleteBySeq(bookcase_seq);
        return subBookcaseDAO.selectAllListByAccountSeq(account_seq);
    }
}
