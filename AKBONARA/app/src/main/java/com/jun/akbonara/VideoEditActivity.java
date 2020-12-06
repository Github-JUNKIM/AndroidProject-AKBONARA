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

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VideoEditActivity extends AppCompatActivity {

    //기본 메뉴 멤버 변수 선언
    ImageView button_mypage, button_alarm, icon_music ,icon_video ,icon_community;
    TextView 악보나라;
    int ID_NUMBER;
    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONArray array_Article_Sheetmusic = new JSONArray();
    JSONArray array_Article_Video = new JSONArray();
    //--------------------------------------------------------------------------------------------------

    TextView letter_register_article;
    Button button_upload;
    EditText ArticleName, editTextTextMultiLine, youtube_address;

    Button button00,button01,button02,button03,button10,button11,button12,button13,button20,button21,button22,button23;



    Button button_youtube_address;


    Boolean Boolean_youtube=false;
    YouTubePlayerView youtubeView;
    YouTubePlayer mYouTubePlayer;
    String videoID="";

    JSONObject thisArticle = new JSONObject();
    String[] genre = new String[1];
    int ArticleNumber;


    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_upload);  //layout xml 매칭

        //기본 메뉴 주소 매칭
        button_mypage = findViewById(R.id.button_mypage);
        button_alarm =findViewById(R.id.button_alarm);
        악보나라 = findViewById(R.id.악보나라);
        icon_music = findViewById(R.id.icon_music);
        icon_video = findViewById(R.id.icon_video);
        icon_community = findViewById(R.id.icon_community);
        //--------------------------------------------------------------------------------------------------

        button_upload = findViewById(R.id.button_upload);
        ArticleName = findViewById(R.id.ArticleName);
        editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine);
        youtube_address = findViewById(R.id.youtube_address);
        button00 = findViewById(R.id.button00);
        button01 = findViewById(R.id.button01);
        button02 = findViewById(R.id.button02);
        button03 = findViewById(R.id.button03);
        button10 = findViewById(R.id.button10);
        button11 = findViewById(R.id.button11);
        button12 = findViewById(R.id.button12);
        button13 = findViewById(R.id.button13);
        button20 = findViewById(R.id.button20);
        button21 = findViewById(R.id.button21);
        button22 = findViewById(R.id.button22);
        button23 = findViewById(R.id.button23);

        youtubeView = findViewById(R.id.youtubeView);
        button_youtube_address = findViewById(R.id.button_youtube_address);
        letter_register_article = findViewById(R.id.letter_register_article);

        button_upload.setText("수정하기");
        letter_register_article.setText("영상 수정");


        //데이터 베이스 불러오기
        SharedPreferences preferences = getSharedPreferences("데이터베이스",0);
        String JsonDATA = preferences.getString("데이터베이스","");
        try {
            DATABASE = new JSONObject(JsonDATA);
            array_client =  DATABASE.getJSONArray("회원정보");
            array_Article_Sheetmusic = DATABASE.getJSONArray("악보글정보");
            array_Article_Video = DATABASE.getJSONArray("영상글정보");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent_get = getIntent();
        ArticleNumber = intent_get.getIntExtra("악보넘버",-1);
        if(ArticleNumber!=-1){
            try {
                thisArticle = array_Article_Video.getJSONObject(ArticleNumber);
                Boolean_youtube=true;

                ArticleName.setText(thisArticle.getString("글제목"));

                editTextTextMultiLine.setText(thisArticle.getString("글"));
                youtube_address.setText( "https://youtu.be/"+thisArticle.getString("동영상주소"));
                videoID = thisArticle.getString("동영상주소");

                youtubeView.setVisibility(View.VISIBLE);
                //유튜브 플레이어 초기셋팅
                youtubeView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(YouTubePlayer youTubePlayer) {
                        super.onReady(youTubePlayer);
                        mYouTubePlayer = youTubePlayer;
                        try {
                            mYouTubePlayer.cueVideo(thisArticle.getString("동영상주소"),0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                String selectedgenre = thisArticle.getString("장르");
                genre[0] = selectedgenre;
                switch(selectedgenre){
                    case "K-POP" : button00.setBackgroundResource(R.drawable.checkcomplete); break;
                    case "POP" : button01.setBackgroundResource(R.drawable.checkcomplete); break;
                    case "뉴에이지" : button02.setBackgroundResource(R.drawable.checkcomplete); break;
                    case "클래식" : button03.setBackgroundResource(R.drawable.checkcomplete); break;
                    case "재즈" : button10.setBackgroundResource(R.drawable.checkcomplete); break;
                    case "연탄곡" : button11.setBackgroundResource(R.drawable.checkcomplete); break;
                    case "게임/애니" : button12.setBackgroundResource(R.drawable.checkcomplete); break;
                    case "드라마/영화" : button13.setBackgroundResource(R.drawable.checkcomplete); break;
                    case "동요" : button20.setBackgroundResource(R.drawable.checkcomplete); break;
                    case "BGM" : button21.setBackgroundResource(R.drawable.checkcomplete); break;
                    case "CCM" : button22.setBackgroundResource(R.drawable.checkcomplete); break;
                    case "캐롤" : button23.setBackgroundResource(R.drawable.checkcomplete); break;
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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
            array_Article_Sheetmusic = DATABASE.getJSONArray("악보글정보");
            array_Article_Video = DATABASE.getJSONArray("영상글정보");
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
        final Intent intent_requireLogin = new Intent(VideoEditActivity.this,LoginActivity.class);

        //기본 메뉴 버튼 클릭이벤트
        // 홈 버튼
        악보나라.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(VideoEditActivity.this,MainActivity.class);
                intent_main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_main);
                finish();
            }
        });
        // 마이페이지 버튼
        button_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_mypage = new Intent(VideoEditActivity.this, MypageActivity.class);
                    intent_mypage.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent_mypage);
                }
                else{
                    intent_requireLogin.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent_requireLogin.putExtra("액티비티","마이페이지");
                    startActivity(intent_requireLogin);
                }
                finish();
            }
        });
        //알림 버튼
        button_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_alarm = new Intent(VideoEditActivity.this, AlarmActivity.class);
                    intent_alarm.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent_alarm);
                }
                else{
                    intent_requireLogin.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent_requireLogin.putExtra("액티비티","알림");
                    startActivity(intent_requireLogin);
                }
                finish();
            }
        });
        // 악보 커뮤니티 버튼
        icon_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_sheetmusic = new Intent(VideoEditActivity.this,SheetmusicActivity.class);
                intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_sheetmusic);
                finish();
            }
        });
        // 영상 커뮤니티 버튼
        icon_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_video = new Intent(VideoEditActivity.this,VideoActivity.class);
                intent_video.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_video);
                finish();
            }
        });
        // 글 커뮤니티 버튼
        icon_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_community = new Intent(VideoEditActivity.this,CommunityActivity.class);
                intent_community.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_community);
                finish();
            }
        });
        //--------------------------------------------------------------------------------------------------



        button00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genre_btn_initialize();
                button00.setBackgroundResource(R.drawable.checkcomplete);
                genre[0] = "K-POP";
            }
        });
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genre_btn_initialize();
                button01.setBackgroundResource(R.drawable.checkcomplete);
                genre[0] = "POP";
            }
        });
        button02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genre_btn_initialize();
                button02.setBackgroundResource(R.drawable.checkcomplete);
                genre[0] = "뉴에이지";
            }
        });
        button03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genre_btn_initialize();
                button03.setBackgroundResource(R.drawable.checkcomplete);
                genre[0] = "클래식";
            }
        });
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genre_btn_initialize();
                button10.setBackgroundResource(R.drawable.checkcomplete);
                genre[0] = "재즈";
            }
        });
        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genre_btn_initialize();
                button11.setBackgroundResource(R.drawable.checkcomplete);
                genre[0] = "연탄곡";
            }
        });
        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genre_btn_initialize();
                button12.setBackgroundResource(R.drawable.checkcomplete);
                genre[0] = "게임/애니";
            }
        });
        button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genre_btn_initialize();
                button13.setBackgroundResource(R.drawable.checkcomplete);
                genre[0] = "드라마/영화";
            }
        });
        button20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genre_btn_initialize();
                button20.setBackgroundResource(R.drawable.checkcomplete);
                genre[0] = "동요";
            }
        });
        button21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genre_btn_initialize();
                button21.setBackgroundResource(R.drawable.checkcomplete);
                genre[0] = "BGM";
            }
        });
        button22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genre_btn_initialize();
                button22.setBackgroundResource(R.drawable.checkcomplete);
                genre[0] = "CCM";
            }
        });
        button23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genre_btn_initialize();
                button23.setBackgroundResource(R.drawable.checkcomplete);
                genre[0] = "캐롤";
            }
        });



        //유튜브 플레이어 초기셋팅
        youtubeView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                mYouTubePlayer = youTubePlayer;
            }
        });

        //유튜브 영상 등록 버튼
        button_youtube_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youtubeView.setVisibility(View.VISIBLE);
                String split[] = youtube_address.getText().toString().split("/");
                videoID = split[split.length-1];
                mYouTubePlayer.cueVideo(videoID,0);
                Boolean_youtube = true;
            }
        });

        //게시글 등록 버튼
        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String  uploadArticleName= ArticleName.getText().toString();
                String Article = editTextTextMultiLine.getText().toString();
                String Genre = genre[0];


                //업로드 절차
                if (uploadArticleName.equals("")  || videoID.equals("") || Article.equals("")  || Genre.equals("")   || Boolean_youtube==false   ) {
                    Toast.makeText(VideoEditActivity.this, "게시글 정보를 모두 등록해주세요.", Toast.LENGTH_SHORT).show();
                }

                else {

                    try {
                        thisArticle = array_Article_Video.getJSONObject(ArticleNumber);

                        thisArticle.put("글제목", uploadArticleName);
                        thisArticle.put("장르",Genre);
                        thisArticle.put("동영상주소", videoID);
                        thisArticle.put("글", Article);


                        DATABASE.put("영상글정보", array_Article_Video);


                        SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("데이터베이스", DATABASE.toString());
                        editor.commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(VideoEditActivity.this, "연주영상이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VideoEditActivity.this, VideoActivity.class);
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


    public void genre_btn_initialize(){
        button00.setBackgroundResource(R.drawable.btn_grey);
        button01.setBackgroundResource(R.drawable.btn_grey);
        button02.setBackgroundResource(R.drawable.btn_grey);
        button03.setBackgroundResource(R.drawable.btn_grey);
        button10.setBackgroundResource(R.drawable.btn_grey);
        button11.setBackgroundResource(R.drawable.btn_grey);
        button12.setBackgroundResource(R.drawable.btn_grey);
        button13.setBackgroundResource(R.drawable.btn_grey);
        button20.setBackgroundResource(R.drawable.btn_grey);
        button21.setBackgroundResource(R.drawable.btn_grey);
        button22.setBackgroundResource(R.drawable.btn_grey);
        button23.setBackgroundResource(R.drawable.btn_grey);

    }



}

