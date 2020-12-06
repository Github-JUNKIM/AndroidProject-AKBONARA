package com.jun.akbonara;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.shockwave.pdfium.PdfiumCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PopupActivity extends Activity {


    int ID_NUMBER;
    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONArray array_SheetmusicArticle = new JSONArray();
    //--------------------------------------------------------------------------------------------------

    Button purchase_btn;
    ImageView close_btn;
    TextView article_name,article_price,client_cash,article_price_calculate,cashpriceSum,currenttime;

    //글넘버
    int ARTICLE_NUMBER;
    //잔여캐시 int값
    int int_cashpriceSum;
    //구매성공실패 불린
    boolean successOrfail;

    //현재 글정보 JSONOBJ
    JSONObject ThisArticle;
    //현재 글쓴이
    JSONObject ThisArticleWriter;
    //현재 회원정보 JSONOBJ
    JSONObject CurrentClient;




    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);  //layout xml 매칭

        purchase_btn= findViewById (R.id.purchase_btn);
        article_name= findViewById (R.id.article_name);
        article_price= findViewById (R.id.article_price);
        client_cash= findViewById (R.id.client_cash);
        article_price_calculate= findViewById (R.id.article_price_calculate);
        cashpriceSum= findViewById (R.id.cashpriceSum);
        close_btn = findViewById (R.id.close_btn);
        currenttime = findViewById (R.id.currenttime);

        Intent intent = getIntent();
        ARTICLE_NUMBER = intent.getIntExtra("아티클넘버",-1);

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
            array_SheetmusicArticle = DATABASE.getJSONArray("악보글정보");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //로그인 아이디넘버
        try {
            ID_NUMBER = DATABASE.getInt("로그인정보");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //체크해보자!!
        try {
            JSONObject 현재회원 = array_client.getJSONObject(ID_NUMBER);
            JSONArray checkpurchaseList = 현재회원.getJSONArray("구매한악보");
            Log.d("구매한 악보 리스트", String.valueOf(checkpurchaseList));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        DecimalFormat formatter = new DecimalFormat("###,###");


        //셋팅
        try {
            //현재글 셋팅
            ThisArticle = array_SheetmusicArticle.getJSONObject(ARTICLE_NUMBER);
            //현재글 작성자 셋팅
            ThisArticleWriter = array_client.getJSONObject(ThisArticle.getInt("아이디넘버"));
            //현재회원 셋팅
            CurrentClient = array_client.getJSONObject(ID_NUMBER);

            //현재 시간 셋팅
            long time = System.currentTimeMillis();
            SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String str = dayTime.format(new Date(time));
            currenttime.setText(str);

            //글제목 셋팅
            article_name.setText(ThisArticle.getString("글제목"));
            //악보 가격 셋팅
            if(ThisArticle.getInt("가격")==0){
                article_price.setText("무료");
            }
            else{
                article_price.setText(formatter.format(ThisArticle.getInt("가격"))+"원");
            }
            //회원 캐시 셋팅
            client_cash.setText(formatter.format(CurrentClient.getInt("캐시"))+"원");
            //악보 계산 가격 셋팅
            article_price_calculate.setText("(-) "+formatter.format(ThisArticle.getInt("가격"))+"원");
            //잔여 캐시 계산
            int_cashpriceSum = CurrentClient.getInt("캐시")-ThisArticle.getInt("가격");
            //잔여 캐시 셋팅
            if(int_cashpriceSum<0){
                cashpriceSum.setTextColor(Color.RED);
                cashpriceSum.setText(formatter.format(int_cashpriceSum)+"원");
                successOrfail = false;
            }
            else {
                cashpriceSum.setTextColor(Color.BLUE);
                cashpriceSum.setText(formatter.format(int_cashpriceSum)+"원");
                successOrfail = true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }





        //구매버튼
        purchase_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                if(successOrfail==true){
                    try {
                        //구매자 캐시소비
                        JSONArray purchaseList = CurrentClient.getJSONArray("구매한악보");
                        purchaseList.put(ARTICLE_NUMBER);
                        CurrentClient.put("캐시",int_cashpriceSum);

                        //캐시내역
                        JSONArray CashHistory = CurrentClient.getJSONArray("캐시내역");
                        JSONObject historyObj = new JSONObject();
                        long time = System.currentTimeMillis();
                        historyObj.put("시간",time);
                        historyObj.put("타입","악보 구매");
                        historyObj.put("캐시정보",ThisArticle.getString("글제목"));
                        historyObj.put("금액",ThisArticle.getInt("가격"));
                        CashHistory.put(historyObj);

                        //판매자 캐시누적
                        int ThisArticleWriterCash = ThisArticleWriter.getInt("캐시");
                        int ThisArticlePrice = ThisArticle.getInt("가격");
                        ThisArticleWriter.put("캐시",ThisArticleWriterCash+ThisArticlePrice);

                        //캐시내역
                        JSONArray WriterCashHistory = ThisArticleWriter.getJSONArray("캐시내역");
                        JSONObject WriterhistoryObj = new JSONObject();
                        WriterhistoryObj.put("시간",time);
                        WriterhistoryObj.put("타입","악보 판매");
                        WriterhistoryObj.put("캐시정보",ThisArticle.getString("글제목"));
                        WriterhistoryObj.put("금액",ThisArticle.getInt("가격"));
                        WriterCashHistory.put(WriterhistoryObj);

                        //판매자 알림메세지
                        JSONArray alarmList = ThisArticleWriter.getJSONArray("알림목록");
                        JSONObject alarmmsg = new JSONObject();

                        String message = "님이 회원님의 악보를 구매하셨습니다.";

                        alarmmsg.put("아이디넘버",ID_NUMBER);
                        alarmmsg.put("게시물타입","sheetmusic");
                        alarmmsg.put("게시글넘버",ARTICLE_NUMBER);
                        alarmmsg.put("체크여부",false);
                        alarmmsg.put("알림메세지",message);

                        alarmList.put(alarmmsg);

                        //구매수 추가
                        int purchasetime = ThisArticle.getInt("구매수");
                        ThisArticle.put("구매수",purchasetime+1);


                        SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("데이터베이스", DATABASE.toString());
                        editor.commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Intent intent = new Intent(PopupActivity.this, PopupPurchaseActivity.class);
                intent.putExtra("성공/실패",successOrfail);
                startActivity(intent);
                finish();
            }
        });
        //닫기 버튼
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    // 실제 경로 찾기

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getPath(Uri uri)

    {
        String id = DocumentsContract.getDocumentId(uri).split(":")[1];
        String[] columns = { MediaStore.Files.FileColumns.DATA };
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try { int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex);
            }else{
                return "";
            }
        } finally { cursor.close(); }

    }
}

