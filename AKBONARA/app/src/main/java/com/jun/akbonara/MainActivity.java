package com.jun.akbonara;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Bitmap mBitmap;


    private RecyclerView recyclerView;
    private Adapter_main_item_sheetmusic mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private RecyclerView RecyclerView_main_video;
    private Adapter_main_item_video mAdapter_video;
    private RecyclerView.LayoutManager layoutManager_video;

    private RecyclerView RecyclerView_main_community;
    private Adapter_main_item_community mAdapter_community;
    private RecyclerView.LayoutManager layoutManager_community;



    LinearLayout Linear_loginstate;



    TextView charge,free;
    int Filter_Price;
    int Filter_More;
    int Filter_More_Post;
    int howmany;

    Button expand,postexpand;

    List<Item_SheetmusicActivity> SheetmusicArticleInfo = new ArrayList<>();
    List<Item_VideoActivity> VideoArticleList = new ArrayList<>();
    List<Item_CommunityActivity> CommunityArticleList = new ArrayList<>();

    //기본 메뉴 멤버 변수 선언
    ImageView button_mypage, button_alarm, icon_music ,icon_video ,icon_community;
    TextView 악보나라;
    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONArray array_Article_Sheetmusic = new JSONArray();
    JSONArray array_Article_Video = new JSONArray();
    JSONArray array_Article_Community = new JSONArray();

    SharedPreferences preferences;
    SharedPreferences.Editor editor;



    int ID_NUMBER;


    //main
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  //layout xml 매칭





        //리싸이클러 뷰 - 악보

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView_main_sheetmusic);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);




        //리싸이클러 뷰 - 영상

        RecyclerView_main_video = (RecyclerView) findViewById(R.id.RecyclerView_main_video);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        RecyclerView_main_video.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager_video = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView_main_video.setLayoutManager(layoutManager_video);



        //리싸이클러 뷰 - 글

        RecyclerView_main_community = (RecyclerView) findViewById(R.id.RecyclerView_main_community);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        RecyclerView_main_community.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager_community = new LinearLayoutManager(this);
        RecyclerView_main_community.setLayoutManager(layoutManager_community);





