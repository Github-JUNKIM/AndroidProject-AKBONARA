package com.jun.akbonara;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
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

public class SheetmusicActivity extends AppCompatActivity {

    private static final String TAG = "SheetmusicActivity";

    //기본 메뉴 멤버 변수 선언
    ImageView button_mypage, button_alarm, icon_music ,icon_video ,icon_community;
    TextView 악보나라;
    int ID_NUMBER;
    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONArray array_Article_Sheetmusic = new JSONArray();
    JSONArray array_Article_Video = new JSONArray();
    //--------------------------------------------------------------------------

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Adapter_SheetmusicActivity mAdapter;

    List<Item_SheetmusicActivity> SheetmusicArticleInfo = new ArrayList<>();

    TextView category_new,category_top100,category_genre;
    TextView upload_button;
    TextView PriceFilter_all,PriceFilter_charge,PriceFilter_free;
    TextView genre_btn_ALL,genre_btn_KPOP,genre_btn_POP,genre_btn_NewAge,genre_btn_Classic,genre_btn_Jazz,genre_btn_MultiHand,genre_btn_GameAni,genre_btn_DramaMovie,genre_btn_Kids,genre_btn_BGM,genre_btn_CCM,genre_btn_Carol;

    HorizontalScrollView scrollbar_genre;

    EditText searchbar;
    ImageView search_button,search_init_button;

    int Filter_Search,Filter_Category,Filter_Price;
    String Filter_Genre;

    String searchinput="";




    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheetmusic);  //layout xml 매칭

        //기본 메뉴 주소 매칭
        button_mypage = findViewById(R.id.button_mypage);
        button_alarm =findViewById(R.id.button_alarm);
        악보나라 = findViewById(R.id.악보나라);
        icon_music = findViewById(R.id.icon_music);
        icon_video = findViewById(R.id.icon_video);
        icon_community = findViewById(R.id.icon_community);
        //--------------------------------------------------------------------------

        upload_button =findViewById(R.id.upload_button);
        category_new = findViewById(R.id.category_new);
        category_top100 = findViewById(R.id.category_top100);
        category_genre = findViewById(R.id.category_genre);


        PriceFilter_all = findViewById(R.id.PriceFilter_all);
        PriceFilter_charge = findViewById(R.id.PriceFilter_charge);
        PriceFilter_free = findViewById(R.id.PriceFilter_free);


        genre_btn_ALL = findViewById(R.id.genre_btn_ALL);
        genre_btn_BGM = findViewById(R.id.genre_btn_BGM);
        genre_btn_Carol = findViewById(R.id.genre_btn_Carol);
        genre_btn_CCM = findViewById(R.id.genre_btn_CCM);
        genre_btn_Classic = findViewById(R.id.genre_btn_Classic);
        genre_btn_DramaMovie = findViewById(R.id.genre_btn_DramaMovie);
        genre_btn_GameAni = findViewById(R.id.genre_btn_GameAni);
        genre_btn_Jazz = findViewById(R.id.genre_btn_Jazz);
        genre_btn_Kids = findViewById(R.id.genre_btn_Kids);
        genre_btn_KPOP = findViewById(R.id.genre_btn_KPOP);
        genre_btn_MultiHand = findViewById(R.id.genre_btn_MultiHand);
        genre_btn_NewAge = findViewById(R.id.genre_btn_NewAge);
        genre_btn_POP = findViewById(R.id.genre_btn_POP);

        scrollbar_genre = findViewById(R.id.scrollbar_genre);

        searchbar = findViewById(R.id.searchbar);
        search_button = findViewById(R.id.search_button);
        search_init_button = findViewById(R.id.search_init_button);


        //리싸이클러 뷰

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView_sheetmusic);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        Filter_Category =0;
        Filter_Price=0;
        Filter_Genre="전체";




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

