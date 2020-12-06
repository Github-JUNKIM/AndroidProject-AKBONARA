package com.jun.akbonara;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.CancelListener;
import kr.co.bootpay.CloseListener;
import kr.co.bootpay.ConfirmListener;
import kr.co.bootpay.DoneListener;
import kr.co.bootpay.ErrorListener;
import kr.co.bootpay.ReadyListener;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;

public class ChargeActivity extends AppCompatActivity {

    private int stuck = 10;

    //기본 메뉴 멤버 변수 선언
    ImageView button_mypage, button_alarm, icon_music ,icon_video ,icon_community;
    TextView 악보나라;
    int ID_NUMBER;
    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();

    TextView charecash;
    ImageView button_require;
    ScrollView Scroll_require;
    Button charge_btn,close_btn;
    RadioButton RadioButton_cash1000,RadioButton_cash5000,RadioButton_cash10000,RadioButton_cash20000,RadioButton_cash50000;
    RadioGroup radioGroup;

    JSONObject CurrentClient ;

    CheckBox checkBox;

    boolean requireExpandShrink = true;

    int int_chargecash = 0;

    boolean radiobtnselected = false;

    //main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);  //layout xml 매칭
        //기본 메뉴 주소 매칭
        button_mypage = findViewById(R.id.button_mypage);
        button_alarm =findViewById(R.id.button_alarm);
        악보나라 = findViewById(R.id.악보나라);
        icon_music = findViewById(R.id.icon_music);
        icon_video = findViewById(R.id.icon_video);
        icon_community = findViewById(R.id.icon_community);

        charecash = findViewById (R.id.charecash);
        button_require = findViewById (R.id.button_require);
        Scroll_require = findViewById (R.id.Scroll_require);
        charge_btn = findViewById (R.id.charge_btn);
        close_btn =findViewById (R.id.close_btn);
        checkBox = findViewById (R.id.checkBox);

        RadioButton_cash1000 = findViewById (R.id.RadioButton_cash1000);
        RadioButton_cash5000 = findViewById (R.id.RadioButton_cash5000);
        RadioButton_cash10000 = findViewById (R.id.RadioButton_cash10000);
        RadioButton_cash20000 = findViewById (R.id.RadioButton_cash20000);
        RadioButton_cash50000 = findViewById (R.id.RadioButton_cash50000);
        radioGroup = findViewById (R.id.radioGroup);

        // 초기설정 - 해당 프로젝트(안드로이드)의 application id 값을 설정합니다. 결제와 통계를 위해 꼭 필요합니다.
        BootpayAnalytics.init(this, "5f9facf118e1ae002e4f4688");

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


