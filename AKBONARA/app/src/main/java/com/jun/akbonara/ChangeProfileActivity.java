package com.jun.akbonara;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChangeProfileActivity extends AppCompatActivity {

    //기본 메뉴 멤버 변수 선언
    ImageView button_mypage, button_alarm, icon_music ,icon_video ,icon_community;
    TextView 악보나라;
    //--------------------------------------------------------------------------

    EditText nickname;
    Button checknickname,ChangeNickname;
    TextView resetbutton;
    ImageView upload_image,profile;


    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONArray array_SheetmusicArticle = new JSONArray();

    int ID_NUMBER;
    boolean CheckNickname;

    private final int REQ_CODE_GALLERY = 100;

    String ChangedNickname="";


    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeprofile);  //layout xml 매칭

        //기본 메뉴 주소 매칭
        button_mypage = findViewById(R.id.button_mypage);
        button_alarm =findViewById(R.id.button_alarm);
        악보나라 = findViewById(R.id.악보나라);
        icon_music = findViewById(R.id.icon_music);
        icon_video = findViewById(R.id.icon_video);
        icon_community = findViewById(R.id.icon_community);
        //--------------------------------------------------------------------------

        nickname = findViewById(R.id.nickname);
        checknickname = findViewById(R.id.checknickname);
        ChangeNickname = findViewById(R.id.ChangeNickname);
        resetbutton = findViewById(R.id.resetbutton);
        upload_image = findViewById(R.id.upload_image);
        profile = findViewById(R.id.profile);


    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        CheckNickname =false;
        checknickname.setBackgroundResource(R.drawable.btn_grey);
        nickname.clearFocus();

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
        final Intent intent_requireLogin = new Intent(ChangeProfileActivity.this,LoginActivity.class);

        //기본 메뉴 버튼 클릭이벤트
        // 홈 버튼
        악보나라.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(ChangeProfileActivity.this,MainActivity.class);
                intent_main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_main);
            }
        });
        // 마이페이지 버튼
        button_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID_NUMBER!=-1) {
                    Intent intent_mypage = new Intent(ChangeProfileActivity.this, MypageActivity.class);
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
                    Intent intent_alarm = new Intent(ChangeProfileActivity.this, AlarmActivity.class);
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
                Intent intent_sheetmusic = new Intent(ChangeProfileActivity.this,SheetmusicActivity.class);
                intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_sheetmusic);
            }
        });
        // 영상 커뮤니티 버튼
        icon_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_video = new Intent(ChangeProfileActivity.this,VideoActivity.class);
                intent_video.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_video);
            }
        });
        // 글 커뮤니티 버튼
        icon_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_community = new Intent(ChangeProfileActivity.this,CommunityActivity.class);
                intent_community.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_community);
            }
        });
        //--------------------------------------------------------------------------------------------------

        //해당 아이디의 닉네임
        String NicknameCheck="";
        try {
            JSONObject a =(JSONObject) array_client.get(ID_NUMBER);
            NicknameCheck = a.getString("닉네임");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //닉네임 셋팅
        nickname.setText(NicknameCheck);


        //해당 아이디의 프로필이미지
        String ProfileImage="";
        try {
            JSONObject a =(JSONObject) array_client.get(ID_NUMBER);
            ProfileImage = a.getString("프로필사진");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //프로필 사진 셋팅
        if(!ProfileImage.equals("")) {
            Uri ProfileImageUri = Uri.parse(ProfileImage);
            profile.setImageURI(ProfileImageUri);
        }
        else{
            profile.setImageResource(R.drawable.ic_icon_basicprofile);
        }



        //닉네임 중복확인 버튼
        checknickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String InputNickname = nickname.getText().toString();

                //닉네임 리스트 만들기
                List<String> List_Nickname = new ArrayList<>();

                for(int i=0;i<array_client.length();i++){
                    try {
                        JSONObject a =(JSONObject) array_client.get(i);
                        String Nickname = a.getString("닉네임");
                        List_Nickname.add(Nickname);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(List_Nickname.contains(InputNickname)){
                    Toast.makeText(ChangeProfileActivity.this, "이미 사용중인 닉네임입니다.", Toast.LENGTH_SHORT).show();
                    CheckNickname =false;
                    checknickname.setBackgroundResource(R.drawable.btn_grey);
                }
                else{
                    Toast.makeText(ChangeProfileActivity.this, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();
                    CheckNickname =true;
                    checknickname.setBackgroundResource(R.drawable.checkcomplete);

                    ChangedNickname = InputNickname;
                }
            }
        });



        //닉네임 변경 버튼
        ChangeNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CheckNickname==false){
                    Toast.makeText(ChangeProfileActivity.this, "중복 확인을 해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        JSONObject a =(JSONObject) array_client.get(ID_NUMBER);
                        a.put("닉네임",ChangedNickname);

                        for(int i=0;i<array_SheetmusicArticle.length();i++){
                            JSONObject b = (JSONObject) array_SheetmusicArticle.get(i);
                            int idnumber = b.getInt("아이디넘버");
                            if(idnumber==ID_NUMBER){
                                Log.d("아이디넘버", String.valueOf(idnumber));
                                String Artist_name = b.getString("원곡자");
                                String Sheetmusic_name = b.getString("악보명");
                                b.put("닉네임",ChangedNickname);
                                b.put("글제목",Artist_name+" - "+Sheetmusic_name+" By "+ ChangedNickname);
                            }
                        }


                        SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("데이터베이스", DATABASE.toString());
                        editor.commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(ChangeProfileActivity.this, "닉네임이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    onResume();

                }

            }
        });

        //이미지 변경 버튼
        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setType("image/*");
                startActivityForResult(intent, REQ_CODE_GALLERY);


            }
        });


        //초기화 버튼
        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject a = (JSONObject) array_client.get(ID_NUMBER);
                    a.remove("프로필사진");

                    SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("데이터베이스", DATABASE.toString());
                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(ChangeProfileActivity.this, "기본 이미지로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangeProfileActivity.this,ChangeProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_GALLERY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    Uri originalUri = data.getData();
                    final int takeFlags = data.getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    // Check for the freshest data.
                    Log.d("겟 플래그", String.valueOf(takeFlags));
                    getContentResolver().takePersistableUriPermission(originalUri, takeFlags);
                    Log.d("이미지 URI 주소", String.valueOf(originalUri));

                    String ProfileImage = originalUri.toString();

                    JSONObject a = (JSONObject) array_client.get(ID_NUMBER);
                    a.put("프로필사진", ProfileImage);

                    for(int i=0;i<array_SheetmusicArticle.length();i++){
                        JSONObject b = (JSONObject) array_SheetmusicArticle.get(i);
                        int idnumber = b.getInt("아이디넘버");
                        if(idnumber==ID_NUMBER){
                            b.put("프로필",ProfileImage);
                        }

                    }




                    SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("데이터베이스", DATABASE.toString());
                    editor.commit();
                    Toast.makeText(ChangeProfileActivity.this, "프로필 이미지가 변경되었습니다.", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
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