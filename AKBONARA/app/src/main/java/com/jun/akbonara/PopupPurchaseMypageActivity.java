package com.jun.akbonara;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PopupPurchaseMypageActivity extends Activity {


    int ID_NUMBER;
    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONArray array_Article_Sheetmusic = new JSONArray();
    //--------------------------------------------------------------------------------------------------

    JSONObject CurrentClient;
    TextView move_btn,open_btn;

    int ArticleNumber;
    TextView filename;


    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_mypage_purchase);  //layout xml 매칭

        move_btn = findViewById (R.id.move_btn);
        open_btn = findViewById (R.id.open_btn);
        filename = findViewById (R.id.filename);


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

        //체크해보자!!
        try {
            CurrentClient = array_client.getJSONObject(ID_NUMBER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //해당 글넘버 찾기
        Intent intent = getIntent();
        final String CheckArticleName = intent.getStringExtra("글제목");

        for(int i=0;i<array_Article_Sheetmusic.length();i++){
            try {
                final JSONObject a = (JSONObject) array_Article_Sheetmusic.get(i);
                if(a.getString("글제목").equals(CheckArticleName)){
                    ArticleNumber =i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        move_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PopupPurchaseMypageActivity.this,SheetmusicPostActivity.class);
                intent.putExtra("글제목",CheckArticleName);
                startActivity(intent);
                finish();
            }
        });

        open_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PopupPurchaseMypageActivity.this,Opensheetmusic.class);
                intent.putExtra("아티클넘버",ArticleNumber);
                startActivity(intent);
                finish();
            }
        });

        try {
            JSONObject thisArticle = array_Article_Sheetmusic.getJSONObject(ArticleNumber);
            filename.setText(getFileName(Uri.parse(thisArticle.getString("악보"))));
        } catch (JSONException e) {
            e.printStackTrace();
        }




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


}

