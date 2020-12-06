package com.jun.akbonara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MypageActivity_like extends AppCompatActivity {

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

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Adapter_SheetmusicActivity SheetmusicAdapter;
    private Adapter_VideoActivity adapter_videoActivity;
    private Adapter_CommunityActivity adapter_communityActivity;

    List<Item_SheetmusicActivity> SheetmusicArticleInfo = new ArrayList<>();
    List<Item_VideoActivity> VideoArticleList = new ArrayList<>();
    List<Item_CommunityActivity> CommunityArticleList = new ArrayList<>();

    int Filter_Category;

    TextView Sheetmusic_like_fliter;
    TextView Video_like_fliter;
    TextView Community_like_fliter;

    TextView NoLikePostMSG;


    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_like);  //layout xml 매칭

        //기본 메뉴 주소 매칭
        button_mypage = findViewById(R.id.button_mypage);
        button_alarm =findViewById(R.id.button_alarm);
        악보나라 = findViewById(R.id.악보나라);
        icon_music = findViewById(R.id.icon_music);
        icon_video = findViewById(R.id.icon_video);
        icon_community = findViewById(R.id.icon_community);
        //--------------------------------------------------------------------------------------------------


        Sheetmusic_like_fliter = findViewById(R.id.Sheetmusic_like_fliter);
        Video_like_fliter = findViewById(R.id.Video_like_fliter);
        Community_like_fliter = findViewById(R.id.Community_like_fliter);

        NoLikePostMSG = findViewById (R.id. NoLikePostMSG );



        //리싸이클러 뷰

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView_sheetmusic);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Filter_Category=0;
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
            array_Article_Community = DATABASE.getJSONArray("커뮤니티글정보");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences loadShared = getSharedPreferences("쉐어드데이터",MODE_PRIVATE);

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



        //카테고리 선택되었을때 색깔 변경
        if(Filter_Category==0){
            Sheetmusic_like_fliter.setTextColor(Color.MAGENTA);
            Video_like_fliter.setTextColor(Color.GRAY);
            Community_like_fliter.setTextColor(Color.GRAY);
        }
        else if(Filter_Category==1){
            Sheetmusic_like_fliter.setTextColor(Color.GRAY);
            Video_like_fliter.setTextColor(Color.MAGENTA);
            Community_like_fliter.setTextColor(Color.GRAY);
        }
        else if(Filter_Category==2){
            Sheetmusic_like_fliter.setTextColor(Color.GRAY);
            Video_like_fliter.setTextColor(Color.GRAY);
            Community_like_fliter.setTextColor(Color.MAGENTA);
        }




        //로그인 안되었을때 로그인 창으로 이동
        final Intent intent_requireLogin = new Intent(MypageActivity_like.this,LoginActivity.class);

        //기본 메뉴 버튼 클릭이벤트
        // 홈 버튼
        악보나라.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(MypageActivity_like.this,MainActivity.class);
                intent_main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_main);
            }
        });
        // 마이페이지 버튼
        button_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_mypage = new Intent(MypageActivity_like.this, MypageActivity.class);
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
                    Intent intent_alarm = new Intent(MypageActivity_like.this, AlarmActivity.class);
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
                Intent intent_sheetmusic = new Intent(MypageActivity_like.this,SheetmusicActivity.class);
                intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_sheetmusic);
            }
        });
        // 영상 커뮤니티 버튼
        icon_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_video = new Intent(MypageActivity_like.this,VideoActivity.class);
                intent_video.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_video);
            }
        });
        // 글 커뮤니티 버튼
        icon_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_community = new Intent(MypageActivity_like.this,CommunityActivity.class);
                intent_community.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_community);
            }
        });
        //--------------------------------------------------------------------------------------------------

        // 악보 좋아요된것만 걸르기
        if(Filter_Category==0){

            //리사이클러뷰 초기화
            SheetmusicArticleInfo = new ArrayList<>();

            try {
                JSONObject thisclient  = (JSONObject) array_client.get(ID_NUMBER);
                JSONArray likeSheetmusicList =  thisclient.getJSONArray("좋아요목록");
                JSONArray arraySheetmusicArticleInfo =  DATABASE.getJSONArray("악보글정보");

                for(int i=0 , j = likeSheetmusicList.length(); i <j ;i++){
                    JSONObject obj =  arraySheetmusicArticleInfo.getJSONObject(likeSheetmusicList.getInt(i));
                    if(obj.getInt("아이디넘버")!=-1) {
                        AddItemToList(obj);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(SheetmusicArticleInfo.size()==0 ){
                NoLikePostMSG.setText("좋아요한 게시글이 없습니다");
                NoLikePostMSG.setVisibility(View.VISIBLE);
            }
            else{
                NoLikePostMSG.setVisibility(View.GONE);
            }

            // specify an adapter (see also next example)
            // 리싸이클러뷰 구현
            SheetmusicAdapter = new Adapter_SheetmusicActivity(SheetmusicArticleInfo);
            recyclerView.setAdapter(SheetmusicAdapter);
        }


        // 영상 좋아요된것만 걸르기
        else if(Filter_Category==1){

            //리사이클러뷰 초기화
            VideoArticleList = new ArrayList<>();

            try {
                JSONObject thisclient  = (JSONObject) array_client.get(ID_NUMBER);
                JSONArray likeList =  thisclient.getJSONArray("영상좋아요목록");

                for(int i=0 , j = likeList.length(); i <j ;i++){
                    JSONObject obj =  array_Article_Video.getJSONObject(likeList.getInt(i));
                    if(obj.getInt("아이디넘버")!=-1) {
                        Item_VideoActivity item_videoActivity = new Item_VideoActivity(
                                obj.getString("글제목"),obj.getInt("아이디넘버"),obj.getString("동영상주소"),
                                obj.getString("글"),obj.getLong("업로드시간"),obj.getJSONArray("조회한사람"),
                                obj.getJSONArray("좋아요한사람"),obj.getJSONArray("댓글목록"));
                        VideoArticleList.add(item_videoActivity);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if( VideoArticleList.size()==0 ){
                NoLikePostMSG.setText("좋아요한 게시글이 없습니다");
                NoLikePostMSG.setVisibility(View.VISIBLE);
            }
            else{
                NoLikePostMSG.setVisibility(View.GONE);
            }

            // specify an adapter (see also next example)
            // 리싸이클러뷰 구현
            adapter_videoActivity = new Adapter_VideoActivity(VideoArticleList,DATABASE,this,loadShared);
            recyclerView.setAdapter(adapter_videoActivity);
        }

        // 커뮤니티 좋아요된것만 걸르기
        else if(Filter_Category==2){

            //리사이클러뷰 초기화
            CommunityArticleList = new ArrayList<>();

            try {
                JSONObject thisclient  = (JSONObject) array_client.get(ID_NUMBER);
                JSONArray likeList =  thisclient.getJSONArray("글좋아요목록");

                for(int i=0 , j = likeList.length(); i <j ;i++){
                    JSONObject obj =  array_Article_Community.getJSONObject(likeList.getInt(i));
                    if(obj.getInt("아이디넘버")!=-1) {
                        Item_CommunityActivity item_communityActivity = new Item_CommunityActivity(
                                obj.getString("글제목"),obj.getInt("아이디넘버"),
                                obj.getString("글"),obj.getLong("업로드시간"),obj.getJSONArray("조회한사람"),
                                obj.getJSONArray("좋아요한사람"),obj.getJSONArray("댓글목록"));
                        CommunityArticleList.add(item_communityActivity);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(CommunityArticleList.size()==0){
                NoLikePostMSG.setText("좋아요한 게시글이 없습니다");
                NoLikePostMSG.setVisibility(View.VISIBLE);
            }
            else{
                NoLikePostMSG.setVisibility(View.GONE);
            }

            // specify an adapter (see also next example)
            // 리싸이클러뷰 구현
            adapter_communityActivity = new Adapter_CommunityActivity(CommunityArticleList,DATABASE);
            recyclerView.setAdapter(adapter_communityActivity);
        }


        //아이템 클릭 부분
        if(Filter_Category==0){
            //악보 목록
            SheetmusicAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                    Intent intent = new Intent(MypageActivity_like.this,SheetmusicPostActivity.class);
                    intent.putExtra("글제목",SheetmusicArticleInfo.get(position).get글제목());
                    startActivity(intent);
                }
            });
        }
        else if(Filter_Category==1){
            //영상 목록
            adapter_videoActivity.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                    Intent intent = new Intent(MypageActivity_like.this,VideoPostActivity.class);
                    intent.putExtra("글제목",VideoArticleList.get(position).get글제목());
                    startActivity(intent);
                }
            });
        }
        else if(Filter_Category==2){
            //커뮤니티 목록
            adapter_communityActivity.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                    Intent intent = new Intent(MypageActivity_like.this,CommunityPostActivity.class);
                    intent.putExtra("글제목",CommunityArticleList.get(position).get글제목());
                    startActivity(intent);
                }
            });

        }


        //악보버튼 눌렀을때
        Sheetmusic_like_fliter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Category =0;
                onResume();
            }
        });

        //영상버튼 눌렀을때
        Video_like_fliter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Category =1;
                onResume();
            }
        });

        //글 버튼 눌렀을때
        Community_like_fliter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Category =2;
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void AddItemToList(JSONObject obj) throws JSONException {
        Item_SheetmusicActivity SheetmusicArticleData = new Item_SheetmusicActivity();
        SheetmusicArticleData.set프로필(obj.getString("프로필"));
        SheetmusicArticleData.set닉네임(obj.getString("닉네임"));
        SheetmusicArticleData.set글제목(obj.getString("글제목"));
        SheetmusicArticleData.set업로드시간(obj.getLong("업로드시간"));
        SheetmusicArticleData.set가격(obj.getInt("가격"));
        SheetmusicArticleData.set조회수(obj.getInt("조회수"));
        SheetmusicArticleData.set좋아요수(obj.getInt("좋아요수"));
        SheetmusicArticleData.set댓글수(obj.getInt("댓글수"));
        SheetmusicArticleInfo.add(SheetmusicArticleData);
    }

}