//--------------------------------------------------------------------------------------------------------------------
        //기본 메뉴 주소 매칭
        button_mypage = findViewById(R.id.button_mypage);
        button_alarm =findViewById(R.id.button_alarm);
        악보나라 = findViewById(R.id.악보나라);
        icon_music = findViewById(R.id.icon_music);
        icon_video = findViewById(R.id.icon_video);
        icon_community = findViewById(R.id.icon_community);

        Linear_loginstate = findViewById(R.id.Linear_loginstate);

        charge = findViewById(R.id.charge);
        free = findViewById(R.id.free);
        expand = findViewById(R.id.expand);
        postexpand = findViewById(R.id.postexpand);




        Log.i("메인","메인  onCreate");


    }



    @Override
    protected void onStart() {
        super.onStart();
        //가격 부분 유료로 초기 셋팅
        Filter_Price=0;
        // 더보기 부분 초기화
        Filter_More =0 ;
        Filter_More_Post =0;


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();


        //데이터 베이스 불러오기
        preferences = getSharedPreferences("데이터베이스",0);
        editor = preferences.edit();
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


//        //데이터 초기화
//        try {
//            for(int i=0;i<array_client.length();i++) {
//                JSONArray jsonArray = new JSONArray();
//                JSONObject holy = (JSONObject) array_client.get(i);
//                holy.put("프로필사진","");
//                holy.put("좋아요목록",jsonArray);
//                holy.put("구매한악보",jsonArray);
//                DATABASE.put("악보글정보",jsonArray);
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


//        //내역 초기화
//        try {
//            for(int i=0;i<array_client.length();i++) {
//                JSONArray jsonArray = new JSONArray();
//                JSONObject holy = (JSONObject) array_client.get(i);
//                if(i==1 || i==3){
//                    holy.put("캐시",10000);
//                }
//                else{
//                    holy.put("캐시",0);
//                }
//                holy.put("캐시내역",jsonArray);
//                holy.put("구매한악보",jsonArray);
//                holy.put("알림목록",jsonArray);
//
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
//
////       //구매수 초기화
//        try {
//            for(int i=0;i<array_Article_Sheetmusic.length();i++) {
//                JSONObject holy = (JSONObject) array_Article_Sheetmusic.get(i);
//                holy.put("구매수",0);
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





        //더보기 버튼 선택되었을때 아이콘 변경
        switch (Filter_More){
            case 0 : expand.setVisibility(View.VISIBLE);break;
            case 1 : expand.setVisibility(View.INVISIBLE);break;
        }
        //더보기 버튼 선택되었을때 아이콘 변경
        switch (Filter_More_Post){
            case 0 : postexpand.setVisibility(View.VISIBLE);break;
            case 1 : postexpand.setVisibility(View.INVISIBLE);break;
        }



        // 가격필터 선택되었을때 색깔 변경
        if(Filter_Price==0){
            charge.setTextColor(Color.MAGENTA);
            free.setTextColor(Color.GRAY);
        }
        else if(Filter_Price==1){
            charge.setTextColor(Color.GRAY);
            free.setTextColor(Color.MAGENTA);
        }



        //악보 리싸이클러뷰------------------------------------------
        //리사이클러뷰 초기화
        SheetmusicArticleInfo = new ArrayList<>();

        //가격 유료 필터 TOP 3
        if(Filter_Price==0){
            try {
                JSONArray arraySheetmusicArticleInfo = DATABASE.getJSONArray("악보글정보");

                for (int i = 0, j = arraySheetmusicArticleInfo.length(); i < j; i++) {
                    JSONObject obj = arraySheetmusicArticleInfo.getJSONObject(i);
                    if(obj.getInt("아이디넘버")!=-1) {
                        if (obj.getInt("가격") > 0) {
                            AddItemToList(obj);
                            SheetmusicArticleInfo.sort(new HitsDescending());
                        }
                        switch (Filter_More) {
                            case 0:
                                howmany = 3;
                                break;
                            case 1:
                                howmany = 5;
                                break;
                        }
                        while (SheetmusicArticleInfo.size() > howmany) {
                            SheetmusicArticleInfo.remove(SheetmusicArticleInfo.size() - 1);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        //가격 무료 필터
        else if(Filter_Price==1){
            try {
                JSONArray arraySheetmusicArticleInfo =  DATABASE.getJSONArray("악보글정보");

                for(int i=0 , j = arraySheetmusicArticleInfo.length(); i <j ;i++){
                    JSONObject obj =  arraySheetmusicArticleInfo.getJSONObject(i);
                    if(obj.getInt("아이디넘버")!=-1) {
                        if (obj.getInt("가격") == 0) {
                            AddItemToList(obj);
                            SheetmusicArticleInfo.sort(new HitsDescending());
                        }
                        switch (Filter_More) {
                            case 0:
                                howmany = 3;
                                break;
                            case 1:
                                howmany = 5;
                                break;
                        }
                        while (SheetmusicArticleInfo.size() > howmany) {
                            SheetmusicArticleInfo.remove(SheetmusicArticleInfo.size() - 1);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //리싸이클러 뷰 구현
        mAdapter = new Adapter_main_item_sheetmusic(SheetmusicArticleInfo);
        recyclerView.setAdapter(mAdapter);


        //비디오 리싸이클러뷰-----------------------------------------------------------------
        //리사이클러뷰 초기화
        VideoArticleList = new ArrayList<>();

        try{
            for (int i = 0, j = array_Article_Video.length(); i < j; i++) {
                JSONObject obj = array_Article_Video.getJSONObject(i);
                if(obj.getInt("아이디넘버")!=-1) {
                    AddItemToList_video(obj);
                    VideoArticleList.sort(new HitsDescending_video());
                }
            }
            while (VideoArticleList.size() > 5) {
                VideoArticleList.remove(VideoArticleList.size() - 1);
            }

        }catch (Exception e){

        }

        //리싸이클러 뷰 구현
        mAdapter_video = new Adapter_main_item_video(VideoArticleList,DATABASE,this,loadShared);
        RecyclerView_main_video.setAdapter(mAdapter_video);




        //글 리싸이클러뷰------------------------------------------
        //리사이클러뷰 초기화
        CommunityArticleList = new ArrayList<>();

        try {
            for (int i = 0, j = array_Article_Community.length(); i < j; i++) {
                JSONObject obj = array_Article_Community.getJSONObject(i);
                if(obj.getInt("아이디넘버")!=-1) {

                    AddItemToList_community(obj);
                    CommunityArticleList.sort(new HitsDescending_community());

                    switch (Filter_More_Post) {
                        case 0:
                            howmany = 3;
                            break;
                        case 1:
                            howmany = 5;
                            break;
                    }
                    while (CommunityArticleList.size() > howmany) {
                        CommunityArticleList.remove(CommunityArticleList.size() - 1);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //리싸이클러 뷰 구현
        mAdapter_community = new Adapter_main_item_community(CommunityArticleList,DATABASE);
        RecyclerView_main_community.setAdapter(mAdapter_community);



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

        //체크해보자!!
        try {
            JSONObject 현재회원 = array_client.getJSONObject(ID_NUMBER);
            JSONArray checkpurchaseList = 현재회원.getJSONArray("구매한악보");
            Log.d("구매한 악보 리스트", String.valueOf(checkpurchaseList));
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

        //베스트 악보 유료 버튼
        charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Price = 0;
                Filter_More =0;
                update();

            }
        });
        //베스트 악보 무료 버튼
        free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Price = 1;
                Filter_More =0;
                update();
            }
        });

        //더보기 버튼
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_More=1;
                update();
            }
        });
        //더보기 버튼
        postexpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_More_Post=1;
                update();
            }
        });


        //아이템 클릭
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this,SheetmusicPostActivity.class);
                intent.putExtra("글제목",SheetmusicArticleInfo.get(position).get글제목());
                startActivity(intent);
            }
        });

        //아이템 클릭
        mAdapter_video.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(MainActivity.this,VideoPostActivity.class);
                intent.putExtra("글제목",VideoArticleList.get(position).get글제목());
                startActivity(intent);
            }
        });

        mAdapter_community.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(MainActivity.this,CommunityPostActivity.class);
                intent.putExtra("글제목",CommunityArticleList.get(position).get글제목());
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
        Log.i("메인","메인  onDestroy");

        try {
            DATABASE.put("로그인정보",-1);
            editor.putString("데이터베이스", DATABASE.toString());
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //조회순 정렬 메소드
    public class HitsDescending implements Comparator<Item_SheetmusicActivity> {
        @Override
        public int compare(Item_SheetmusicActivity o1, Item_SheetmusicActivity o2) {
            if(o1.get조회수() < o2.get조회수())
                return 1;
            else if(o1.get조회수()==o2.get조회수())
                return o1.get업로드시간() <= o2.get업로드시간() ? 1 : -1;
            else{
                return -1;
            }

        }
    }

    //재실행 메소드
    public void update(){
        Intent intent_sheetmusic = new Intent(MainActivity.this,MainActivity.class);
        intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent_sheetmusic);
    }

    //리싸이클러뷰 아이템 추가 메소드
    public void AddItemToList(JSONObject obj) throws JSONException {
        Item_SheetmusicActivity SheetmusicArticleData = new Item_SheetmusicActivity();
        SheetmusicArticleData.set닉네임(obj.getString("닉네임"));
        SheetmusicArticleData.set글제목(obj.getString("글제목"));
        SheetmusicArticleData.set가격(obj.getInt("가격"));
        SheetmusicArticleData.set조회수(obj.getInt("조회수"));
        SheetmusicArticleData.set업로드시간(obj.getLong("업로드시간"));
        SheetmusicArticleInfo.add(SheetmusicArticleData);
    }


    //조회순 정렬 메소드
    public class HitsDescending_video implements Comparator<Item_VideoActivity> {
        @Override
        public int compare(Item_VideoActivity o1, Item_VideoActivity o2) {
            if(o1.get조회한사람().length() < o2.get조회한사람().length())
                return 1;
            else if(o1.get조회한사람().length()==o2.get조회한사람().length())
                return o1.get업로드시간() <= o2.get업로드시간() ? 1 : -1;
            else{
                return -1;
            }

        }
    }

    //리싸이클러뷰 아이템 추가 메소드
    public void AddItemToList_video(JSONObject obj) throws JSONException {
        Item_VideoActivity item_videoActivity = new Item_VideoActivity(
                obj.getString("글제목"),obj.getInt("아이디넘버"),obj.getString("동영상주소"),
                obj.getString("글"),obj.getLong("업로드시간"),obj.getJSONArray("조회한사람"),
                obj.getJSONArray("좋아요한사람"),obj.getJSONArray("댓글목록"));
        VideoArticleList.add(item_videoActivity);
    }

    //조회순 정렬 메소드
    public class HitsDescending_community implements Comparator<Item_CommunityActivity> {
        @Override
        public int compare(Item_CommunityActivity o1, Item_CommunityActivity o2) {
            if(o1.get조회한사람().length() < o2.get조회한사람().length())
                return 1;
            else if(o1.get조회한사람().length()==o2.get조회한사람().length())
                return o1.get업로드시간() <= o2.get업로드시간() ? 1 : -1;
            else{
                return -1;
            }

        }
    }

    //리싸이클러뷰 아이템 추가 메소드
    public void AddItemToList_community(JSONObject obj) throws JSONException {
        Item_CommunityActivity item_communityActivity = new Item_CommunityActivity(
                obj.getString("글제목"),obj.getInt("아이디넘버"),
                obj.getString("글"),obj.getLong("업로드시간"),obj.getJSONArray("조회한사람"),
                obj.getJSONArray("좋아요한사람"),obj.getJSONArray("댓글목록"));
        CommunityArticleList.add(item_communityActivity);
    }















}

