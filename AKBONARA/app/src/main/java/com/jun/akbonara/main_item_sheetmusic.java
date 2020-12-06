package com.jun.akbonara;

import java.io.Serializable;

public class main_item_sheetmusic implements Serializable { //Serializable은 그냥 string데이터가 아닌 JSONstring데이터여서
    private String 닉네임;
    private String 아이디;
    private String 비밀번호;

    public String get닉네임() {
        return 닉네임;
    }

    public void set닉네임(String 닉네임) {
        this.닉네임 = 닉네임;
    }

    public String get아이디() {
        return 아이디;
    }

    public void set아이디(String 아이디) {
        this.아이디 = 아이디;
    }

    public String get비밀번호() {
        return 비밀번호;
    }

    public void set비밀번호(String 비밀번호) {
        this.비밀번호 = 비밀번호;
    }


}
