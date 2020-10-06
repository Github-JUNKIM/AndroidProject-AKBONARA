package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

public class SheetmusicPostActivity extends AppCompatActivity {

    //기본 메뉴 멤버 변수 선언
    ImageView button_mypage, button_alarm, icon_music ,icon_video ,icon_community;
    TextView 악보나라;
    int ID_NUMBER;
    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONArray array_SheetmusicArticle = new JSONArray();
    //--------------------------------------------------------------------------------------------------

    int ArticleNumber;

    ImageView profile_1;
    TextView nickname_1;
    TextView genre_drama;
    TextView upload_time;
    TimeCalculator timeCalculator;
    PDFView pdfviewer;
    YouTubePlayerView YoutubeVideo;
    TextView number_of_hits;
    TextView price;
    TextView postcontent;
    TextView number_of_like;
    TextView number_of_comment;
    TextView postname_1;

    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheetmusic_post);  //layout xml 매칭

        //기본 메뉴 주소 매칭
        button_mypage = findViewById(R.id.button_mypage);
        button_alarm =findViewById(R.id.button_alarm);
        악보나라 = findViewById(R.id.악보나라);
        icon_music = findViewById(R.id.icon_music);
        icon_video = findViewById(R.id.icon_video);
        icon_community = findViewById(R.id.icon_community);
        //--------------------------------------------------------------------------------------------------

        profile_1 = findViewById(R.id.profile_1);
        nickname_1 = findViewById(R.id.nickname_1);
        genre_drama = findViewById(R.id.genre_drama);
        upload_time = findViewById(R.id.upload_time);
        pdfviewer = findViewById(R.id.pdfviewer);
        YoutubeVideo = findViewById(R.id.YoutubeVideo);
        number_of_hits = findViewById(R.id.number_of_hits);
        price = findViewById(R.id.price);
        postcontent=findViewById(R.id.postcontent);
        number_of_like = findViewById(R.id.number_of_like);
        number_of_comment = findViewById(R.id.number_of_comment);
        postname_1 = findViewById(R.id.postname_1);
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

        Intent intent = getIntent();
        String CheckArticleName = intent.getStringExtra("글제목");

        for(int i=0;i<array_SheetmusicArticle.length();i++){
            try {
                final JSONObject a = (JSONObject) array_SheetmusicArticle.get(i);
                if(a.getString("글제목").equals(CheckArticleName)){
                    if(a.getString("프로필").equals("")){
                        profile_1.setImageResource(R.drawable.ic_icon_basicprofile);
                    }
                    else{
                        profile_1.setImageURI(Uri.parse(a.getString("프로필")));
                    }

                    nickname_1.setText(a.getString("닉네임"));
                    genre_drama.setText(a.getString("장르"));
                    upload_time.setText(timeCalculator.formatTimeString(a.getLong("업로드시간")));
                    pdfviewer.fromUri(Uri.parse(a.getString("악보"))).pages(0).load();
                    YoutubeVideo.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(YouTubePlayer youTubePlayer) {
                            super.onReady(youTubePlayer);
                            try {
                                youTubePlayer.cueVideo(a.getString("동영상주소"),0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    number_of_hits.setText(Integer.toString(a.getInt("조회수")));
                    number_of_like.setText(Integer.toString(a.getInt("좋아요수")));
                    number_of_comment.setText(Integer.toString(a.getInt("댓글수")));
                    if(a.getInt("가격")==0){
                        price.setText("캐시  무료");
                    }
                    else {
                        price.setText("캐시  " + Integer.toString(a.getInt("가격")) + "원");
                    }
                    postcontent.setText(a.getString("글"));
                    postname_1.setText(a.getString("글제목"));





                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //로그인 안되었을때 로그인 창으로 이동
        final Intent intent_requireLogin = new Intent(SheetmusicPostActivity.this,LoginActivity.class);

        //기본 메뉴 버튼 클릭이벤트
        // 홈 버튼
        악보나라.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(SheetmusicPostActivity.this,MainActivity.class);
                intent_main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_main);
            }
        });
        // 마이페이지 버튼
        button_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_mypage = new Intent(SheetmusicPostActivity.this, MypageActivity.class);
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
                    Intent intent_alarm = new Intent(SheetmusicPostActivity.this, AlarmActivity.class);
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
                Intent intent_sheetmusic = new Intent(SheetmusicPostActivity.this,SheetmusicActivity.class);
                intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_sheetmusic);
            }
        });
        // 영상 커뮤니티 버튼
        icon_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_video = new Intent(SheetmusicPostActivity.this,VideoActivity.class);
                intent_video.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_video);
            }
        });
        // 글 커뮤니티 버튼
        icon_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_community = new Intent(SheetmusicPostActivity.this,CommunityActivity.class);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




}

