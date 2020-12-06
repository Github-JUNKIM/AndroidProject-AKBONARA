package com.jun.akbonara;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MypageActivity_minigame extends AppCompatActivity {

    //기본 메뉴 멤버 변수 선언
    ImageView button_mypage, button_alarm, icon_music ,icon_video ,icon_community;
    TextView 악보나라;
    int ID_NUMBER;
    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONArray array_Article_Sheetmusic = new JSONArray();
    JSONArray array_Article_Video = new JSONArray();
    JSONArray array_Article_Community = new JSONArray();
    //--------------------------------------------------------------------------------------------------

    JSONObject currentClient;

    ImageView gamer_computer,gamer_user,life_1,life_2,icon_info;
    LinearLayout sissors_btn,rock_btn,paper_btn;
    Button play_btn;
    TextView winlose,letter_sissors_btn,letter_rock_btn,letter_paper_btn,winNumber;

    int type=1;

    int computer_type;

    boolean run = true;

    int intwinnumber = 0;
    int life = 2;

    MediaPlayer mp;

    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_minigame);  //layout xml 매칭

        //기본 메뉴 주소 매칭
        button_mypage = findViewById(R.id.button_mypage);
        button_alarm =findViewById(R.id.button_alarm);
        악보나라 = findViewById(R.id.악보나라);
        icon_music = findViewById(R.id.icon_music);
        icon_video = findViewById(R.id.icon_video);
        icon_community = findViewById(R.id.icon_community);
        //--------------------------------------------------------------------------------------------------

        gamer_computer= findViewById (R.id. gamer_computer  );
        gamer_user = findViewById (R.id. gamer_user    );
        sissors_btn = findViewById (R.id.   sissors_btn   );
        rock_btn = findViewById (R.id.  rock_btn    );
        paper_btn = findViewById (R.id.  paper_btn    );
        play_btn = findViewById (R.id. play_btn   );
        winlose = findViewById (R.id.winlose);
        letter_sissors_btn = findViewById (R.id.letter_sissors_btn);
        letter_rock_btn = findViewById (R.id.letter_rock_btn);
        letter_paper_btn = findViewById (R.id.letter_paper_btn);
        life_1 = findViewById (R.id.life_1);
        life_2 = findViewById (R.id.life_2);
        winNumber = findViewById (R.id.winNumber);
        icon_info = findViewById (R.id.icon_info);




    }


    @Override
    protected void onStart() {
        super.onStart();
        mp = MediaPlayer.create(this,R.raw.dododo);
        mp.setLooping(true);
        mp.start();

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
            array_Article_Video = DATABASE.getJSONArray("영상글정보");
            array_Article_Community = DATABASE.getJSONArray("커뮤니티글정보");
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
            currentClient = array_client.getJSONObject(ID_NUMBER);
        } catch (JSONException e) {
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



        //유저 가위바위보 세팅
        switch(type){
            case 1 : gamer_user.setImageResource(R.drawable.sissors);colorinit();letter_sissors_btn.setTextColor(Color.MAGENTA);break;
            case 2 : gamer_user.setImageResource(R.drawable.rock);colorinit();letter_rock_btn.setTextColor(Color.MAGENTA);break;
            case 3 : gamer_user.setImageResource(R.drawable.paper);colorinit();letter_paper_btn.setTextColor(Color.MAGENTA);break;
        }

        //플레이버튼 셋팅
        if(run==true){
            play_btn.setText("플레이");
        }
        else{
            play_btn.setText("한번 더");
        }

        //생명 셋팅
        if(life==2){
            life_1.setImageResource(R.drawable.icon_like_selected);
            life_2.setImageResource(R.drawable.icon_like_selected);
        }
        else if(life==1){
            life_1.setImageResource(R.drawable.icon_like_selected);
            life_2.setImageResource(R.drawable.icon_like);
        }
        else if(life==0){
            life_1.setImageResource(R.drawable.icon_like);
            life_2.setImageResource(R.drawable.icon_like);
        }

        //승리횟수 셋팅
        winNumber.setText( Integer.toString(intwinnumber));


        //컴퓨터 가위바위보 셋팅
        Thread thread = new Thread(){
            @Override
            public void run(){
                try{
//
                    // UI 변경용 핸들러 기본
                    new Handler(Looper.getMainLooper()).post(new Runnable(){
                        @Override
                        public void run() {

                        }
                    });

                    while(run==true) {
                        gamer_computer.setImageResource(R.drawable.rock);
                        Thread.sleep(100);
                        gamer_computer.setImageResource(R.drawable.sissors);
                        Thread.sleep(100);
                        gamer_computer.setImageResource(R.drawable.paper);
                        Thread.sleep(100);
                    }

                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

        };

        thread.start();

        //가위 버튼
        sissors_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                gamer_user.setImageResource(R.drawable.sissors);
                colorinit();
                letter_sissors_btn.setTextColor(Color.MAGENTA);
            }
        });

        //바위 버튼
        rock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                gamer_user.setImageResource(R.drawable.rock);
                colorinit();
                letter_rock_btn.setTextColor(Color.MAGENTA);
            }
        });

        //보 버튼
        paper_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 3;
                gamer_user.setImageResource(R.drawable.paper);
                colorinit();
                letter_paper_btn.setTextColor(Color.MAGENTA);
            }
        });

        //정보 버튼
        icon_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoshow();
            }
        });

        //플레이버튼
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(run==true){

                    run =false;
                    try {
                        Thread.sleep(200
                        );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    computer_type = (int) (Math.random() * 3 + 1);
                    Log.d("컴퓨터 타입", String.valueOf(computer_type));


                    if(computer_type==1){
                        gamer_computer.setImageResource(R.drawable.sissors);
                    }
                    else if(computer_type==2){
                        gamer_computer.setImageResource(R.drawable.rock);
                    }
                    else if(computer_type==3){
                        gamer_computer.setImageResource(R.drawable.paper);
                    }



                    if (computer_type == type) {
                        winlose.setText("무승부");
                        winlose.setVisibility(View.VISIBLE);
                    } else if (type - computer_type == -1 || type - computer_type == 2) {
                        winlose.setText("패배");
                        winlose.setVisibility(View.VISIBLE);
                        life = life - 1;
                    } else if (type - computer_type == 1 || type - computer_type == -2) {
                        winlose.setText("승리");
                        winlose.setVisibility(View.VISIBLE);
                        intwinnumber = intwinnumber + 1;
                    }

                    play_btn.setText("한번더");


                }
                else if(run==false){
                    run =true;
                    play_btn.setText("플레이");
                    winlose.setVisibility(View.INVISIBLE);
                    winlose.setText("");
                }
                onResume();
            }
        });


        if(intwinnumber==2){
            show();
        }

        if(life==0){
            failshow();
        }







        //로그인 안되었을때 로그인 창으로 이동
        final Intent intent_requireLogin = new Intent(MypageActivity_minigame.this,LoginActivity.class);

        //기본 메뉴 버튼 클릭이벤트
        // 홈 버튼
        악보나라.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(MypageActivity_minigame.this,MainActivity.class);
                intent_main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_main);
            }
        });
        // 마이페이지 버튼
        button_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_mypage = new Intent(MypageActivity_minigame.this, MypageActivity.class);
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
                    Intent intent_alarm = new Intent(MypageActivity_minigame.this, AlarmActivity.class);
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
                Intent intent_sheetmusic = new Intent(MypageActivity_minigame.this,SheetmusicActivity.class);
                intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_sheetmusic);
            }
        });
        // 영상 커뮤니티 버튼
        icon_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_video = new Intent(MypageActivity_minigame.this,VideoActivity.class);
                intent_video.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_video);
            }
        });
        // 글 커뮤니티 버튼
        icon_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_community = new Intent(MypageActivity_minigame.this,CommunityActivity.class);
                intent_community.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_community);
            }
        });
        //--------------------------------------------------------------------------------------------------




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
        mp.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void colorinit(){
        letter_sissors_btn.setTextColor(Color.GRAY);
        letter_paper_btn.setTextColor(Color.GRAY);
        letter_rock_btn.setTextColor(Color.GRAY);
    }


    //성공 다이얼로그
    public void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("축하합니다! 보상으로 100캐시가 주어집니다.");
        builder.setTitle("성공");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int currentcash = currentClient.getInt("캐시");
                            currentcash += 100;

                            currentClient.put("캐시",currentcash);

                            //캐시내역
                            JSONArray CashHistory = currentClient.getJSONArray("캐시내역");
                            JSONObject historyObj = new JSONObject();
                            long time = System.currentTimeMillis();
                            historyObj.put("시간",time);
                            historyObj.put("타입","미니게임 보상");
                            historyObj.put("캐시정보","캐시 100원");
                            historyObj.put("금액",100);
                            CashHistory.put(historyObj);

                            SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("데이터베이스", DATABASE.toString());
                            editor.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mp.stop();
                        finish();
                    }
                });
        builder.show();
    }

    //실패 다이얼로그
    public void failshow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("실패");
        builder.setMessage("다음에 도전하세요");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mp.stop();
                        finish();
                    }
                });
        builder.show();
    }
    //정보 다이얼로그
    public void infoshow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("3판 2선승 가위바위보 게임입니다. 2번 승리하면 보상으로 100캐시를 획득할 수 있습니다.");
        builder.setTitle("<가위바위보 게임>");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();

    }

}

