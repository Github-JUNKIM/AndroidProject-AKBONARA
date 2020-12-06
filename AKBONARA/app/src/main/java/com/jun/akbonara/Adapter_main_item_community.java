package com.jun.akbonara;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Adapter_main_item_community extends RecyclerView.Adapter<Adapter_main_item_community.MyViewHolder> {
    private List<Item_CommunityActivity> mDataset;
    private OnItemClickListener mListener = null ;

    JSONObject DATABASE;
    JSONArray array_client =new JSONArray();
    JSONArray array_Article_Sheetmusic = new JSONArray();
    JSONArray array_Article_Video = new JSONArray();
    JSONArray array_Article_Community = new JSONArray();

    TimeCalculator timeCalculator;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    //아이템에 들어가는 요소들
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //뷰홀더는 데이터셋의 크기(길이)만큼 반복

        public ImageView ImageView_profile;
        public TextView TextView_name_seller;
        public TextView upload_time;
        public TextView number_of_hits;
        public TextView number_of_like;
        public TextView number_of_comment;
        public TextView PostName;
        public View View_line;





        public MyViewHolder(View v) {
            super(v);

            ImageView_profile = v.findViewById(R.id.ImageView_profile);
            TextView_name_seller = v.findViewById(R.id.TextView_name_seller);
            PostName = v.findViewById(R.id.PostName);
            upload_time = v.findViewById(R.id.upload_time);
            number_of_hits = v.findViewById(R.id.number_of_hits);
            number_of_like = v.findViewById(R.id.number_of_like);
            number_of_comment = v.findViewById(R.id.number_of_comment);
            View_line = v.findViewById(R.id.View_line);



            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(pos);
                        }
                    }

                }
            });

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Adapter_main_item_community(List<Item_CommunityActivity> myDataset, JSONObject database) {
        mDataset = myDataset;
        DATABASE = database;

        try {
            array_client =  DATABASE.getJSONArray("회원정보");
            array_Article_Sheetmusic = DATABASE.getJSONArray("악보글정보");
            array_Article_Video = DATABASE.getJSONArray("영상글정보");
            array_Article_Community =DATABASE.getJSONArray("커뮤니티글정보");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Create new views (invoked by the layout manager)
    @Override // 하나의 아이템 전체목록(요소들의 집합)
    public Adapter_main_item_community.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_community_activity, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // 값을 셋팅한다.
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Item_CommunityActivity item_communityActivity = mDataset.get(position);

        try {
            JSONObject clientObj = array_client.getJSONObject(item_communityActivity.get아이디넘버());

            //프로필 이미지 주소가 없을때
            if(clientObj.getString("프로필사진").equals("")){
                holder.ImageView_profile.setImageResource(R.drawable.ic_icon_basicprofile);
            }
            else{
                holder.ImageView_profile.setImageURI(Uri.parse(clientObj.getString("프로필사진")));
            }
            holder.TextView_name_seller.setText(clientObj.getString("닉네임"));
            holder.PostName.setText(item_communityActivity.get글제목());


            holder.number_of_like.setText(Integer.toString(item_communityActivity.get좋아요한사람().length()));
            holder.number_of_comment.setText(Integer.toString(item_communityActivity.get댓글목록().length()));
            holder.number_of_hits.setText(Integer.toString(item_communityActivity.get조회한사람().length()));
            holder.upload_time.setText(timeCalculator.formatTimeString(item_communityActivity.get업로드시간()));
            holder.View_line.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        //삼항 연산자 mDataset이 null이면 0 return, 아니면 size리턴
        return mDataset == null ? 0 : mDataset.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }




}