package com.jun.akbonara;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Adapter_AlarmActivity extends RecyclerView.Adapter<Adapter_AlarmActivity.MyViewHolder>  {
    private List<Item_AlarmActivity> mDataset;
    private OnItemClickListener mListener = null;
    private static final String TAG = "Adapter_alarmActiv";

    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONObject sendingClient = new JSONObject();


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    //아이템에 들어가는 요소들
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //뷰홀더는 데이터셋의 크기(길이)만큼 반복
        public TextView TextView_alarm;



        public MyViewHolder(View v) {
            super(v);

            Log.d(TAG, "MyViewHolder");
            TextView_alarm = v.findViewById(R.id.TextView_alarm);

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
    public Adapter_AlarmActivity(List<Item_AlarmActivity> myDataset,JSONObject database)  {
        mDataset = myDataset;
        DATABASE = database;

        try {
            array_client =  DATABASE.getJSONArray("회원정보");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Adapter_alarmActivity 객체 생성 ");

    }

    // Create new views (invoked by the layout manager)
    @Override // 하나의 아이템 전체목록(요소들의 집합)
    public Adapter_AlarmActivity.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {

        Log.d(TAG, "onCreateViewHolder");
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alram_activity, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // 값을 셋팅한다.
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Item_AlarmActivity item_alarmActivity = mDataset.get(position);


        try {
            sendingClient = array_client.getJSONObject(item_alarmActivity.get아이디넘버());
            String msg = sendingClient.getString("닉네임")+item_alarmActivity.get알림메세지();
            holder.TextView_alarm.setText(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(item_alarmActivity.get체크여부()==false) {
            holder.TextView_alarm.setBackgroundResource(R.color.darkgrey);
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