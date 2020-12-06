package com.jun.akbonara;

import org.json.JSONArray;

import java.io.Serializable;

public class Item_VideoActivity implements Serializable { //Serializable은 그냥 string데이터가 아닌 JSONstring데이터여서
    private String 글제목;
    private int 아이디넘버;
    private String 동영상주소;
    private String 글;
    private long 업로드시간;
    private JSONArray 조회한사람;
    private JSONArray 좋아요한사람;
    private JSONArray 댓글목록;


    public Item_VideoActivity(String 글제목,int 아이디넘버,String 동영상주소,String 글,long 업로드시간,JSONArray 조회한사람,JSONArray 좋아요한사람,JSONArray 댓글목록) {
        this.글제목 = 글제목;
        this.아이디넘버 = 아이디넘버;
        this.동영상주소 = 동영상주소;
        this.글 = 글;
        this.업로드시간 = 업로드시간;
        this.조회한사람 = 조회한사람;
        this.좋아요한사람 = 좋아요한사람;
        this.댓글목록 = 댓글목록;


    }

    public String get글제목() {
        return 글제목;
    }

    public int get아이디넘버() {
        return 아이디넘버;
    }

    public String get동영상주소() {
        return 동영상주소;
    }

    public String get글() {
        return 글;
    }

    public long get업로드시간() {
        return 업로드시간;
    }

    public JSONArray get조회한사람() {
        return 조회한사람;
    }

    public JSONArray get좋아요한사람() {
        return 좋아요한사람;
    }

    public JSONArray get댓글목록() {
        return 댓글목록;
    }






}
