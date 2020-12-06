package com.jun.akbonara;

import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Adapter_MypageActivity_CashHistory extends RecyclerView.Adapter<Adapter_MypageActivity_CashHistory.MyViewHolder>  {
    private List  mDataset;
    private OnItemClickListener mListener = null;
    private static final String TAG = "Adapter_alarmActiv";

    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONArray array_Article_Sheetmusic = new JSONArray();

    int ID_NUMBER;

    JSONObject CurrentClient;




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    //아이템에 들어가는 요소들
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //뷰홀더는 데이터셋의 크기(길이)만큼 반복

        TextView history_time,history_type,history_price;

        public MyViewHolder(View v) {
            super(v);
            history_price = v.findViewById(R.id.history_price);
            history_time = v.findViewById(R.id.history_time);
            history_type = v.findViewById(R.id.history_type);


            Log.d(TAG, "MyViewHolder");

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: 포지션 계산");
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Log.d(TAG, "onClick: pos != RecyclerView.NO_POSITION ");
                        if(mListener !=null){
                            Log.d(TAG, "onClick: mListener !=null ");
                            mListener.onItemClick(pos);
                        }
                    }

                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Adapter_MypageActivity_CashHistory(List myDataset, JSONObject database)  {
        mDataset = myDataset;
        DATABASE = database;

        try {
            array_client =  DATABASE.getJSONArray("회원정보");
            array_Article_Sheetmusic = DATABASE.getJSONArray("악보글정보");
            ID_NUMBER = DATABASE.getInt("로그인정보");
            CurrentClient = array_client.getJSONObject(ID_NUMBER);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d(TAG, "Adapter_alarmActivity 객체 생성 ");

    }

    // Create new views (invoked by the layout manager)
    @Override // 하나의 아이템 전체목록(요소들의 집합)
    public Adapter_MypageActivity_CashHistory.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                              int viewType) {

        Log.d(TAG, "onCreateViewHolder");
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cashhistory, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // 값을 셋팅한다.
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        try {
            JSONArray historyList = CurrentClient.getJSONArray("캐시내역");
            JSONObject historyObj = historyList.getJSONObject(position);
            //시간 매칭
            long time = historyObj.getLong("시간");
            SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str = dayTime.format(new Date(time));
            holder.history_time.setText(str);

            //타입 매칭
            holder.history_type.setText(historyObj.getString("타입")+ "  ("+historyObj.getString("캐시정보")+")");
            //가격 매칭
            DecimalFormat formatter = new DecimalFormat("###,###");
            String cashtype = historyObj.getString("타입");
            if(cashtype.equals("캐시 충전") || cashtype.equals("악보 판매") || cashtype.equals("미니게임 보상")){
                holder.history_price.setTextColor(Color.BLUE);
                holder.history_price.setText(formatter.format(historyObj.getInt("금액"))+"원");
            }
            else{
                holder.history_price.setTextColor(Color.RED);
                holder.history_price.setText("-"+formatter.format(historyObj.getInt("금액"))+"원");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }




        Log.d(TAG, "onBindViewHolder");

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        //삼항 연산자 mDataset이 null이면 0 return, 아니면 size리턴
        return mDataset == null ? 0 : mDataset.size();
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }





}