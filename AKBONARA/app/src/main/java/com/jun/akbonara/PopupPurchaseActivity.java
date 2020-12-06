package com.jun.akbonara;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PopupPurchaseActivity extends Activity {


    int ID_NUMBER;
    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    //--------------------------------------------------------------------------------------------------


    //구매성공실패 불린
    boolean successOrfail;

    TextView  ment, ment_movepage,cancel_btn,move_btn;



    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_purchase);  //layout xml 매칭

        ment = findViewById (R.id.ment);
        ment_movepage = findViewById (R.id.ment_movepage);
        cancel_btn = findViewById (R.id.cancel_btn);
        move_btn= findViewById (R.id.move_btn);

        Intent intent = getIntent();
        successOrfail = intent.getBooleanExtra("성공/실패",false);
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

        //캐시부족일때 멘트 셋팅
        if(successOrfail==false){
            ment.setText("캐시가 부족합니다.");
            ment_movepage.setText("충전페이지로 이동하시겠습니까?");
        }

        //취소버튼
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //이동버튼
        move_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(successOrfail==true){
                    Intent intent = new Intent(PopupPurchaseActivity.this,MypageActivity_purchase.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(PopupPurchaseActivity.this,ChargeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                }
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


}

