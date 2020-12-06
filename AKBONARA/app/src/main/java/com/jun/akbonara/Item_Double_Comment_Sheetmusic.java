package com.jun.akbonara;

import org.json.JSONArray;

import java.io.Serializable;

public class Item_Double_Comment_Sheetmusic implements Serializable { //Serializable은 그냥 string데이터가 아닌 JSONstring데이터여서
    private int 아이디넘버;
    private long 작성시간;
    private String 댓글내용;
    private JSONArray 좋아요목록;
    private int 태그아이디넘버;

    public Item_Double_Comment_Sheetmusic(int 아이디넘버, String 댓글내용, JSONArray 좋아요목록, long 작성시간, int 태그아이디넘버) {
        this.아이디넘버 = 아이디넘버;
        this.댓글내용 = 댓글내용;
        this.좋아요목록 = 좋아요목록;
        this.작성시간 = 작성시간;
        this.태그아이디넘버 = 태그아이디넘버;
    }

    public int get아이디넘버() {
        return 아이디넘버;
    }

    public long get작성시간() {
        return 작성시간;
    }

    public String get댓글내용() {
        return 댓글내용;
    }

    public JSONArray get좋아요목록() {
        return 좋아요목록;
    }

    public int get태그아이디넘버() {
        return 태그아이디넘버;
    }








}
