package com.jun.akbonara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class MypageActivity extends AppCompatActivity {

    //기본 메뉴 멤버 변수 선언
    ImageView button_mypage, button_alarm, icon_music ,icon_video ,icon_community;
    TextView 악보나라;
    //--------------------------------------------------------------------------

    ImageView button_setting;
    CircleImageView profile;
    Button button_charge,button_history;
    TextView nickname;
    TextView mylike,mypost,minigame,cashmoney,purchasesheetmusic;

    JSONObject DATABASE;
    JSONArray array_client;
    JSONObject currentClinet;

    int ID_NUMBER;

    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);  //layout xml 매칭


        //기본 메뉴 주소 매칭
        button_mypage = findViewById(R.id.button_mypage);
        button_alarm =findViewById(R.id.button_alarm);
        악보나라 = findViewById(R.id.악보나라);
        icon_music = findViewById(R.id.icon_music);
        icon_video = findViewById(R.id.icon_video);
        icon_community = findViewById(R.id.icon_community);
        //--------------------------------------------------------------------------

        button_setting = findViewById(R.id.button_setting);
        button_charge = findViewById(R.id.button_charge);
        nickname = findViewById(R.id.nickname);
        profile = findViewById(R.id.profile);
        mylike = findViewById(R.id.mylike);
        mypost = findViewById(R.id.mypost);
        minigame = findViewById (R.id. minigame  );
        cashmoney = findViewById (R.id.cashmoney);
        purchasesheetmusic = findViewById (R.id.purchasesheetmusic);
        button_history = findViewById (R.id.button_history);




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

        //현재 회원데이터 셋팅
        try {
            currentClinet = array_client.getJSONObject(ID_NUMBER);
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
        final Intent intent_requireLogin = new Intent(MypageActivity.this,LoginActivity.class);

        //기본 메뉴 버튼 클릭이벤트
        // 홈 버튼
        악보나라.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(MypageActivity.this,MainActivity.class);
                intent_main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_main);
            }
        });
        // 마이페이지 버튼
        button_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_mypage = new Intent(MypageActivity.this, MypageActivity.class);
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
                    Intent intent_alarm = new Intent(MypageActivity.this, AlarmActivity.class);
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
                Intent intent_sheetmusic = new Intent(MypageActivity.this,SheetmusicActivity.class);
                intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_sheetmusic);
            }
        });
        // 영상 커뮤니티 버튼
        icon_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_video = new Intent(MypageActivity.this,VideoActivity.class);
                intent_video.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_video);
            }
        });
        // 글 커뮤니티 버튼
        icon_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_community = new Intent(MypageActivity.this,CommunityActivity.class);
                intent_community.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_community);
            }
        });
        //--------------------------------------------------------------------------------------------------



        //해당 아이디의 닉네임
        String NICKNAME="";
        try {
            JSONObject a =(JSONObject) array_client.get(ID_NUMBER);
            NICKNAME = a.getString("닉네임");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //닉네임 셋팅
        nickname.setText(NICKNAME);

        //해당 아이디의 프로필이미지
        String ProfileImage="";
        try {
            JSONObject a =(JSONObject) array_client.get(ID_NUMBER);
            ProfileImage = a.getString("프로필사진");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //프로필 사진 셋팅
        if(!ProfileImage.equals("")) {
            Uri ProfileImageUri = Uri.parse(ProfileImage);
            profile.setImageURI(ProfileImageUri);
        }
        else{
            profile.setImageResource(R.drawable.ic_icon_basicprofile);
        }


        DecimalFormat formatter = new DecimalFormat("###,###");

        //현재 캐시
        try {
            cashmoney.setText(formatter.format(currentClinet.getInt("캐시"))+"원");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //설정 버튼
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_setting = new Intent(MypageActivity.this,MypageSettingActivity.class);
                startActivity(intent_setting);
            }
        });

        //충전 버튼
        button_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_charge = new Intent(MypageActivity.this,ChargeActivity.class);
                startActivity(intent_charge);
            }
        });
        button_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this,MypageActivity_CashHistory.class);
                startActivity(intent);
            }
        });
        //구매한 악보 버튼
        purchasesheetmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this,MypageActivity_purchase.class);
                startActivity(intent);
            }
        });
        //좋아요 버튼
        mylike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this,MypageActivity_like.class);
                startActivity(intent);
            }
        });
        //게시글 버튼
        mypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this,MypageActivity_mypost.class);
                startActivity(intent);
            }
        });
        //미니게임 버튼
        minigame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this,MypageActivity_minigame.class);
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

