package com.kh.deneb.dto;

import lombok.*;

import java.sql.Timestamp;

@Data
public class AccountDTO {
    private int account_no;
    private String user_email;
    private String user_pw;
    private String user_name;
    private Timestamp signup_date;
    private String storage_order;
}