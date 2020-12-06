package com.jun.akbonara;

import java.io.Serializable;

public class Item_SheetmusicActivity implements Serializable { //Serializable은 그냥 string데이터가 아닌 JSONstring데이터여서
    private String 프로필;
    private String 닉네임;
    private String 글제목;
    private long 업로드시간;
    private int 가격;
    private int 조회수;
    private int 좋아요수;
    private int 댓글수;

    public String get프로필() {
        return 프로필;
    }

    public void set프로필(String 프로필) {
        this.프로필 = 프로필;
    }

    public String get닉네임() {
        return 닉네임;
    }

    public void set닉네임(String 닉네임) {
        this.닉네임 = 닉네임;
    }

    public String get글제목() {
        return 글제목;
    }

    public void set글제목(String 글제목) {
        this.글제목 = 글제목;
    }

    public long get업로드시간() {
        return 업로드시간;
    }

    public void set업로드시간(long 업로드시간) {
        this.업로드시간 = 업로드시간;
    }

    public int get가격() {
        return 가격;
    }

    public void set가격(int 가격) {
        this.가격 = 가격;
    }

    public int get조회수() {
        return 조회수;
    }

    public void set조회수(int 조회수) {
        this.조회수 = 조회수;
    }

    public int get좋아요수() {
        return 좋아요수;
    }

    public void set좋아요수(int 좋아요수) {
        this.좋아요수 = 좋아요수;
    }

    public int get댓글수() {
        return 댓글수;
    }

    public void set댓글수(int 댓글수) {
        this.댓글수 = 댓글수;
    }



}
