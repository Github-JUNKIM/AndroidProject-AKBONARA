package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_SheetmusicActivity extends RecyclerView.Adapter<Adapter_SheetmusicActivity.MyViewHolder>  {
    private List<Item_SheetmusicActivity> mDataset;
    private OnItemClickListener mListener = null;
    private static final String TAG = "Adapter_SheetmusicActiv";




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    //아이템에 들어가는 요소들
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //뷰홀더는 데이터셋의 크기(길이)만큼 반복
        public ImageView ImageView_profile;
        public TextView TextView_name_seller;
        public TextView TextView_name_sheetmusic;
        public TextView number_of_like;
        public TextView number_of_comment;
        public TextView number_of_hits;
        public TextView upload_time;
        public TextView TextView_price;



        public MyViewHolder(View v) {
            super(v);

            Log.d(TAG, "MyViewHolder");
            ImageView_profile = v.findViewById(R.id.ImageView_profile);
            TextView_name_seller = v.findViewById(R.id.TextView_name_seller);
            TextView_name_sheetmusic = v.findViewById(R.id.TextView_name_sheetmusic);
            number_of_like = v.findViewById(R.id.number_of_like);
            number_of_comment = v.findViewById(R.id.number_of_comment);
            number_of_hits = v.findViewById(R.id.number_of_hits);
            upload_time = v.findViewById(R.id.upload_time);
            TextView_price = v.findViewById(R.id.TextView_price);

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
    public Adapter_SheetmusicActivity(List<Item_SheetmusicActivity> myDataset) {
        mDataset = myDataset;
        Log.d(TAG, "Adapter_SheetmusicActivity 객체 생성 ");
    }

    // Create new views (invoked by the layout manager)
    @Override // 하나의 아이템 전체목록(요소들의 집합)
    public Adapter_SheetmusicActivity.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {

        Log.d(TAG, "onCreateViewHolder");
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sheetmusic_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // 값을 셋팅한다.
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Item_SheetmusicActivity SheetmusicArticleData = mDataset.get(position);

        //프로필 이미지 주소가 없을때
        if(SheetmusicArticleData.get프로필().equals("")){
            holder.ImageView_profile.setImageResource(R.drawable.ic_icon_basicprofile);
        }
        else{
            holder.ImageView_profile.setImageURI(Uri.parse(SheetmusicArticleData.get프로필()));
        }
        holder.TextView_name_seller.setText(SheetmusicArticleData.get닉네임());
        holder.TextView_name_sheetmusic.setText(SheetmusicArticleData.get글제목());
        holder.number_of_like.setText(Integer.toString(SheetmusicArticleData.get좋아요수()));
        holder.number_of_comment.setText(Integer.toString(SheetmusicArticleData.get댓글수()));
        holder.number_of_hits.setText(Integer.toString(SheetmusicArticleData.get조회수()));
        holder.upload_time.setText(formatTimeString(SheetmusicArticleData.get업로드시간()));
        if(SheetmusicArticleData.get가격()==0){
            holder.TextView_price.setText("무료");
        }
        else {
            holder.TextView_price.setText(Integer.toString(SheetmusicArticleData.get가격()) + "원");
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

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
        Log.d(TAG, "setOnItemClickListener");
    }





    //시간 연산 클래스
    private static class TIME_MAXIMUM{
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }
    public static String formatTimeString(long regTime) {
        long curTime = System.currentTimeMillis();
        long diffTime = (curTime - regTime) / 1000;
        String msg = null;
        if (diffTime < TIME_MAXIMUM.SEC) {
            msg = "방금 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }


}