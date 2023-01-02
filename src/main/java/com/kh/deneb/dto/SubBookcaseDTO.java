package com.kh.deneb.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SubBookcaseDTO {
    int bookcase_seq;
    String share_code;
    int account_seq;
    String bookcase_name;
    String book_order;
    char share_public;
    String share_contents;
    String share_tag;
    Timestamp share_date;

    String user_name;
    int like_count;
    int share_count;
}