//        //좋아요리스트 초기화
//        try {
//            JSONArray newJsonarray = new JSONArray();
//            for(int i=0;i<array_client.length();i++) {
//                JSONObject holy = (JSONObject) array_client.get(i);
//                holy.remove("좋아요목록");
//                holy.remove("영상좋아요목록");
//                holy.remove("글좋아요목록");
//                holy.put("좋아요목록",newJsonarray);
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



        //장르 선택되었을때 장르 목록 띄우기
        if(Filter_Category==2){
            scrollbar_genre.setVisibility(View.VISIBLE);
        }
        else {
            scrollbar_genre.setVisibility(View.GONE);
        }

        //카테고리 선택되었을때 색깔 변경
        if(Filter_Category==0){
            category_new.setTextColor(Color.MAGENTA);
            category_top100.setTextColor(Color.GRAY);
            category_genre.setTextColor(Color.GRAY);
        }
        else if(Filter_Category==1){
            category_new.setTextColor(Color.GRAY);
            category_top100.setTextColor(Color.MAGENTA);
            category_genre.setTextColor(Color.GRAY);
        }
        else if(Filter_Category==2){
            category_new.setTextColor(Color.GRAY);
            category_top100.setTextColor(Color.GRAY);
            category_genre.setTextColor(Color.MAGENTA);
        }

        //장르 선택되었을때 색깔 변경
        genrebtnInitialize();
        switch (Filter_Genre){
            case "전체" : genre_btn_ALL.setTextColor(Color.MAGENTA);break;
            case "K-POP" : genre_btn_KPOP.setTextColor(Color.MAGENTA);break;
            case "POP" : genre_btn_POP.setTextColor(Color.MAGENTA);break;
            case "뉴에이지" : genre_btn_NewAge.setTextColor(Color.MAGENTA);break;
            case "클래식" : genre_btn_Classic.setTextColor(Color.MAGENTA);break;
            case "재즈" : genre_btn_Jazz.setTextColor(Color.MAGENTA);break;
            case "연탄곡" : genre_btn_MultiHand.setTextColor(Color.MAGENTA);break;
            case "게임/애니" : genre_btn_GameAni.setTextColor(Color.MAGENTA);break;
            case "드라마/영화" : genre_btn_DramaMovie.setTextColor(Color.MAGENTA);break;
            case "동요" : genre_btn_Kids.setTextColor(Color.MAGENTA);break;
            case "BGM" : genre_btn_BGM.setTextColor(Color.MAGENTA);break;
            case "CCM" : genre_btn_CCM.setTextColor(Color.MAGENTA);break;
            case "캐롤" : genre_btn_Carol.setTextColor(Color.MAGENTA);break;

        }


        //가격필터 선택되었을때 색깔 변경
        if(Filter_Price==0){
            PriceFilter_all.setTextColor(Color.MAGENTA);
            PriceFilter_charge.setTextColor(Color.GRAY);
            PriceFilter_free.setTextColor(Color.GRAY);
        }
        else if(Filter_Price==1){
            PriceFilter_all.setTextColor(Color.GRAY);
            PriceFilter_charge.setTextColor(Color.MAGENTA);
            PriceFilter_free.setTextColor(Color.GRAY);
        }
        else if(Filter_Price==2){
            PriceFilter_all.setTextColor(Color.GRAY);
            PriceFilter_charge.setTextColor(Color.GRAY);
            PriceFilter_free.setTextColor(Color.MAGENTA);
        }

        //리사이클러뷰 초기화
        SheetmusicArticleInfo = new ArrayList<>();

        //NEW 필터와 가격 전체
        if(Filter_Category==0 && Filter_Price==0){

            try {
                JSONArray arraySheetmusicArticleInfo =  DATABASE.getJSONArray("악보글정보");

                for(int i=0 , j = arraySheetmusicArticleInfo.length(); i <j ;i++){
                    JSONObject obj =  arraySheetmusicArticleInfo.getJSONObject(i);
                    if(obj.getInt("아이디넘버")!=-1 && obj.getString("글제목").contains(searchinput)) {
                        AddItemToList(obj);
                        SheetmusicArticleInfo.sort(new TimeDescending());
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //NEW 필터와 가격 유료
        else if(Filter_Category==0 && Filter_Price==1){

            try {
                JSONArray arraySheetmusicArticleInfo =  DATABASE.getJSONArray("악보글정보");

                for(int i=0 , j = arraySheetmusicArticleInfo.length(); i <j ;i++){
                    JSONObject obj =  arraySheetmusicArticleInfo.getJSONObject(i);
                    if(obj.getInt("아이디넘버")!=-1 && obj.getString("글제목").contains(searchinput)) {
                        if (obj.getInt("가격") > 0) {
                            AddItemToList(obj);
                            SheetmusicArticleInfo.sort(new TimeDescending());
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //NEW 필터와 가격 무료
        else if(Filter_Category==0 && Filter_Price==2){
            try {
                JSONArray arraySheetmusicArticleInfo =  DATABASE.getJSONArray("악보글정보");

                for(int i=0 , j = arraySheetmusicArticleInfo.length(); i <j ;i++){
                    JSONObject obj =  arraySheetmusicArticleInfo.getJSONObject(i);
                    if(obj.getInt("아이디넘버")!=-1 && obj.getString("글제목").contains(searchinput)) {
                        if (obj.getInt("가격") == 0) {
                            AddItemToList(obj);
                            SheetmusicArticleInfo.sort(new TimeDescending());
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //-TOP 100------------------------------------------------
        else if(Filter_Category==1 && Filter_Price==0){
            try {
                JSONArray arraySheetmusicArticleInfo =  DATABASE.getJSONArray("악보글정보");

                for(int i=0 , j = arraySheetmusicArticleInfo.length(); i <j ;i++){
                    JSONObject obj =  arraySheetmusicArticleInfo.getJSONObject(i);
                    //삭제된 글은 아이디넘버 =-1 임
                    if(obj.getInt("아이디넘버")!=-1 && obj.getString("글제목").contains(searchinput)) {
                        AddItemToList(obj);
                        SheetmusicArticleInfo.sort(new HitsDescending());
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(Filter_Category==1 && Filter_Price==1){

            try {
                JSONArray arraySheetmusicArticleInfo =  DATABASE.getJSONArray("악보글정보");

                for(int i=0 , j = arraySheetmusicArticleInfo.length(); i <j ;i++){
                    JSONObject obj =  arraySheetmusicArticleInfo.getJSONObject(i);
                    if(obj.getInt("아이디넘버")!=-1 && obj.getString("글제목").contains(searchinput)) {
                        if (obj.getInt("가격") > 0) {
                            AddItemToList(obj);
                            SheetmusicArticleInfo.sort(new HitsDescending());
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(Filter_Category==1 && Filter_Price==2){
            try {
                JSONArray arraySheetmusicArticleInfo =  DATABASE.getJSONArray("악보글정보");

                for(int i=0 , j = arraySheetmusicArticleInfo.length(); i <j ;i++){
                    JSONObject obj =  arraySheetmusicArticleInfo.getJSONObject(i);
                    if(obj.getInt("아이디넘버")!=-1 && obj.getString("글제목").contains(searchinput)) {
                        if (obj.getInt("가격") == 0) {
                            AddItemToList(obj);
                            SheetmusicArticleInfo.sort(new HitsDescending());
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //-장르 카테고리 필터링-------------------------------------------------
        else if(Filter_Category==2 && Filter_Genre.equals("전체")){
                try {
                    JSONArray arraySheetmusicArticleInfo = DATABASE.getJSONArray("악보글정보");

                    for (int i = 0, j = arraySheetmusicArticleInfo.length(); i < j; i++) {
                        JSONObject obj = arraySheetmusicArticleInfo.getJSONObject(i);
                        if(obj.getInt("아이디넘버")!=-1 && obj.getString("글제목").contains(searchinput)) {
                            if (Filter_Price == 0) {
                                AddItemToList(obj);
                                SheetmusicArticleInfo.sort(new TimeDescending());
                            } else if (Filter_Price == 1 && obj.getInt("가격") > 0) {
                                AddItemToList(obj);
                                SheetmusicArticleInfo.sort(new TimeDescending());
                            } else if (Filter_Price == 2 && obj.getInt("가격") == 0) {
                                AddItemToList(obj);
                                SheetmusicArticleInfo.sort(new TimeDescending());
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        else if(Filter_Category==2 && !Filter_Genre.equals("전체")){
            try {
                JSONArray arraySheetmusicArticleInfo =  DATABASE.getJSONArray("악보글정보");

                for(int i=0 , j = arraySheetmusicArticleInfo.length(); i <j ;i++){
                    JSONObject obj =  arraySheetmusicArticleInfo.getJSONObject(i);
                    if(obj.getInt("아이디넘버")!=-1 && obj.getString("글제목").contains(searchinput)) {
                        if (Filter_Price == 0 && obj.getString("장르").equals(Filter_Genre)) {
                            AddItemToList(obj);
                            SheetmusicArticleInfo.sort(new TimeDescending());
                        } else if (Filter_Price == 1 && obj.getString("장르").equals(Filter_Genre) && obj.getInt("가격") > 0) {
                            AddItemToList(obj);
                            SheetmusicArticleInfo.sort(new TimeDescending());
                        } else if (Filter_Price == 2 && obj.getString("장르").equals(Filter_Genre) && obj.getInt("가격") == 0) {
                            AddItemToList(obj);
                            SheetmusicArticleInfo.sort(new TimeDescending());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        // specify an adapter (see also next example)
        // 리싸이클러뷰 구현
        mAdapter = new Adapter_SheetmusicActivity(SheetmusicArticleInfo);
        Log.d(TAG, "onResume: mAapter 생성 ");
        recyclerView.setAdapter(mAdapter);
        Log.d(TAG, "onResume: setAdapter ");


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
        final Intent intent_requireLogin = new Intent(SheetmusicActivity.this,LoginActivity.class);

        //기본 메뉴 버튼 클릭이벤트
        // 홈 버튼
        악보나라.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(SheetmusicActivity.this,MainActivity.class);
                intent_main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_main);
            }
        });
        // 마이페이지 버튼
        button_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_mypage = new Intent(SheetmusicActivity.this, MypageActivity.class);
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
                    Intent intent_alarm = new Intent(SheetmusicActivity.this, AlarmActivity.class);
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
                Intent intent_sheetmusic = new Intent(SheetmusicActivity.this,SheetmusicActivity.class);
                intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_sheetmusic);
            }

        });
        // 영상 커뮤니티 버튼
        icon_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_video = new Intent(SheetmusicActivity.this,VideoActivity.class);
                intent_video.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_video);
            }
        });
        // 글 커뮤니티 버튼
        icon_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_community = new Intent(SheetmusicActivity.this,CommunityActivity.class);
                intent_community.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_community);
            }
        });
        //--------------------------------------------------------------------------------------------------

        //악보 업로드 버튼
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_post = new Intent(SheetmusicActivity.this, SheetmusicUploadActivity.class);
                    intent_post.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent_post);
                }
                else{
                    intent_requireLogin.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent_requireLogin.putExtra("액티비티","악보업로드");
                    startActivity(intent_requireLogin);
                }
            }
        });

        //아이템 클릭
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(SheetmusicActivity.this,SheetmusicPostActivity.class);
                intent.putExtra("글제목",SheetmusicArticleInfo.get(position).get글제목());
                startActivity(intent);
            }
        });

        //---------------------------------카테고리 -------------------------------------------------------
        //NEW 버튼
        category_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Category=0;
                Filter_Price=0;
                update();
            }
        });

        //TOP 100버튼
        category_top100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Category=1;
                Filter_Price=0;
                update();
            }
        });

        //장르 버튼
        category_genre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Category=2;
                Filter_Genre="전체";
                Filter_Price=0;
                update();
            }
        });
        //---------------------------------장르선택 -------------------------------------------------------
        //장르 전체버튼
        genre_btn_ALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Genre="전체";
                update();
            }
        });

        //장르 K_POP 버튼
        genre_btn_KPOP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Genre="K-POP";
                update();
            }
        });

        //장르 POP 버튼
        genre_btn_POP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Genre="POP";
                update();
            }
        });
        //장르 뉴에이지 버튼
        genre_btn_NewAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Genre="뉴에이지";
                update();
            }
        });
        //장르 클래식 버튼
        genre_btn_Classic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Genre="클래식";
                update();
            }
        });
        //장르 재즈 버튼
        genre_btn_Jazz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Genre="재즈";
                update();
            }
        });
        //장르 연탄곡 버튼
        genre_btn_MultiHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Genre="연탄곡";
                update();
            }
        });
        //장르 게임애니 버튼
        genre_btn_GameAni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Genre="게임/애니";
                update();
            }
        });
        //장르 드라마영화 버튼
        genre_btn_DramaMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Genre="드라마/영화";
                update();
            }
        });
        //장르 동요 버튼
        genre_btn_Kids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Genre="동요";
                update();
            }
        });
        //장르 BGM 버튼
        genre_btn_BGM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Genre="BGM";
                update();
            }
        });
        //장르 CCM 버튼
        genre_btn_CCM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Genre="CCM";
                update();
            }
        });
        //장르 캐롤 버튼
        genre_btn_Carol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Genre="캐롤";
                update();
            }
        });


        //-----------------------------가격 선택 -------------------------------------------------------
        //가격 전체 버튼
        PriceFilter_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Price=0;
                update();
            }
        });

        //가격 유료 버튼
        PriceFilter_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Price=1;
                update();
            }
        });

        //가격 무료 버튼
        PriceFilter_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_Price=2;
                update();
            }
        });

        //검색버튼
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchinput = searchbar.getText().toString();
                searchbar.clearFocus();
                //키보드 가리기
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                search_init_button.setVisibility(View.GONE);

                onResume();
            }
        });


        searchbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                search_init_button.setVisibility(View.VISIBLE);
                return false;
            }
        });

        search_init_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbar.setText("");
                searchbar.clearFocus();
                searchinput = "";
                //키보드 가리기
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                search_init_button.setVisibility(View.GONE);

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

    //시간순 정렬 메소드
    public class TimeDescending implements Comparator<Item_SheetmusicActivity> {
        @Override
        public int compare(Item_SheetmusicActivity o1, Item_SheetmusicActivity o2) {
            return o1.get업로드시간() <= o2.get업로드시간() ? 1 : -1;
        }
    }

    //조회순 정렬 메소드
    public class HitsDescending implements  Comparator<Item_SheetmusicActivity>{
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
    public void update(){
        //재실행
        Intent intent_sheetmusic = new Intent(SheetmusicActivity.this,SheetmusicActivity.class);
        intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent_sheetmusic);
    }

    public void genrebtnInitialize(){
        genre_btn_ALL.setTextColor(Color.GRAY);
        genre_btn_KPOP.setTextColor(Color.GRAY);
        genre_btn_POP.setTextColor(Color.GRAY);
        genre_btn_NewAge.setTextColor(Color.GRAY);
        genre_btn_Classic.setTextColor(Color.GRAY);
        genre_btn_Jazz.setTextColor(Color.GRAY);
        genre_btn_MultiHand.setTextColor(Color.GRAY);
        genre_btn_GameAni.setTextColor(Color.GRAY);
        genre_btn_DramaMovie.setTextColor(Color.GRAY);
        genre_btn_Kids.setTextColor(Color.GRAY);
        genre_btn_BGM.setTextColor(Color.GRAY);
        genre_btn_CCM.setTextColor(Color.GRAY);
        genre_btn_Carol.setTextColor(Color.GRAY);


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





}

