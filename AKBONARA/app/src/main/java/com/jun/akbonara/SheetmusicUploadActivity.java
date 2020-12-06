package com.jun.akbonara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.github.barteksc.pdfviewer.PDFView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SheetmusicUploadActivity extends AppCompatActivity {

    //기본 메뉴 멤버 변수 선언
    ImageView button_mypage, button_alarm, icon_music ,icon_video ,icon_community;
    TextView 악보나라;
    int ID_NUMBER;
    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONArray array_Article_Sheetmusic = new JSONArray();
    //--------------------------------------------------------------------------------------------------

    Button button_upload;
    EditText ArtistName, SheetmusicName, editTextTextMultiLine, youtube_address, price_register;
    Button upload_pdf;
    Button button00,button01,button02,button03,button10,button11,button12,button13,button20,button21,button22,button23;
    TextView pdfName,register_sheetmusic;
    LinearLayout Linear_Sheetmusic;
    Button ChangeSheetmusic;
    Button button_youtube_address;

    //사진 선택 코드
    private final int REQ_CODE_PDF = 1;

    String SheetmusicUri ="";
    String SheetmusicFileName ="";
    PDFView pdfviewer;
    Boolean Boolean_upload=false;
    Boolean Boolean_youtube=false;
    YouTubePlayerView youtubeView;
    YouTubePlayer mYouTubePlayer;
    String videoID="";



    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheetmusic_upload);  //layout xml 매칭

        //기본 메뉴 주소 매칭
        button_mypage = findViewById(R.id.button_mypage);
        button_alarm =findViewById(R.id.button_alarm);
        악보나라 = findViewById(R.id.악보나라);
        icon_music = findViewById(R.id.icon_music);
        icon_video = findViewById(R.id.icon_video);
        icon_community = findViewById(R.id.icon_community);
        //--------------------------------------------------------------------------------------------------

        button_upload = findViewById(R.id.button_upload);
        ArtistName = findViewById(R.id.ArtistName);
        SheetmusicName = findViewById(R.id.SheetmusicName);
        editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine);
        youtube_address = findViewById(R.id.youtube_address);
        price_register = findViewById(R.id.price_register);
        upload_pdf = findViewById(R.id.upload_pdf);
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
        pdfviewer = findViewById(R.id.pdfviewer);
        pdfName = findViewById(R.id.pdfName);
        Linear_Sheetmusic = findViewById(R.id.Linear_Sheetmusic);
        ChangeSheetmusic = findViewById(R.id.ChangeSheetmusic);
        youtubeView = findViewById(R.id.youtubeView);
        button_youtube_address = findViewById(R.id.button_youtube_address);
        register_sheetmusic = findViewById(R.id.register_sheetmusic);




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
        final Intent intent_requireLogin = new Intent(SheetmusicUploadActivity.this,LoginActivity.class);

        //기본 메뉴 버튼 클릭이벤트
        // 홈 버튼
        악보나라.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(SheetmusicUploadActivity.this,MainActivity.class);
                intent_main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_main);
            }
        });
        // 마이페이지 버튼
        button_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_mypage = new Intent(SheetmusicUploadActivity.this, MypageActivity.class);
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
                    Intent intent_alarm = new Intent(SheetmusicUploadActivity.this, AlarmActivity.class);
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
                Intent intent_sheetmusic = new Intent(SheetmusicUploadActivity.this,SheetmusicActivity.class);
                intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_sheetmusic);
            }
        });
        // 영상 커뮤니티 버튼
        icon_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_video = new Intent(SheetmusicUploadActivity.this,VideoActivity.class);
                intent_video.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_video);
            }
        });
        // 글 커뮤니티 버튼
        icon_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_community = new Intent(SheetmusicUploadActivity.this,CommunityActivity.class);
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

        //해당 아이디의 프로필이미지
        String ProfileImage="";
        try {
            JSONObject a =(JSONObject) array_client.get(ID_NUMBER);
            ProfileImage = a.getString("프로필사진");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //pdfview 셋팅

        if(Boolean_upload==true){
            Linear_Sheetmusic.setVisibility(View.VISIBLE);
            Uri Sheetmusic = Uri.parse(SheetmusicUri);
            pdfviewer.fromUri(Sheetmusic).swipeHorizontal(true).load();
            pdfName.setText(SheetmusicFileName);
        }

        //유튜브 플레이어 초기셋팅
        youtubeView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                mYouTubePlayer = youTubePlayer;
            }
        });


        //장르선택
        final String finalNICKNAME = NICKNAME;
        final String finalProfileImage = ProfileImage;
        final String[] genre = new String[1];
        genre[0]="";

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

        //악보 업로드 버튼
        upload_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                startActivityForResult(intent, REQ_CODE_PDF);

            }
        });
        //악보 수정버튼
        ChangeSheetmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                startActivityForResult(intent, REQ_CODE_PDF);

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


                String Artist_Name = ArtistName.getText().toString();
                String Sheetmusic_Name = SheetmusicName.getText().toString();
                String Article = editTextTextMultiLine.getText().toString();
                String Price = price_register.getText().toString();
                String Genre = genre[0];
                int initialnum = 0;


                //업로드 절차
                if (Artist_Name.equals("") || Sheetmusic_Name.equals("")  || videoID.equals("") || Article.equals("") || Price.equals("") || Genre.equals("")  || Boolean_upload==false   || Boolean_youtube==false   ) {
                    Toast.makeText(SheetmusicUploadActivity.this, "게시글 정보를 모두 등록해주세요.", Toast.LENGTH_SHORT).show();
                }

                else {

                    try {
                        long time = System.currentTimeMillis();
                        JSONArray newJsonarray = new JSONArray();

                        JSONObject article = new JSONObject();
                        article.put("글제목", Artist_Name+" - "+Sheetmusic_Name+" By "+ finalNICKNAME);
                        article.put("원곡자",Artist_Name);
                        article.put("악보명",Sheetmusic_Name);
                        article.put("아이디넘버",ID_NUMBER);
                        article.put("프로필", finalProfileImage);
                        article.put("닉네임", finalNICKNAME);
                        article.put("장르",Genre);
                        article.put("조회수",initialnum);
                        article.put("좋아요수",initialnum);
                        article.put("댓글수",initialnum);
                        article.put("가격", Integer.parseInt(Price));
                        article.put("동영상주소", videoID);
                        article.put("글", Article);
                        article.put("악보",SheetmusicUri);
                        article.put("업로드시간",time);
                        article.put("조회한사람",newJsonarray);
                        article.put("좋아요한사람",newJsonarray);
                        article.put("댓글목록",newJsonarray);
                        article.put("구매수",0);


                        array_Article_Sheetmusic.put(article);
                        DATABASE.put("악보글정보", array_Article_Sheetmusic);


                        SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("데이터베이스", DATABASE.toString());
                        editor.commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(SheetmusicUploadActivity.this, "악보가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SheetmusicUploadActivity.this, SheetmusicActivity.class);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PDF) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Uri originalUri = data.getData();
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                // Check for the freshest data.
                getContentResolver().takePersistableUriPermission(originalUri, takeFlags);

                SheetmusicUri = originalUri.toString();
                SheetmusicFileName = getFileName(originalUri);
                Boolean_upload = true;



            }
        }
    }

    //파일이름 찾기 메소드
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
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

