package com.kh.deneb.dto;

import lombok.Data;

@Data
public class BookDTO {
    int book_seq;
    int parent_bookcase_seq;
    String book_name;
    String book_color;
    char book_open;
    String bookmark_order;
}
