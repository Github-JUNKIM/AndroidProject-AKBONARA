package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    LinearLayout Linear_loginstate;

    //기본 메뉴 멤버 변수 선언
    ImageView button_mypage, button_alarm, icon_music ,icon_video ,icon_community;
    TextView 악보나라;
    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    int ID_NUMBER;


    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  //layout xml 매칭

        //리싸이클러 뷰

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView_main_sheetmusic);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//--------------------------------------------------------------------------------------------------------------------
        //기본 메뉴 주소 매칭
        button_mypage = findViewById(R.id.button_mypage);
        button_alarm =findViewById(R.id.button_alarm);
        악보나라 = findViewById(R.id.악보나라);
        icon_music = findViewById(R.id.icon_music);
        icon_video = findViewById(R.id.icon_video);
        icon_community = findViewById(R.id.icon_community);

        Linear_loginstate = findViewById(R.id.Linear_loginstate);

        Log.i("메인","메인  onCreate");
        Toast.makeText(this, "메인  onCreate", Toast.LENGTH_SHORT).show();
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

        //첫번째 리싸이클러뷰
        try {
            JSONArray arrayClientInfo =  DATABASE.getJSONArray("회원정보");
            List<main_item_sheetmusic> ClientInfo = new ArrayList<>();

            for(int i=0 , j = arrayClientInfo.length(); i <j ;i++){
                JSONObject obj =  arrayClientInfo.getJSONObject(i);

                main_item_sheetmusic ClientData = new main_item_sheetmusic();
                ClientData.set닉네임(obj.getString("닉네임"));
                ClientData.set아이디(obj.getString("아이디"));
                ClientData.set비밀번호(obj.getString("비밀번호"));
                ClientInfo.add(ClientData);
            }

            // specify an adapter (see also next example)
            mAdapter = new Adapter_main_item_sheetmusic(ClientInfo);
            recyclerView.setAdapter(mAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //로그인 아이디넘버
        try {
            ID_NUMBER = DATABASE.getInt("로그인정보");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //로그인 상태
        if(ID_NUMBER!=-1){
            Linear_loginstate.setVisibility(View.GONE);
        }
        else {
            Linear_loginstate.setVisibility(View.VISIBLE);
        }

        //로그인 안되었을때 로그인 창으로 이동
        final Intent intent_requireLogin = new Intent(MainActivity.this,LoginActivity.class);


        //기본 메뉴 버튼 클릭이벤트
        // 홈 버튼
        악보나라.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(MainActivity.this,MainActivity.class);
                intent_main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_main);
            }
        });
        // 마이페이지 버튼
        button_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_mypage = new Intent(MainActivity.this, MypageActivity.class);
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
                    Intent intent_alarm = new Intent(MainActivity.this, AlarmActivity.class);
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
                Intent intent_sheetmusic = new Intent(MainActivity.this,SheetmusicActivity.class);
                intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_sheetmusic);
            }
        });
        // 영상 커뮤니티 버튼
        icon_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_video = new Intent(MainActivity.this,VideoActivity.class);
                intent_video.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_video);
            }
        });
        // 글 커뮤니티 버튼
        icon_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_community = new Intent(MainActivity.this,CommunityActivity.class);
                intent_community.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_community);
            }
        });
        //--------------------------------------------------------------------------------------------------


        // 1. 로그인 상태버튼
        Linear_loginstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER==-1) {
                    intent_requireLogin.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent_requireLogin.putExtra("액티비티","메인");
                    startActivity(intent_requireLogin);
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
        try {
            SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
            SharedPreferences.Editor editor = preferences.edit();
            DATABASE.put("로그인정보",-1);
            editor.putString("데이터베이스", DATABASE.toString());
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("메인","메인  onDestroy");
        Toast.makeText(this, "메인  onDestroy", Toast.LENGTH_SHORT).show();

    }


}

