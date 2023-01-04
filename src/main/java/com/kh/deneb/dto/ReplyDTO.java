package com.kh.deneb.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ReplyDTO {

    int reply_seq;
    int parent_bookcase_seq;
    int account_seq;
    String reply_contents;
    Timestamp write_date;

    String user_name;
}
