package com.jun.akbonara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MypageSettingActivity extends AppCompatActivity {

    //기본 메뉴 멤버 변수 선언
    ImageView button_mypage, button_alarm, icon_music ,icon_video ,icon_community;
    TextView 악보나라;
    //--------------------------------------------------------------------------

    TextView ChangeProfile,ChangePassword,Logout;

    JSONObject DATABASE;
    JSONArray array_client;
    int ID_NUMBER;


    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_setting);  //layout xml 매칭

        //기본 메뉴 주소 매칭
        button_mypage = findViewById(R.id.button_mypage);
        button_alarm =findViewById(R.id.button_alarm);
        악보나라 = findViewById(R.id.악보나라);
        icon_music = findViewById(R.id.icon_music);
        icon_video = findViewById(R.id.icon_video);
        icon_community = findViewById(R.id.icon_community);
        //--------------------------------------------------------------------------

        ChangeProfile = findViewById(R.id.ChangeProfile);
        ChangePassword = findViewById(R.id.ChangePassword);
        Logout = findViewById(R.id.Logout);

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

        //알림 빨간 표시
        try {
            if(ID_NUMBER!=-1){
                JSONObject currenclient = array_client.getJSONObject(ID_NUMBER);
                JSONArray haha = currenclient.getJSONArray("알림목록");
                if(haha.length()!=0) {
                    for (int i = 0; i < haha.length(); i++) {
                        JSONObject holy = (JSONObject) haha.get(i);
                        boolean alarmcheck = holy.getBoolean("체크여부");
                        if (alarmcheck == false) {
                            button_alarm.setImageResource(R.drawable.icon_alarm_new);
                            break;
                        }
                        if (i == haha.length() - 1) {
                            button_alarm.setImageResource(R.drawable.icon_alarm);
                        }
                    }
                }
                else{
                    button_alarm.setImageResource(R.drawable.icon_alarm);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //로그인 안되었을때 로그인 창으로 이동
        final Intent intent_requireLogin = new Intent(MypageSettingActivity.this,LoginActivity.class);

        //기본 메뉴 버튼 클릭이벤트
        // 홈 버튼
        악보나라.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(MypageSettingActivity.this,MainActivity.class);
                intent_main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_main);
            }
        });
        // 마이페이지 버튼
        button_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_mypage = new Intent(MypageSettingActivity.this, MypageActivity.class);
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
                    Intent intent_alarm = new Intent(MypageSettingActivity.this, AlarmActivity.class);
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
                Intent intent_sheetmusic = new Intent(MypageSettingActivity.this,SheetmusicActivity.class);
                intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_sheetmusic);
            }
        });
        // 영상 커뮤니티 버튼
        icon_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_video = new Intent(MypageSettingActivity.this,VideoActivity.class);
                intent_video.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_video);
            }
        });
        // 글 커뮤니티 버튼
        icon_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_community = new Intent(MypageSettingActivity.this,CommunityActivity.class);
                intent_community.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_community);
            }
        });
        //--------------------------------------------------------------------------------------------------

        //프로필 변경 버튼
        ChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_changeprofile = new Intent(MypageSettingActivity.this,ChangeProfileActivity.class);
                intent_changeprofile.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_changeprofile);
            }
        });


        //로그아웃 버튼
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DATABASE.put("로그인정보",-1);
                    SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("데이터베이스", DATABASE.toString());
                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(MypageSettingActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent_logout = new Intent(MypageSettingActivity.this,MainActivity.class);
                intent_logout.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                finish();
                startActivity(intent_logout);

            }
        });

        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageSettingActivity.this,ChangePasswordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
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