        //현재회원
        try {
            CurrentClient = array_client.getJSONObject(ID_NUMBER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RadioButton_cash1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int_chargecash = 1000;
                radiobtnselected = true;
                onResume();
            }
        });
        RadioButton_cash5000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int_chargecash = 5000;
                radiobtnselected = true;
                onResume();
            }
        });
        RadioButton_cash10000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int_chargecash = 10000;
                radiobtnselected = true;
                onResume();
            }
        });
        RadioButton_cash20000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int_chargecash = 20000;
                radiobtnselected = true;
                onResume();
            }
        });
        RadioButton_cash50000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int_chargecash = 50000;
                radiobtnselected = true;
                onResume();
            }
        });

        //결제금액 셋팅
        DecimalFormat formatter = new DecimalFormat("###,###");
        charecash.setText(formatter.format(int_chargecash)+"원");


        //결제약관 표시/숨김
        if(requireExpandShrink == false){
            Scroll_require.setVisibility(View.GONE);
            button_require.setImageResource(R.drawable.icon_expand);
        }
        else{
            Scroll_require.setVisibility(View.VISIBLE);
            button_require.setImageResource(R.drawable.icon_shrink);
        }

        //로그인 안되었을때 로그인 창으로 이동
        final Intent intent_requireLogin = new Intent(ChargeActivity.this,LoginActivity.class);

        //기본 메뉴 버튼 클릭이벤트
        // 홈 버튼
        악보나라.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(ChargeActivity.this,MainActivity.class);
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
                    Intent intent_mypage = new Intent(ChargeActivity.this, MypageActivity.class);
                    intent_mypage.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent_mypage);
                    finish();
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
                    Intent intent_alarm = new Intent(ChargeActivity.this, AlarmActivity.class);
                    intent_alarm.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent_alarm);
                    finish();
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
                Intent intent_sheetmusic = new Intent(ChargeActivity.this,SheetmusicActivity.class);
                intent_sheetmusic.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_sheetmusic);
                finish();
            }
        });
        // 영상 커뮤니티 버튼
        icon_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_video = new Intent(ChargeActivity.this,VideoActivity.class);
                intent_video.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_video);
                finish();
            }
        });
        // 글 커뮤니티 버튼
        icon_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_community = new Intent(ChargeActivity.this,CommunityActivity.class);
                intent_community.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent_community);
                finish();
            }
        });
        //--------------------------------------------------------------------------------------------------



        button_require.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requireExpandShrink==false){
                    requireExpandShrink = true;
                }else{
                    requireExpandShrink = false;
                }
                onResume();
            }
        });


        charge_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(checkBox.isChecked()==true && radiobtnselected==true ){
                    int id = radioGroup.getCheckedRadioButtonId();
                    RadioButton rb = findViewById (id);
                    String Productname = rb.getText().toString();
                    onClick_request(v,Productname);
                }
                else if(!checkBox.isChecked() && radiobtnselected ){
                    Toast.makeText(ChargeActivity.this, "결제약관에 동의해주세요", Toast.LENGTH_SHORT).show(); //(context, message, floating time)
                }
                else if(checkBox.isChecked() && !radiobtnselected ){
                    Toast.makeText(ChargeActivity.this, "결제금액을 선택해주세요", Toast.LENGTH_SHORT).show(); //(context, message, floating time)
                }
                else if(!checkBox.isChecked() && !radiobtnselected ){
                    Toast.makeText(ChargeActivity.this, "결제금액을 선택해주세요", Toast.LENGTH_SHORT).show(); //(context, message, floating time)
                }
            }
        });
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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


    public void onClick_request(View v, final String productname) {
//        결제호출
        Bootpay.init(getFragmentManager())
                .setApplicationId("5f9facf118e1ae002e4f4688") // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(PG.KCP) // 결제할 PG 사
                .setUserPhone("010-1234-5678") // 구매자 전화번호
                .setMethod(Method.SELECT) // 결제수단
                .setName(productname) // 결제할 상품명
                .setOrderId("1234") //고유 주문번호로, 생성하신 값을 보내주셔야 합니다.
                .setPrice(int_chargecash) // 결제할 금액
                //.setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )
                .setQuotas(new int[] {0,2,3}) // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
                .addItem("마우스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String message) {
                        if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                        else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                        Log.d("confirm", message);
                    }
                })
                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                    @Override
                    public void onDone(@Nullable String message) {
                        Log.d("done", message);
                        try {
                            int currentcash = CurrentClient.getInt("캐시");
                            CurrentClient.put("캐시",currentcash+int_chargecash);

                            //캐시내역
                            JSONArray CashHistory = CurrentClient.getJSONArray("캐시내역");
                            JSONObject historyObj = new JSONObject();
                            long time = System.currentTimeMillis();
                            historyObj.put("시간",time);
                            historyObj.put("타입","캐시 충전");
                            historyObj.put("캐시정보",productname);
                            historyObj.put("금액",int_chargecash);
                            CashHistory.put(historyObj);

                            SharedPreferences preferences = getSharedPreferences("데이터베이스", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("데이터베이스", DATABASE.toString());
                            editor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        success();

                    }
                })
                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                    @Override
                    public void onReady(@Nullable String message) {
                        Log.d("ready", message);
                    }
                })
                .onCancel(new CancelListener() { // 결제 취소시 호출
                    @Override
                    public void onCancel(@Nullable String message) {
                        Toast.makeText(ChargeActivity.this, "결제가 취소되었습니다.", Toast.LENGTH_SHORT).show(); //(context, message, floating time)
                        Log.d("cancel", message);
                    }
                })
                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                    @Override
                    public void onError(@Nullable String message) {
                        Toast.makeText(ChargeActivity.this, "결제가 취소되었습니다.", Toast.LENGTH_SHORT).show(); //(context, message, floating time)
                        Log.d("error", message);
                    }
                })
                .onClose(new CloseListener() { //결제창이 닫힐때 실행되는 부분
                    @Override
                    public void onClose(String message) {
                        Log.d("close", "close");
                    }
                })
                .show();
    }

    //실패 다이얼로그
    public void success(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("결제 완료");
        builder.setMessage("결제가 완료되었습니다.");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        finish();
                    }
                });
        builder.show();
    }

}

