package com.kh.deneb.dto;

import lombok.Data;

@Data
public class BookmarkDTO {
    int bookmark_seq;
    int parent_book_seq;
    String bookmark_name;
    String bookmark_url;
    String bookmark_memo;
    String bookmark_icon;
}
