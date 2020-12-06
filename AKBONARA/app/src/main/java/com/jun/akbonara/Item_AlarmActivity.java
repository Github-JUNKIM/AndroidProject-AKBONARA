package com.jun.akbonara;


import java.io.Serializable;

public class Item_AlarmActivity implements Serializable { //Serializable은 그냥 string데이터가 아닌 JSONstring데이터여서

    private int 아이디넘버;
    private String 게시물타입;
    private int 게시글넘버;
    private boolean 체크여부;
    private String 알림메세지;



    public Item_AlarmActivity(int 아이디넘버,String 게시물타입,int 게시글넘버,boolean 체크여부,String 알림메세지) {
        this.아이디넘버 = 아이디넘버;
        this.게시물타입 = 게시물타입;
        this.게시글넘버 = 게시글넘버;
        this.체크여부 = 체크여부;
        this.알림메세지 = 알림메세지;

    }


    public int get아이디넘버() {
        return 아이디넘버;
    }

    public String get게시물타입() {
        return 게시물타입;
    }

    public int get게시글넘버() {
        return 게시글넘버;
    }

    public boolean get체크여부() {
        return 체크여부;
    }

    public String get알림메세지() {
        return 알림메세지;
    }









}
