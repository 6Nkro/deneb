package com.kh.deneb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.deneb.dao.BookDAO;
import com.kh.deneb.dao.BookmarkDAO;
import com.kh.deneb.dto.BookDTO;
import com.kh.deneb.dto.BookmarkDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BookmarkService {
    @Autowired
    BookmarkDAO bookmarkDAO;

    @Autowired
    BookDAO bookDAO;

    @Autowired
    LibraryService libraryService;

    public BookmarkDTO createBookmark(BookmarkDTO bookmark) throws JsonProcessingException {
        int bookmark_seq = bookmarkDAO.selectNextSeq();
        int parent_book_seq = bookmark.getParent_book_seq();

        bookmark.setBookmark_seq(bookmark_seq);
        if (bookmark.getBookmark_memo().length() == 0) {
            bookmark.setBookmark_memo("-");
        }
        bookmarkDAO.insert(bookmark);

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> order = mapper.readValue(bookDAO.selectBookmarkOrderBySeq(parent_book_seq), ArrayList.class);
        order.add(bookmark_seq);

        BookDTO book = new BookDTO();
        book.setBook_seq(parent_book_seq);
        book.setBookmark_order(String.valueOf(order));
        bookDAO.updateOrderBySeq(book);

        return libraryService.getBookmark(bookmark_seq);
    }

    public BookmarkDTO editBookmark(BookmarkDTO bookmark) {
        if (bookmark.getBookmark_memo().length() == 0) {
            bookmark.setBookmark_memo("-");
        }
        bookmarkDAO.updateBySeq(bookmark);
        return libraryService.getBookmark(bookmark.getBookmark_seq());
    }

    public BookmarkDTO moveBookmark(BookmarkDTO bookmark) {
        bookmarkDAO.updateParentBySeq(bookmark);
        return bookmarkDAO.selectAllBySeq(bookmark.getBookmark_seq());
    }

    public int editVideoTime(BookmarkDTO bookmark) {
        return bookmarkDAO.updateVideoTimeBySeq(bookmark);
    }
}
