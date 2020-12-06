
package com.jun.akbonara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    Button button_login;
    TextView button_signup;
    ImageView button_back;
    EditText editText_Email,editText_Password;

    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();


    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  //layout xml 매칭

        button_login =findViewById(R.id.button_login);
        button_signup = findViewById(R.id.button_signup);
        button_back = findViewById(R.id.button_back);
        editText_Email =findViewById(R.id.editText_Email);
        editText_Password = findViewById(R.id.editText_Password);




    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent_requireLogin = getIntent();
        final String WhatActivity = intent_requireLogin.getStringExtra("액티비티");

        //데이터 베이스 불러오기
        SharedPreferences preferences = getSharedPreferences("데이터베이스",0);
        String JsonDATA = preferences.getString("데이터베이스","");

        try {
            DATABASE = new JSONObject(JsonDATA);
            array_client =  DATABASE.getJSONArray("회원정보");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        //회원가입 버튼
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        //돌아가기 버튼
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //로그인 버튼
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editText_Email.getText().toString();
                String password = editText_Password.getText().toString();

                //ID 리스트 만들기
                List<String> List_ID = new ArrayList<>();

                for(int i=0;i<array_client.length();i++){
                    try {
                        JSONObject a =(JSONObject) array_client.get(i);
                        String ID = a.getString("아이디");
                        List_ID.add(ID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //해당 아이디 찾기
                int ID_NUMBER=0;
                for(int i=0;i<array_client.length();i++){
                    try {
                        JSONObject a =(JSONObject) array_client.get(i);
                        if(email.equals(a.getString("아이디"))) {
                            ID_NUMBER = i;
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //해당 아이디의 비밀번호
                String PasswordCheck="";
                try {
                    JSONObject a =(JSONObject) array_client.get(ID_NUMBER);
                    PasswordCheck = a.getString("비밀번호");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //해당 아이디의 닉네임
                String NicknameCheck="";
                try {
                    JSONObject a =(JSONObject) array_client.get(ID_NUMBER);
                    NicknameCheck = a.getString("닉네임");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //로그인 절차
                if (email.equals("") || password.equals("")) {
                        Toast.makeText(LoginActivity.this, "공백란을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!List_ID.contains(email)  || !PasswordCheck.equals(password) ){
                        Toast.makeText(LoginActivity.this, "존재하지 않는 아이디이거나 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        // 로그인 정보 등록
                        try {
                            DATABASE.put("로그인정보",ID_NUMBER);

                            SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("데이터베이스", DATABASE.toString());
                            editor.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(WhatActivity.equals("메인")){
                            Intent intent_login = new Intent(LoginActivity.this, MainActivity.class);
                            intent_login.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            Toast.makeText(LoginActivity.this, NicknameCheck+"님 환영합니다!", Toast.LENGTH_SHORT).show();
                            startActivity(intent_login);
                            finish();
                        }
                        if(WhatActivity.equals("알림")){
                            Intent intent_login = new Intent(LoginActivity.this, AlarmActivity.class);
                            intent_login.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            Toast.makeText(LoginActivity.this, NicknameCheck+"님 환영합니다!", Toast.LENGTH_SHORT).show();
                            startActivity(intent_login);
                            finish();
                        }
                        if(WhatActivity.equals("마이페이지")){
                            Intent intent_login = new Intent(LoginActivity.this, MypageActivity.class);
                            intent_login.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            Toast.makeText(LoginActivity.this, NicknameCheck+"님 환영합니다!", Toast.LENGTH_SHORT).show();
                            startActivity(intent_login);
                            finish();
                        }
                        if(WhatActivity.equals("악보업로드")){
                            Intent intent_login = new Intent(LoginActivity.this, SheetmusicUploadActivity.class);
                            intent_login.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            Toast.makeText(LoginActivity.this, NicknameCheck+"님 환영합니다!", Toast.LENGTH_SHORT).show();
                            startActivity(intent_login);
                            finish();
                        }
                        if(WhatActivity.equals("영상업로드")){
                            Intent intent_login = new Intent(LoginActivity.this, VideoUploadActivity.class);
                            intent_login.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            Toast.makeText(LoginActivity.this, NicknameCheck+"님 환영합니다!", Toast.LENGTH_SHORT).show();
                            startActivity(intent_login);
                            finish();
                        }
                        if(WhatActivity.equals("글업로드")){
                            Intent intent_login = new Intent(LoginActivity.this, CommunityUploadActivity.class);
                            intent_login.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            Toast.makeText(LoginActivity.this, NicknameCheck+"님 환영합니다!", Toast.LENGTH_SHORT).show();
                            startActivity(intent_login);
                            finish();
                        }
                        if(WhatActivity.equals("악보포스트")){
                            Intent intent_login = new Intent(LoginActivity.this, SheetmusicPostActivity.class);
                            intent_login.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            Toast.makeText(LoginActivity.this, NicknameCheck+"님 환영합니다!", Toast.LENGTH_SHORT).show();
                            startActivity(intent_login);
                            finish();
                        }
                        if(WhatActivity.equals("영상포스트")){
                            Intent intent_login = new Intent(LoginActivity.this, VideoPostActivity.class);
                            intent_login.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            Toast.makeText(LoginActivity.this, NicknameCheck+"님 환영합니다!", Toast.LENGTH_SHORT).show();
                            startActivity(intent_login);
                            finish();
                        }
                        if(WhatActivity.equals("커뮤니티포스트")){
                            Intent intent_login = new Intent(LoginActivity.this, CommunityPostActivity.class);
                            intent_login.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            Toast.makeText(LoginActivity.this, NicknameCheck+"님 환영합니다!", Toast.LENGTH_SHORT).show();
                            startActivity(intent_login);
                            finish();
                        }

                    }

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
