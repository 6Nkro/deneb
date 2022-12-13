package com.kh.deneb.commons;

public class CommonConstants {
    public static final String EMAIL_REGEX = "^([\\w-.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$";
    public static final String PW_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$";
    public static final String NAME_REGEX = "^[가-힣a-zA-Z\\d_-]{2,12}$";
}
