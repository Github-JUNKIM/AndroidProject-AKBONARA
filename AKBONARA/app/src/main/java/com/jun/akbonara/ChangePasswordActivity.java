package com.jun.akbonara;

import android.widget.Toast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {

    //기본 메뉴 멤버 변수 선언
    ImageView button_mypage, button_alarm, icon_music ,icon_video ,icon_community;
    TextView 악보나라;
    int ID_NUMBER;
    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONObject CurrentClinet = new JSONObject();
    //--------------------------------------------------------------------------------------------------

    TextInputLayout TextInputLayout_password,TextInputLayout_passwordcheck;
    EditText editText_Password,editText_Password_check;
    Button change_btn;

    boolean checkboolean = false;
    boolean equalboolean = false;
    boolean blankboolean = false;


    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);  //layout xml 매칭

        //기본 메뉴 주소 매칭
        button_mypage = findViewById(R.id.button_mypage);
        button_alarm =findViewById(R.id.button_alarm);
        악보나라 = findViewById(R.id.악보나라);
        icon_music = findViewById(R.id.icon_music);
        icon_video = findViewById(R.id.icon_video);
        icon_community = findViewById(R.id.icon_community);
        //--------------------------------------------------------------------------------------------------

        TextInputLayout_password = findViewById (R.id.TextInputLayout_password);
        TextInputLayout_passwordcheck = findViewById (R.id.TextInputLayout_passwordcheck);
        editText_Password = findViewById (R.id.editText_Password);
        editText_Password_check = findViewById (R.id.editText_Password_check);
        change_btn = findViewById (R.id.change_btn);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final String reqpassword = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";

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
            //현재회원
            CurrentClinet = array_client.optJSONObject(ID_NUMBER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //로그인 안되었을때 로그인 창으로 이동
        final Intent intent_requireLogin = new Intent(ChangePasswordActivity.this,LoginActivity.class);

        //기본 메뉴 버튼 클릭이벤트
        // 홈 버튼
        악보나라.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(ChangePasswordActivity.this,MainActivity.class);
                intent_main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_main);
            }
        });
        // 마이페이지 버튼
        button_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_mypage = new Intent(ChangePasswordActivity.this, MypageActivity.class);
                    intent_mypage.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent_mypage);
                }
                else{
                    intent_requireLogin.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent_requireLogin.putExtra("액티비티","마이페이지");
                    startActivity(intent_requireLogin);
                }
            }
        });
        //알림 버튼
        button_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_alarm = new Intent(ChangePasswordActivity.this, AlarmActivity.class);
                    intent_alarm.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent_alarm);
                }
                else{
                    intent_requireLogin.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent_requireLogin.putExtra("액티비티","알림");
                    startActivity(intent_requireLogin);
                }
            }
        });
        // 악보 커뮤니티 버튼
        icon_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_sheetmusic = new Intent(ChangePasswordActivity.this,SheetmusicActivity.class);
                intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_sheetmusic);
            }
        });
        // 영상 커뮤니티 버튼
        icon_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_video = new Intent(ChangePasswordActivity.this,VideoActivity.class);
                intent_video.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_video);
            }
        });
        // 글 커뮤니티 버튼
        icon_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_community = new Intent(ChangePasswordActivity.this,CommunityActivity.class);
                intent_community.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_community);
            }
        });
        //--------------------------------------------------------------------------------------------------

        TextInputLayout_password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (editText_Password.getText().toString().matches(reqpassword)==false) {
                    TextInputLayout_password.setError("8자이상 대소문자 포함");
                    TextInputLayout_password.setErrorEnabled(true);
                    checkboolean = false;
                } else {
                    TextInputLayout_password.setErrorEnabled(false);
                    checkboolean= true;
                }
                if (editText_Password_check.length()!=0 && !editText_Password_check.getText().toString().equals(editText_Password.getText().toString())) {
                    Log.d("새 비밀번호", editText_Password_check.getText().toString());
                    Log.d("새 비밀번호 확인", editText_Password.getText().toString());
                    TextInputLayout_passwordcheck.setError("비밀번호가 일치하지 않습니다.");
                    TextInputLayout_passwordcheck.setErrorEnabled(true);
                    equalboolean = false;
                } else {
                    TextInputLayout_passwordcheck.setErrorEnabled(false);
                    equalboolean = true;
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        TextInputLayout_passwordcheck.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (!editText_Password_check.getText().toString().equals(editText_Password.getText().toString())) {
                    Log.d("새 비밀번호", editText_Password_check.getText().toString());
                    Log.d("새 비밀번호 확인", editText_Password.getText().toString());
                    TextInputLayout_passwordcheck.setError("비밀번호가 일치하지 않습니다.");
                    TextInputLayout_passwordcheck.setErrorEnabled(true);
                    equalboolean = false;
                } else {
                    TextInputLayout_passwordcheck.setErrorEnabled(false);
                    equalboolean = true;
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_Password.equals("") || editText_Password_check.equals("") || editText_Password.length()==0 || editText_Password_check.length()==0  ){
                    blankboolean=false;
                } else{
                    blankboolean = true;
                }

                if(blankboolean==false){
                    Toast.makeText(ChangePasswordActivity.this, "공백을 모두 입력해주세요", Toast.LENGTH_SHORT).show(); //(context, message, floating time)
                }
                else{
                    if(checkboolean==false || equalboolean==false){
                        Toast.makeText(ChangePasswordActivity.this, "형식에 맞지 않거나 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show(); //(context, message, floating time)
                    }
                    else{
                        try {
                            CurrentClinet.put("비밀번호",editText_Password.getText().toString());
                            DATABASE.put("로그인정보",-1);
                            SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("데이터베이스", DATABASE.toString());
                            editor.commit();

                            Log.d("바뀐 비밀번호", CurrentClinet.getString("비밀번호"));

                            Toast.makeText(ChangePasswordActivity.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show(); //(context, message, floating time)

                            Intent intent = new Intent(ChangePasswordActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
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
