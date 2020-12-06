package com.jun.akbonara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlarmActivity extends AppCompatActivity {

    //기본 메뉴 멤버 변수 선언
    ImageView button_mypage, button_alarm, icon_music ,icon_video ,icon_community;
    TextView 악보나라;
    int ID_NUMBER;
    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONArray array_Article_Sheetmusic = new JSONArray();
    JSONArray array_Article_Video = new JSONArray();
    JSONArray array_Article_Community = new JSONArray();


    private RecyclerView RecyclerView_alarm;
    private Adapter_AlarmActivity mAdapter;
    private LinearLayoutManager layoutManager;

    List<Item_AlarmActivity> alarmList = new ArrayList<>();

    ImageView more_btn;
    Button alldelete_btn,viewdelete_btn,allview_btn;
    LinearLayout more_btn_menu;
    TextView letter_noalarm;

    boolean more_btn_filter = false;




    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);  //layout xml 매칭

        //기본 메뉴 주소 매칭
        button_mypage = findViewById(R.id.button_mypage);
        button_alarm =findViewById(R.id.button_alarm);
        악보나라 = findViewById(R.id.악보나라);
        icon_music = findViewById(R.id.icon_music);
        icon_video = findViewById(R.id.icon_video);
        icon_community = findViewById(R.id.icon_community);

        more_btn = findViewById (R.id.more_btn);
        alldelete_btn = findViewById (R.id.alldelete_btn);
        viewdelete_btn = findViewById (R.id.viewdelete_btn);
        allview_btn = findViewById (R.id.allview_btn);
        more_btn_menu =findViewById (R.id.more_btn_menu);
        letter_noalarm = findViewById (R.id.letter_noalarm);



        //리싸이클러 뷰 - 악보

        RecyclerView_alarm = (RecyclerView) findViewById(R.id.RecyclerView_alarm);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        RecyclerView_alarm.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);


        RecyclerView_alarm.setLayoutManager(layoutManager);


    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();


        //데이터 베이스 불러오기
        final SharedPreferences preferences = getSharedPreferences("데이터베이스",0);
        String JsonDATA = preferences.getString("데이터베이스","");
        try {
            DATABASE = new JSONObject(JsonDATA);
            array_client =  DATABASE.getJSONArray("회원정보");
            array_Article_Sheetmusic = DATABASE.getJSONArray("악보글정보");
            array_Article_Video = DATABASE.getJSONArray("영상글정보");
            array_Article_Community = DATABASE.getJSONArray("커뮤니티글정보");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //로그인 아이디넘버
        try {
            ID_NUMBER = DATABASE.getInt("로그인정보");
            Log.d("로그인 회원정보", String.valueOf(ID_NUMBER));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //알람 빨간 표시
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


        if(more_btn_filter==true){
            more_btn_menu.setVisibility(View.VISIBLE);
            more_btn_menu.bringToFront();
        }
        else{
            more_btn_menu.setVisibility(View.GONE);
        }


        //리사이클러뷰 초기화
        alarmList = new ArrayList<>();

        try{
            JSONObject currentClient = array_client.getJSONObject(ID_NUMBER);
            JSONArray array_alarm = currentClient.getJSONArray("알림목록");
            for (int i = 0, j = array_alarm.length(); i < j; i++) {
                JSONObject obj = array_alarm.getJSONObject(i);
                Item_AlarmActivity item_alarmActivity = new Item_AlarmActivity(obj.getInt("아이디넘버"),obj.getString("게시물타입"),
                        obj.getInt("게시글넘버"),obj.getBoolean("체크여부"),obj.getString("알림메세지"));

                alarmList.add(item_alarmActivity);

            }

        }catch (Exception e){

        }
        Log.d("알람리스트 사이즈", String.valueOf(alarmList.size()));

        //리싸이클러 뷰 구현
        mAdapter = new Adapter_AlarmActivity(alarmList,DATABASE);
        RecyclerView_alarm.setAdapter(mAdapter);


        //알람리스트가 없으면
        if(alarmList.size()==0){
            letter_noalarm.setVisibility(View.VISIBLE);
        }
        else{
            letter_noalarm.setVisibility(View.GONE);
        }




        //로그인 안되었을때 로그인 창으로 이동
        final Intent intent_requireLogin = new Intent(AlarmActivity.this,LoginActivity.class);

        //기본 메뉴 버튼 클릭이벤트
        // 홈 버튼
        악보나라.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(AlarmActivity.this,MainActivity.class);
                intent_main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_main);
            }
        });
        // 마이페이지 버튼
        button_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_mypage = new Intent(AlarmActivity.this, MypageActivity.class);
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
                    Intent intent_alarm = new Intent(AlarmActivity.this, AlarmActivity.class);
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
                Intent intent_sheetmusic = new Intent(AlarmActivity.this,SheetmusicActivity.class);
                intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_sheetmusic);
            }
        });
        // 영상 커뮤니티 버튼
        icon_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_video = new Intent(AlarmActivity.this,VideoActivity.class);
                intent_video.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_video);
            }
        });
        // 글 커뮤니티 버튼
        icon_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_community = new Intent(AlarmActivity.this,CommunityActivity.class);
                intent_community.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_community);
            }
        });
        //--------------------------------------------------------------------------------------------------

        //아이템 클릭
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if(alarmList.get(position).get게시물타입().equals("community")){
                    try {
                        JSONObject currentClient = array_client.getJSONObject(ID_NUMBER);
                        JSONArray array_alarm = currentClient.getJSONArray("알림목록");
                        JSONObject alarmobj = array_alarm.getJSONObject(position);
                        Log.d("포지션값", String.valueOf(position));
                        alarmobj.put("체크여부",true);

                        SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("데이터베이스", DATABASE.toString());
                        editor.commit();


                        JSONObject article = array_Article_Community.getJSONObject(alarmList.get(position).get게시글넘버());
                        String articleName = article.getString("글제목");
                        Intent intent = new Intent(AlarmActivity.this,CommunityPostActivity.class);
                        intent.putExtra("글제목",articleName);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(alarmList.get(position).get게시물타입().equals("video")){
                    try {
                        JSONObject currentClient = array_client.getJSONObject(ID_NUMBER);
                        JSONArray array_alarm = currentClient.getJSONArray("알림목록");
                        JSONObject alarmobj = array_alarm.getJSONObject(position);
                        Log.d("포지션값", String.valueOf(position));
                        alarmobj.put("체크여부",true);

                        SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("데이터베이스", DATABASE.toString());
                        editor.commit();


                        JSONObject article = array_Article_Video.getJSONObject(alarmList.get(position).get게시글넘버());
                        String articleName = article.getString("글제목");
                        Intent intent = new Intent(AlarmActivity.this,VideoPostActivity.class);
                        intent.putExtra("글제목",articleName);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(alarmList.get(position).get게시물타입().equals("sheetmusic")){
                    try {
                        JSONObject currentClient = array_client.getJSONObject(ID_NUMBER);
                        JSONArray array_alarm = currentClient.getJSONArray("알림목록");
                        JSONObject alarmobj = array_alarm.getJSONObject(position);
                        Log.d("포지션값", String.valueOf(position));
                        alarmobj.put("체크여부",true);

                        SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("데이터베이스", DATABASE.toString());
                        editor.commit();


                        JSONObject article = array_Article_Sheetmusic.getJSONObject(alarmList.get(position).get게시글넘버());
                        String articleName = article.getString("글제목");
                        Intent intent = new Intent(AlarmActivity.this,SheetmusicPostActivity.class);
                        intent.putExtra("글제목",articleName);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(more_btn_filter == true ){
                    more_btn_filter = false;
                }
                else if(more_btn_filter == false ){
                    more_btn_filter = true;
                }
                onResume();
            }
        });

        //전체 알람 삭제
        alldelete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject currentClient = null;
                try {
                    currentClient = array_client.getJSONObject(ID_NUMBER);
                    JSONArray jsonArray = new JSONArray();
                    currentClient.put("알림목록",jsonArray);

                    SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("데이터베이스", DATABASE.toString());
                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                more_btn_filter=false;
                onResume();
            }
        });

        //읽은 알람 삭제
        viewdelete_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                JSONObject currentClient = null;
                try {
                    currentClient = array_client.getJSONObject(ID_NUMBER);
                    JSONArray array_alarm = currentClient.getJSONArray("알림목록");
                    for(int i =0;i<array_alarm.length();i++){
                        JSONObject alarmobj = array_alarm.getJSONObject(i);
                        if(alarmobj.getBoolean("체크여부")==true){
                            array_alarm.remove(i);
                            i= i-1;
                        }
                        SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("데이터베이스", DATABASE.toString());
                        editor.commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                more_btn_filter=false;
                onResume();
            }
        });

        //모두 읽음 처리
        allview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject currentClient = null;
                try {
                    currentClient = array_client.getJSONObject(ID_NUMBER);
                    JSONArray array_alarm = currentClient.getJSONArray("알림목록");
                    for(int i =0;i<array_alarm.length();i++){
                        JSONObject alarmobj = array_alarm.getJSONObject(i);
                        alarmobj.put("체크여부",true);

                        SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("데이터베이스", DATABASE.toString());
                        editor.commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                more_btn_filter=false;
                onResume();
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
        more_btn_filter = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

