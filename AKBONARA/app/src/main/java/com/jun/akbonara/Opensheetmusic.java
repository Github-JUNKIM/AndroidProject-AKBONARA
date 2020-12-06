package com.jun.akbonara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Opensheetmusic extends AppCompatActivity {


    int ID_NUMBER;
    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONArray array_Article_Sheetmusic = new JSONArray();
    //--------------------------------------------------------------------------------------------------

    PDFView pdfview;

    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opensheetmusic);  //layout xml 매칭



        pdfview = findViewById (R.id.pdfview);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //데이터 베이스 불러오기
        SharedPreferences preferences = getSharedPreferences("데이터베이스",0);
        String JsonDATA = preferences.getString("데이터베이스","");
        try {
            DATABASE = new JSONObject(JsonDATA);
            array_client =  DATABASE.getJSONArray("회원정보");
            array_Article_Sheetmusic = DATABASE.getJSONArray("악보글정보");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //로그인 아이디넘버
        try {
            ID_NUMBER = DATABASE.getInt("로그인정보");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        int articlenumber = intent.getIntExtra("아티클넘버",0);
        Log.d("파일게시물넘버", String.valueOf(articlenumber));

        try {
            JSONObject thisarticle = array_Article_Sheetmusic.getJSONObject(articlenumber);
            Log.d("악보주소", thisarticle.getString("악보"));
            pdfview.fromUri(Uri.parse(thisarticle.getString("악보"))).swipeHorizontal(true).load();
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

