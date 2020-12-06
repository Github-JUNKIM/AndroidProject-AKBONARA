package com.jun.akbonara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SignupActivity extends AppCompatActivity {


    ImageView button_back;
    EditText editText_Email , editText_Password , editText_ConfirmPassword, editText_Nickname;
    Button button_register,button_confirmID,button_confirmNickname;
    Boolean CheckID=false;
    Boolean CheckNickname=false;


    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();

    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);  //layout xml 매칭

        button_back = findViewById(R.id.button_back);
        editText_Email = findViewById(R.id.editText_Email);
        editText_Password = findViewById(R.id.editText_Password);
        editText_ConfirmPassword = findViewById(R.id.editText_ConfirmPassword);
        editText_Nickname = findViewById(R.id.editText_Nickname);
        button_register = findViewById(R.id.button_register);
        button_confirmID = findViewById(R.id.button_confirmID);
        button_confirmNickname = findViewById(R.id.button_confirmNickname);




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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array_client =  DATABASE.getJSONArray("회원정보");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //돌아가기 버튼
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //아이디 중복확인
        button_confirmID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String InputID = editText_Email.getText().toString();

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

                if(List_ID.contains(InputID)){
                    Toast.makeText(SignupActivity.this, "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                    CheckID =false;
                    button_confirmID.setBackgroundResource(R.drawable.btn_grey);
                }
                else{
                    Toast.makeText(SignupActivity.this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                    CheckID =true;
                    button_confirmID.setBackgroundResource(R.drawable.checkcomplete);
                }
                Log.i("체크",InputID+3);
            }
        });

        //닉네임 중복확인
        button_confirmNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String InputNickname = editText_Nickname.getText().toString();

                //닉네임 리스트 만들기
                List<String> List_Nickname = new ArrayList<>();

                for(int i=0;i<array_client.length();i++){
                    try {
                        JSONObject a =(JSONObject) array_client.get(i);
                        String Nickname = a.getString("닉네임");
                        List_Nickname.add(Nickname);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(List_Nickname.contains(InputNickname)){
                    Toast.makeText(SignupActivity.this, "이미 사용중인 닉네임입니다.", Toast.LENGTH_SHORT).show();
                    CheckNickname =false;
                    button_confirmNickname.setBackgroundResource(R.drawable.btn_grey);
                }
                else{
                    Toast.makeText(SignupActivity.this, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();
                    CheckNickname =true;
                    button_confirmNickname.setBackgroundResource(R.drawable.checkcomplete);

                }
            }
        });

        //가입 버튼
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String InputID = editText_Email.getText().toString();
                String InputPassword = editText_Password.getText().toString();
                String InputConfirmPassword = editText_ConfirmPassword.getText().toString();
                String InputNickname = editText_Nickname.getText().toString();

                if (InputID.equals("") || InputPassword.equals("") || InputConfirmPassword.equals("") || InputNickname.equals("")) {
                    Toast.makeText(SignupActivity.this, "공백란을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(!InputPassword.equals(InputConfirmPassword)){
                    Toast.makeText(SignupActivity.this, "비밀번호가 서로 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(CheckID==false || CheckNickname==false){
                    Toast.makeText(SignupActivity.this, "아이디나 닉네임 중복 확인을 해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {

                    try {
                        JSONArray newjsonArray = new JSONArray();
                        JSONObject client = new JSONObject();
                        client.put("닉네임", editText_Nickname.getText().toString());
                        client.put("아이디", editText_Email.getText().toString());
                        client.put("비밀번호", editText_Password.getText().toString());
                        client.put("프로필사진","");
                        client.put("좋아요목록",newjsonArray);
                        client.put("영상좋아요목록",newjsonArray);
                        client.put("글좋아요목록",newjsonArray);
                        client.put("알림목록",newjsonArray);
                        client.put("캐시",0);
                        client.put("구매한악보",newjsonArray);
                        client.put("캐시내역",newjsonArray);

                        array_client.put(client);
                        DATABASE.put("회원정보", array_client);

                        SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("데이터베이스", DATABASE.toString());
                        editor.commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(SignupActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
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
