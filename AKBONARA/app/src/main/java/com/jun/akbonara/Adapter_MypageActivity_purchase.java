package com.jun.akbonara;

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

import java.util.List;

public class Adapter_MypageActivity_purchase extends RecyclerView.Adapter<Adapter_MypageActivity_purchase.MyViewHolder>  {
    private List  mDataset;
    private OnItemClickListener mListener = null;
    private static final String TAG = "Adapter_alarmActiv";

    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONArray array_Article_Sheetmusic = new JSONArray();



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    //아이템에 들어가는 요소들
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //뷰홀더는 데이터셋의 크기(길이)만큼 반복

        public PDFView purchase_pdf;
        public ImageView ImageView_profile;
        public TextView TextView_name_seller;
//        public TextView  TextView_name_sheetmusic;


        public MyViewHolder(View v) {
            super(v);

            Log.d(TAG, "MyViewHolder");
            purchase_pdf = v.findViewById(R.id.purchase_pdf);
            ImageView_profile = v.findViewById(R.id.ImageView_profile);
            TextView_name_seller = v.findViewById(R.id.TextView_name_seller);
//            TextView_name_sheetmusic = v.findViewById(R.id.TextView_name_sheetmusic);

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
    public Adapter_MypageActivity_purchase(List myDataset, JSONObject database)  {
        mDataset = myDataset;
        DATABASE = database;

        try {
            array_client =  DATABASE.getJSONArray("회원정보");
            array_Article_Sheetmusic = DATABASE.getJSONArray("악보글정보");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Adapter_alarmActivity 객체 생성 ");

    }

    // Create new views (invoked by the layout manager)
    @Override // 하나의 아이템 전체목록(요소들의 집합)
    public Adapter_MypageActivity_purchase.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                           int viewType) {

        Log.d(TAG, "onCreateViewHolder");
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_purchase, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // 값을 셋팅한다.
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        int articlenumber = (int) mDataset.get(position);
        Log.d("아티클넘버", String.valueOf(articlenumber));

        try {
            JSONObject thisarticle = array_Article_Sheetmusic.getJSONObject(articlenumber);
            JSONObject writerObj = array_client.getJSONObject(thisarticle.getInt("아이디넘버"));
            holder.purchase_pdf.fromUri(Uri.parse(thisarticle.getString("악보"))).pages(0).load();
            Log.d("pdf주소", thisarticle.getString("악보"));
            if(writerObj.getString("프로필사진").equals("")){
                holder.ImageView_profile.setImageResource(R.drawable.ic_icon_basicprofile);
            }
            else{
                holder.ImageView_profile.setImageURI(Uri.parse(writerObj.getString("프로필사진")));
            }
            holder.TextView_name_seller.setText(writerObj.getString("닉네임"));
//            holder.TextView_name_sheetmusic.setText(thisarticle.getString("글제목"));
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