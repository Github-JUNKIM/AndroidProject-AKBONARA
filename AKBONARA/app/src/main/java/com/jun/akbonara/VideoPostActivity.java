package com.jun.akbonara;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class VideoPostActivity extends AppCompatActivity {

    //기본 메뉴 멤버 변수 선언
    ImageView button_mypage, button_alarm, icon_music ,icon_video ,icon_community;
    TextView 악보나라;
    int ID_NUMBER;
    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONArray array_Article_Sheetmusic = new JSONArray();
    JSONArray array_Article_Video = new JSONArray();



    JSONArray watchingPersonList = new JSONArray();
    JSONObject currentClient = new JSONObject();
    JSONObject thisArticle = new JSONObject();

    //--------------------------------------------------------------------------------------------------

    int ArticleNumber;

    ImageView profile_1;
    TextView nickname_1;
    TextView genre_drama;
    TextView upload_time;
    TimeCalculator timeCalculator;
    YouTubePlayerView YoutubeVideo;
    TextView number_of_hits;
    TextView postcontent;
    TextView number_of_like;
    TextView number_of_comment;
    TextView postname_1;
    TextView upload_button;
    LinearLayout AddComment;
    ImageView Close_AddComment;
    TextView AddComment_button;
    EditText Comment_Content;
    String string_Comment_Content="";
    ImageView like;
    ImageView more_btn;
    LinearLayout EditOrDelete_btn;
    Button Edit_btn;
    Button Delete_btn;

    //댓글 옵션 버튼 불린
    boolean MOREBUTTON = false;

    boolean like_boolean=false;

    private RecyclerView RecyclerView_comment;
    private RecyclerView.LayoutManager layoutManager;
    private Adapter_Comment_SheetmusicActivity mAdapter;


    List<Item_Comment_Sheetmusic> Item_Comment_Sheetmusic_LIST = new ArrayList<>();


    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_post);  //layout xml 매칭

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
        YoutubeVideo = findViewById(R.id.YoutubeVideo);
        number_of_hits = findViewById(R.id.number_of_hits);
        postcontent=findViewById(R.id.postcontent);
        number_of_like = findViewById(R.id.number_of_like);
        number_of_comment = findViewById(R.id.number_of_comment);
        postname_1 = findViewById(R.id.postname_1);
        upload_button = findViewById(R.id.upload_button);
        AddComment = findViewById(R.id.AddComment);
        Close_AddComment = findViewById(R.id.Close_AddComment);
        AddComment_button = findViewById(R.id. AddComment_button);
        Comment_Content = findViewById(R.id.Comment_Content);
        like = findViewById(R.id.like);
        more_btn = findViewById(R.id.more_btn);
        EditOrDelete_btn = findViewById(R.id.EditOrDelete_btn);
        Edit_btn = findViewById(R.id.Edit_btn);
        Delete_btn = findViewById(R.id.Delete_btn);


        //리싸이클러뷰 매칭
        RecyclerView_comment = findViewById(R.id.RecyclerView_comment);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        RecyclerView_comment.setHasFixedSize(true);
        //레이아웃 매니저 리니얼 레이아웃으로 생성
        layoutManager = new LinearLayoutManager(this);

        //리싸이클러뷰 레이아웃 매니저 세팅
        RecyclerView_comment.setLayoutManager(layoutManager);







    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

        //해당 글넘버 찾기
        Intent intent = getIntent();
        String CheckArticleName = intent.getStringExtra("글제목");

        for(int i=0;i<array_Article_Video.length();i++){
            try {
                final JSONObject a = (JSONObject) array_Article_Video.get(i);
                if(a.getString("글제목").equals(CheckArticleName)){
                    ArticleNumber =i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        //댓글목록 초기화
//        try {
//            JSONArray newJsonarray = new JSONArray();
//            for(int i=0;i<array_SheetmusicArticle.length();i++) {
//                JSONObject holy = (JSONObject) array_SheetmusicArticle.get(i);
//                holy.remove("댓글목록");
//                holy.put("댓글목록",newJsonarray);
//
//                preferences = getSharedPreferences("데이터베이스", 0);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("데이터베이스", DATABASE.toString());
//                editor.commit();
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


//                //영상조아요목록 만들기
//        try {
//            JSONArray newJsonarray = new JSONArray();
//            for(int i=0;i<array_client.length();i++) {
//                JSONObject holy = (JSONObject) array_client.get(i);
//                holy.put("영상좋아요목록",newJsonarray);
//                holy.put("글좋아요목록",newJsonarray);
//
//                preferences = getSharedPreferences("데이터베이스", 0);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("데이터베이스", DATABASE.toString());
//                editor.commit();
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }





        //포스팅 내용 매칭
        try {
            thisArticle = (JSONObject) array_Article_Video.get(ArticleNumber);

            JSONObject writerObj = (JSONObject) array_client.get(thisArticle.getInt("아이디넘버"));
            Log.d("아이디넘버", String.valueOf(thisArticle.getInt("아이디넘버")));

            if(writerObj.getString("프로필사진").equals("")){
                profile_1.setImageResource(R.drawable.ic_icon_basicprofile);
            }
            else{
                profile_1.setImageURI(Uri.parse(writerObj.getString("프로필사진")));
            }

            nickname_1.setText(writerObj.getString("닉네임"));
            genre_drama.setText(thisArticle.getString("장르"));
            upload_time.setText(timeCalculator.formatTimeString(thisArticle.getLong("업로드시간")));
            YoutubeVideo.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    super.onReady(youTubePlayer);
                    try {
                        youTubePlayer.cueVideo(thisArticle.getString("동영상주소"),0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            number_of_like.setText(Integer.toString(thisArticle.getJSONArray("좋아요한사람").length()));
            number_of_comment.setText(Integer.toString(thisArticle.getJSONArray("댓글목록").length()));
            postcontent.setText(thisArticle.getString("글"));
            postname_1.setText(thisArticle.getString("글제목"));

            if(ID_NUMBER!=-1) {
                currentClient = (JSONObject) array_client.get(ID_NUMBER);
                JSONArray likeList = currentClient.getJSONArray("영상좋아요목록");
                for(int i =0; i<likeList.length();i++){
                    if(likeList.get(i).equals(ArticleNumber)){
                        like_boolean=true;
                        break;
                    }
                }
            }

            if(like_boolean==true){
                like.setImageResource(R.drawable.icon_like_selected);
            }
            else{
                like.setImageResource(R.drawable.icon_like);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //댓글 아이템 리스트 초기화
        Item_Comment_Sheetmusic_LIST = new ArrayList<>();

        //리싸이클러뷰 Item_Comment_Sheetmusic_LIST 만들기
        try {
            JSONArray jsonArray_comment_list = thisArticle.getJSONArray("댓글목록");

            for(int i=0 , j = jsonArray_comment_list.length(); i <j ;i++){
                JSONObject obj =  jsonArray_comment_list.getJSONObject(i);
                Item_Comment_Sheetmusic item_comment_sheetmusic = new Item_Comment_Sheetmusic(obj.getInt("아이디넘버"),obj.getString("댓글내용"),obj.getJSONArray("대댓글목록"),obj.getJSONArray("좋아요목록"),obj.getLong("작성시간"),obj.getBoolean("더보기불린"));
                Item_Comment_Sheetmusic_LIST.add(item_comment_sheetmusic);
            }
            Item_Comment_Sheetmusic_LIST.sort(new LikesDescending());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //어댑터 생성
        mAdapter = new Adapter_Comment_SheetmusicActivity(1,Item_Comment_Sheetmusic_LIST,DATABASE,ArticleNumber,ID_NUMBER,preferences,this,new SheetmusicPostActivity(),this,new CommunityPostActivity());

        //리싸이클러뷰에 어댑터 적용
        RecyclerView_comment.setAdapter(mAdapter);




        //로그인 안되었을때 로그인 창으로 이동
        final Intent intent_requireLogin = new Intent(VideoPostActivity.this,LoginActivity.class);

        //기본 메뉴 버튼 클릭이벤트
        // 홈 버튼
        악보나라.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(VideoPostActivity.this,MainActivity.class);
                intent_main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_main);
            }
        });
        // 마이페이지 버튼
        button_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_mypage = new Intent(VideoPostActivity.this, MypageActivity.class);
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
                    Intent intent_alarm = new Intent(VideoPostActivity.this, AlarmActivity.class);
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
                Intent intent_sheetmusic = new Intent(VideoPostActivity.this,SheetmusicActivity.class);
                intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_sheetmusic);
            }
        });
        // 영상 커뮤니티 버튼
        icon_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_video = new Intent(VideoPostActivity.this,VideoActivity.class);
                intent_video.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_video);
            }
        });
        // 글 커뮤니티 버튼
        icon_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_community = new Intent(VideoPostActivity.this,CommunityActivity.class);
                intent_community.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_community);
            }
        });
        //--------------------------------------------------------------------------------------------------

        //조회수 체크
        try {
            Log.d("아티클넘버", String.valueOf(ArticleNumber));
            watchingPersonList = thisArticle.getJSONArray("조회한사람");


            //비회원일때
            if(ID_NUMBER==-1){
                //디바이스 SSAID가져오기
                String android_id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
                Log.d("안드로이드 아이디", android_id);
                if(watchingPersonList.length()==0){
                    watchingPersonList.put(android_id);

                    preferences = getSharedPreferences("데이터베이스", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("데이터베이스", DATABASE.toString());
                    editor.commit();
                }
                else {
                    for (int i = 0; i < watchingPersonList.length(); i++) {
                        if (watchingPersonList.get(i).equals(android_id)) {
                            break;
                        }
                        if (i == watchingPersonList.length() - 1) {
                            watchingPersonList.put(android_id);

                            preferences = getSharedPreferences("데이터베이스", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("데이터베이스", DATABASE.toString());
                            editor.commit();
                        }
                    }
                }

            }


            //회원일때
            else {
                if (watchingPersonList.length() == 0) {
                    watchingPersonList.put(ID_NUMBER);

                    preferences = getSharedPreferences("데이터베이스", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("데이터베이스", DATABASE.toString());
                    editor.commit();
                }
                else {
                    for (int i = 0; i < watchingPersonList.length(); i++) {
                        if (watchingPersonList.get(i).equals(ID_NUMBER)) {
                            break;
                        }
                        if (i == watchingPersonList.length() - 1) {
                            watchingPersonList.put(ID_NUMBER);

                            preferences = getSharedPreferences("데이터베이스", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("데이터베이스", DATABASE.toString());
                            editor.commit();
                        }
                    }
                }
            }
            number_of_hits.setText(Integer.toString(watchingPersonList.length()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //댓글 작성 버튼
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    AddComment.setVisibility(View.VISIBLE);
                }
                else{
                    intent_requireLogin.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent_requireLogin.putExtra("액티비티","영상포스트");
                    startActivity(intent_requireLogin);
                }

            }
        });

        //댓글 작성 취소 버튼
        Close_AddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddComment.setVisibility(View.GONE);
            }
        });

        //댓글 등록 버튼
        AddComment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            AddComment.setVisibility(View.GONE);
            string_Comment_Content = Comment_Content.getText().toString();
            Comment_Content.setText("");
            try {
                long time = System.currentTimeMillis();
                JSONArray holymoly = thisArticle.getJSONArray("댓글목록");
                JSONArray newjsonArray = new JSONArray();


                JSONObject a = new JSONObject();

                a.put("아이디넘버",ID_NUMBER);
                a.put("댓글내용",string_Comment_Content);
                a.put("대댓글목록",newjsonArray);
                a.put("좋아요목록",newjsonArray);
                a.put("작성시간",time);
                a.put("더보기불린",false);

                holymoly.put(a);

                //게시물 작성자에게 알림 데이터 등록
                JSONObject replyedClient = array_client.getJSONObject(thisArticle.getInt("아이디넘버"));
                JSONArray haha = replyedClient.getJSONArray("알림목록");
                JSONObject b = new JSONObject();

                String message =  "님이 회원님의 게시물에 댓글을 남기셨습니다.";

                b.put("아이디넘버",ID_NUMBER);
                b.put("게시물타입","video");
                b.put("게시글넘버",ArticleNumber);
                b.put("체크여부",false);
                b.put("알림메세지",message);

                haha.put(b);

                //데이터 저장
                SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("데이터베이스", DATABASE.toString());
                editor.commit();


                if(ID_NUMBER==thisArticle.getInt("아이디넘버"))
                //댓글작성 알림 띄우기
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String FCM_MESSAGE_URL="https://fcm.googleapis.com/fcm/send";
                            String SERVER_KEY= "AAAAYaTeLd0:APA91bHjijFYatJoNxtkE7bLc3tETYpb1Wepw3334akmTWR3a5SajgMxcSyCnvFD3D6TPl2Ks6ZMdKN2ZwfXCvN2aXQBo87oqWuRQ8gyzeD2Iw3G9tFrZADW4lMQpHUK5TOK58rJail4";

                            String message =  currentClient.getString("닉네임")+"님이 회원님의 게시물에 댓글을 남기셨습니다.";
                            // FMC 메시지 생성 start
                            JSONObject root = new JSONObject();
                            JSONObject notification = new JSONObject();
                            notification.put("body", message);
                            notification.put("title", getString(R.string.app_name));
                            root.put("notification", notification);
                            root.put("to", FirebaseInstanceId.getInstance().getToken());
                            Log.d("토큰값", FirebaseInstanceId.getInstance().getToken());
                            // FMC 메시지 생성 end

                            URL Url = new URL(FCM_MESSAGE_URL);
                            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
                            conn.setRequestProperty("Accept", "application/json");
                            conn.setRequestProperty("Content-type", "application/json");
                            OutputStream os = conn.getOutputStream();
                            os.write(root.toString().getBytes("utf-8"));
                            os.flush();
                            conn.getResponseCode();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();




            } catch (JSONException e) {
                e.printStackTrace();
            }
            onResume();
            }
        });

        //좋아요 버튼
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //비회원일때
                if(ID_NUMBER==-1){
                    intent_requireLogin.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent_requireLogin.putExtra("액티비티","영상포스트");
                    startActivity(intent_requireLogin);
                }
                //회원일때
                else {

                    try {
                        JSONArray likeClientList = thisArticle.getJSONArray("좋아요한사람");
                        JSONArray likeList = currentClient.getJSONArray("영상좋아요목록");
                        //좋아요 하지 않았다면
                        if (like_boolean == false) {
                            likeList.put(ArticleNumber);
                            likeClientList.put(ID_NUMBER);
                            like_boolean = true;

                            if(ID_NUMBER==thisArticle.getInt("아이디넘버"))
                            //댓글작성 알림 띄우기
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String FCM_MESSAGE_URL="https://fcm.googleapis.com/fcm/send";
                                        String SERVER_KEY= "AAAAYaTeLd0:APA91bHjijFYatJoNxtkE7bLc3tETYpb1Wepw3334akmTWR3a5SajgMxcSyCnvFD3D6TPl2Ks6ZMdKN2ZwfXCvN2aXQBo87oqWuRQ8gyzeD2Iw3G9tFrZADW4lMQpHUK5TOK58rJail4";

                                        String message =  currentClient.getString("닉네임")+"님이 회원님의 게시물을 좋아합니다.";
                                        // FMC 메시지 생성 start
                                        JSONObject root = new JSONObject();
                                        JSONObject notification = new JSONObject();
                                        notification.put("body", message);
                                        notification.put("title", getString(R.string.app_name));
                                        root.put("notification", notification);
                                        root.put("to", FirebaseInstanceId.getInstance().getToken());
                                        Log.d("토큰값", FirebaseInstanceId.getInstance().getToken());
                                        // FMC 메시지 생성 end

                                        URL Url = new URL(FCM_MESSAGE_URL);
                                        HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                                        conn.setRequestMethod("POST");
                                        conn.setDoOutput(true);
                                        conn.setDoInput(true);
                                        conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
                                        conn.setRequestProperty("Accept", "application/json");
                                        conn.setRequestProperty("Content-type", "application/json");
                                        OutputStream os = conn.getOutputStream();
                                        os.write(root.toString().getBytes("utf-8"));
                                        os.flush();
                                        conn.getResponseCode();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                            //게시물 작성자에게 알림 데이터 등록
                            JSONObject replyedClient = array_client.getJSONObject(thisArticle.getInt("아이디넘버"));
                            JSONArray haha = replyedClient.getJSONArray("알림목록");
                            JSONObject b = new JSONObject();

                            String message = "님이 회원님의 게시물을 좋아합니다.";

                            b.put("아이디넘버",ID_NUMBER);
                            b.put("게시물타입","video");
                            b.put("게시글넘버",ArticleNumber);
                            b.put("체크여부",false);
                            b.put("알림메세지",message);

                            haha.put(b);
                        }
                        //좋아요 취소하는 경우
                        else {
                            for (int i = 0; i < likeList.length(); i++) {
                                if (likeList.get(i).equals(ArticleNumber)) {
                                    likeList.remove(i);
                                    break;
                                }
                            }
                            for (int i = 0; i < likeClientList.length(); i++) {
                                if (likeClientList.get(i).equals(ID_NUMBER)) {
                                    likeClientList.remove(i);
                                    break;
                                }
                            }
                            like_boolean = false;
                            Toast.makeText(VideoPostActivity.this, "좋아요가 취소되었습니다.", Toast.LENGTH_SHORT).show();
                        }



                        SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("데이터베이스", DATABASE.toString());
                        editor.commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    onResume();
                }
            }
        });


        //회원(비회원)일때 수정삭제 기능버튼 보이기 여부
        try {
            if(ID_NUMBER==thisArticle.getInt("아이디넘버")){
                more_btn.setVisibility(View.VISIBLE);
            }
            else{
                more_btn.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //수정삭제 기능 버튼
        more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MOREBUTTON==false) {
                    EditOrDelete_btn.setVisibility(View.VISIBLE);
                    MOREBUTTON=true;
                }
                else{
                    EditOrDelete_btn.setVisibility(View.GONE);
                    MOREBUTTON=false;
                }
            }
        });

        //수정 버튼
        Edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(VideoPostActivity.this,VideoEditActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("악보넘버",ArticleNumber);
                startActivity(intent);
            }
        });

        //삭제버튼
        Delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
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
        try {
            JSONArray holymoly = thisArticle.getJSONArray("댓글목록");
            for(int i =0 ;i<holymoly.length();i++){
                JSONObject a = holymoly.getJSONObject(i);
                a.put("더보기불린",false);

                SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("데이터베이스", DATABASE.toString());
                editor.commit();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //좋아요순 정렬 메소드
    public class LikesDescending implements  Comparator<Item_Comment_Sheetmusic>{
        @Override
        public int compare(Item_Comment_Sheetmusic o1, Item_Comment_Sheetmusic o2) {
            int o1_likes_number,o2_likes_number;
            if(o1.get좋아요목록()==null){
                o1_likes_number = 0;
            }
            else{
                o1_likes_number = o1.get좋아요목록().length();
            }
            if(o2.get좋아요목록()==null){
                o2_likes_number = 0;
            }
            else{
                o2_likes_number = o2.get좋아요목록().length();
            }
            if(o1_likes_number < o2_likes_number)
                return 1;
            else if(o1_likes_number == o2_likes_number)
                return o1.get작성시간() <= o2.get작성시간() ? 1 : -1;
            else{
                return -1;
            }

        }
    }

    //재실행
    public void update(){
        Intent intent_sheetmusic = new Intent(VideoPostActivity.this, VideoPostActivity.class);
        intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent_sheetmusic);
    }

    //좋아요순 정렬 메소드
    public class LikesDescending_jsonobject implements  Comparator<JSONObject>{
        @Override
        public int compare(JSONObject o1, JSONObject o2) {
            int o1_likes_number=0;
            int o2_likes_number=0;
            long o1_upload_time=0;
            long o2_upload_time=0;
            try {
                o1_likes_number = o1.getJSONArray("좋아요목록").length();
                o2_likes_number = o2.getJSONArray("좋아요목록").length();
                o1_upload_time = o1.getLong("작성시간");
                o2_upload_time = o2.getLong("작성시간");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(o1_likes_number < o2_likes_number)
                return 1;
            else if(o1_likes_number == o2_likes_number)
                return o1_upload_time <= o2_upload_time ? 1 : -1;
            else{
                return -1;
            }

        }
    }

    //로그인 필요기능
    public void loginrequireIntent(Intent intent){
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("액티비티","악보포스트");
        startActivity(intent);
    }

    //EDIT 텍스트 포커스 없애기
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }


    //삭제 다이얼로그
    public void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제하시겠습니까?");
        builder.setPositiveButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.setNegativeButton("예",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void onClick(DialogInterface dialog, int which) {


                        JSONObject newjsonObject = new JSONObject();

                        try {
                            newjsonObject.put("아이디넘버",-1);
                            array_Article_Video.put(ArticleNumber,newjsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("데이터베이스", DATABASE.toString());
                        editor.commit();


                        Toast.makeText(VideoPostActivity.this, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(VideoPostActivity.this, VideoActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        finish();

                    }
                });
        builder.show();



    }




}

