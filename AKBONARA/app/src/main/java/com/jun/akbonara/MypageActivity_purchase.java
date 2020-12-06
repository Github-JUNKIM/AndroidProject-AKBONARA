package com.jun.akbonara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MypageActivity_purchase extends AppCompatActivity {

    //기본 메뉴 멤버 변수 선언
    ImageView button_mypage, button_alarm, icon_music ,icon_video ,icon_community;
    TextView 악보나라;
    int ID_NUMBER;
    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONArray array_Article_Sheetmusic = new JSONArray();
    //--------------------------------------------------------------------------------------------------

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Adapter_SheetmusicActivity SheetmusicAdapter;

    List<Item_SheetmusicActivity> purchaseList = new ArrayList<>();

    //현재 회원정보 JSONOBJ
    JSONObject CurrentClient;

    TextView NoPurchaseMSG;




    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_purchase);  //layout xml 매칭

        //기본 메뉴 주소 매칭
        button_mypage = findViewById(R.id.button_mypage);
        button_alarm =findViewById(R.id.button_alarm);
        악보나라 = findViewById(R.id.악보나라);
        icon_music = findViewById(R.id.icon_music);
        icon_video = findViewById(R.id.icon_video);
        icon_community = findViewById(R.id.icon_community);
        //--------------------------------------------------------------------------------------------------

        NoPurchaseMSG = findViewById (R.id.NoPurchaseMSG);

        //리싸이클러 뷰

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView_purchase);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




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

        SharedPreferences loadShared = getSharedPreferences("쉐어드데이터",MODE_PRIVATE);

        //로그인 아이디넘버
        try {
            ID_NUMBER = DATABASE.getInt("로그인정보");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //현재 회원정보
        try {
            CurrentClient = array_client.getJSONObject(ID_NUMBER);
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



        //리사이클러뷰 초기화
        purchaseList = new ArrayList<>();

        try {
            JSONObject thisclient  = (JSONObject) array_client.get(ID_NUMBER);
            JSONArray purchaseSheetmusicList =  thisclient.getJSONArray("구매한악보");
            JSONArray arraySheetmusicArticleInfo =  DATABASE.getJSONArray("악보글정보");

            for(int i=0 , j = purchaseSheetmusicList.length(); i <j ;i++){
                JSONObject obj =  arraySheetmusicArticleInfo.getJSONObject(purchaseSheetmusicList.getInt(i));
                if(obj.getInt("아이디넘버")!=-1) {
                    AddItemToList(obj);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // specify an adapter (see also next example)
        // 리싸이클러뷰 구현
        SheetmusicAdapter = new Adapter_SheetmusicActivity(purchaseList);
        recyclerView.setAdapter(SheetmusicAdapter);


        //구매한 악보가 없을때때
        if(purchaseList.size()==0){
            NoPurchaseMSG.setVisibility(View.VISIBLE);
        }
        else{
            NoPurchaseMSG.setVisibility(View.GONE);
        }







        //로그인 안되었을때 로그인 창으로 이동
        final Intent intent_requireLogin = new Intent(MypageActivity_purchase.this,LoginActivity.class);

        //기본 메뉴 버튼 클릭이벤트
        // 홈 버튼
        악보나라.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(MypageActivity_purchase.this,MainActivity.class);
                intent_main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_main);
            }
        });
        // 마이페이지 버튼
        button_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_mypage = new Intent(MypageActivity_purchase.this, MypageActivity.class);
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
                    Intent intent_alarm = new Intent(MypageActivity_purchase.this, AlarmActivity.class);
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
                Intent intent_sheetmusic = new Intent(MypageActivity_purchase.this,SheetmusicActivity.class);
                intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_sheetmusic);
            }
        });
        // 영상 커뮤니티 버튼
        icon_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_video = new Intent(MypageActivity_purchase.this,VideoActivity.class);
                intent_video.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_video);
            }
        });
        // 글 커뮤니티 버튼
        icon_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_community = new Intent(MypageActivity_purchase.this,CommunityActivity.class);
                intent_community.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_community);
            }
        });
        //--------------------------------------------------------------------------------------------------

        SheetmusicAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(MypageActivity_purchase.this,PopupPurchaseMypageActivity.class);
                intent.putExtra("글제목",purchaseList.get(position).get글제목());
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
        purchaseList.add(SheetmusicArticleData);
    }

}


//class MyAdapter extends BaseAdapter {
//    Context context;
//    List purchaseList;
//    LayoutInflater inf;
//    JSONObject DATABASE ;
//    JSONArray array_client =new JSONArray();
//    JSONArray array_Article_Sheetmusic = new JSONArray();
//    JSONObject thisArticle;
//
//    public MyAdapter(Context context, List purchaseList,JSONObject DATABASE) {
//        this.context = context;
//        this.purchaseList = purchaseList;
//        this.DATABASE = DATABASE;
//        try {
//            array_client =  DATABASE.getJSONArray("회원정보");
//            array_Article_Sheetmusic = DATABASE.getJSONArray("악보글정보");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        inf = (LayoutInflater) context.getSystemService
//                (Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    @Override
//    public int getCount() {
//        return purchaseList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return purchaseList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView==null)
//            convertView = inf.inflate(R.layout.item_purchase,parent,false);
//
//        int articlenumber = (int) purchaseList.get(position);
//        try {
//            thisArticle = array_Article_Sheetmusic.getJSONObject(articlenumber);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        PDFView purchase_pdf = convertView.findViewById(R.id.purchase_pdf);
//        ImageView ImageView_profile = convertView.findViewById (R.id.ImageView_profile);
//        TextView TextView_name_seller = convertView.findViewById (R.id.TextView_name_seller);
//        TextView TextView_name_sheetmusic = convertView.findViewById (R.id.TextView_name_sheetmusic);
//        try {
//            JSONObject writerobj = array_client.getJSONObject(thisArticle.getInt("아이디넘버"));
//            purchase_pdf.fromUri(Uri.parse(thisArticle.getString("악보"))).pages(0).load();
//            if(writerobj.getString("프로필사진").equals("")){
//                ImageView_profile.setImageResource(R.drawable.ic_icon_basicprofile);
//            }
//            else{
//                ImageView_profile.setImageURI(Uri.parse(writerobj.getString("프로필사진")));
//            }
//            TextView_name_seller.setText(writerobj.getString("닉네임"));
//            TextView_name_sheetmusic.setText(thisArticle.getString("글제목"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        return convertView;
//    }
//}



