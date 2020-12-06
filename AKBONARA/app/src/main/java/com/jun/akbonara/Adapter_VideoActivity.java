package com.jun.akbonara;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Adapter_VideoActivity extends RecyclerView.Adapter<Adapter_VideoActivity.MyViewHolder>  {
    private List<Item_VideoActivity> mDataset;
    private OnItemClickListener mListener = null;
    private static final String TAG = "Adapter_SheetmusicActiv";

    JSONObject DATABASE;
    JSONArray array_client =new JSONArray();
    JSONArray array_Article_Sheetmusic = new JSONArray();
    JSONArray array_Article_Video = new JSONArray();

    SharedPreferences preferences;
    Context context;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    //아이템에 들어가는 요소들
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //뷰홀더는 데이터셋의 크기(길이)만큼 반복

        public ImageView YoutubeVideo;
        public ImageView ImageView_profile;
        public TextView TextView_name_seller;
        public TextView PostName;
        public TextView number_of_like;
        public TextView number_of_comment;
        public TextView number_of_hits;
        public TextView upload_time;




        public MyViewHolder(View v) {
            super(v);
            YoutubeVideo =v.findViewById(R.id.YoutubeVideo);
            ImageView_profile = v.findViewById(R.id.ImageView_profile);
            TextView_name_seller = v.findViewById(R.id.TextView_name_seller);
            PostName = v.findViewById(R.id.PostName);
            number_of_like = v.findViewById(R.id.number_of_like);
            number_of_comment = v.findViewById(R.id.number_of_comment);
            number_of_hits = v.findViewById(R.id.number_of_hits);
            upload_time = v.findViewById(R.id.upload_time);
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
    public Adapter_VideoActivity(List<Item_VideoActivity> myDataset, JSONObject database, Context context, SharedPreferences preferences) {
        mDataset = myDataset;
        DATABASE = database;
        this.context = context;
        this.preferences = preferences;

        try {
            array_client =  DATABASE.getJSONArray("회원정보");
            array_Article_Sheetmusic = DATABASE.getJSONArray("악보글정보");
            array_Article_Video = DATABASE.getJSONArray("영상글정보");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        Log.d(TAG, "Adapter_SheetmusicActivity 객체 생성 ");
    }

    // Create new views (invoked by the layout manager)
    @Override // 하나의 아이템 전체목록(요소들의 집합)
    public Adapter_VideoActivity.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {

        Log.d(TAG, "onCreateViewHolder");
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_activity, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // 값을 셋팅한다.
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Item_VideoActivity item_videoActivity = mDataset.get(position);

        int ShardOrServer=0;
        int ArticleNumber = -1;

        //쉐어드데이터랑 비교하기
        try {
            for(int i=0;i<array_Article_Video.length();i++) {
                JSONObject thisArticle = array_Article_Video.getJSONObject(i);
                if(thisArticle.getInt("아이디넘버")!=-1 && thisArticle.getString("글제목").equals(item_videoActivity.get글제목()) && thisArticle.getLong("업로드시간")==item_videoActivity.get업로드시간()){
                    ArticleNumber = i;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String ArticleNumberString = Integer.toString(ArticleNumber);

        SharedPreferences.Editor sharedEditor = preferences.edit();
        String youtubecode = preferences.getString(ArticleNumberString,"");
        if(youtubecode.equals("")){
            ShardOrServer=1;
            sharedEditor.putString(ArticleNumberString,item_videoActivity.get동영상주소());
            sharedEditor.commit();
        }
        else if(youtubecode.equals(item_videoActivity.get동영상주소())){
            ShardOrServer=0;
        }
        else if(!youtubecode.equals(item_videoActivity.get동영상주소())){
            ShardOrServer=1;
            sharedEditor.putString(ArticleNumberString,item_videoActivity.get동영상주소());
            sharedEditor.commit();
        }

        //정보 매칭
        try {
            //게시글 작성자 객체생성
            JSONObject clientObj = array_client.getJSONObject(item_videoActivity.get아이디넘버());

            //프로필 이미지 주소가 없을때
            if(clientObj.getString("프로필사진").equals("")){
                holder.ImageView_profile.setImageResource(R.drawable.ic_icon_basicprofile);
            }
            else{
                holder.ImageView_profile.setImageURI(Uri.parse(clientObj.getString("프로필사진")));
            }
            holder.TextView_name_seller.setText(clientObj.getString("닉네임"));
            holder.PostName.setText(item_videoActivity.get글제목());

            //쉐어드에 데이터가 없거나 업데이트가 안되었다면
            if(ShardOrServer==1) {
                Picasso.get().load("https://img.youtube.com/vi/" + item_videoActivity.get동영상주소() + "/mqdefault.jpg").into(holder.YoutubeVideo);
                Log.d("인터넷 이미지 접근", "이미지 주소 :"+ "https://img.youtube.com/vi/" + item_videoActivity.get동영상주소() + "/mqdefault.jpg");

                //이미지 다운로드
                new LoadImage(context,item_videoActivity.get동영상주소()).execute("https://img.youtube.com/vi/" + item_videoActivity.get동영상주소() + "/mqdefault.jpg");
                Log.d("이미지 다운로드",  "이미지 다운로드");


            }
            //쉐어드에 최신데이터가 있다면
            else if(ShardOrServer==0){
                String videocode = preferences.getString(ArticleNumberString,"");

                holder.YoutubeVideo.setImageURI(Uri.parse("/data/data/"+context.getPackageName()+"/app_images/"+videocode +".png"));
                Log.d("쉐어드데이터 사용", "이미지 주소 :" +"/data/data/com.example.myapplication/app_images/"+videocode+".png");
            }

//            holder.YoutubeVideo.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//                @Override
//                public void onReady(YouTubePlayer youTubePlayer) {
//                    super.onReady(youTubePlayer);
//                    youTubePlayer.cueVideo(item_videoActivity.get동영상주소(),0);
//                }
//            });

            holder.number_of_like.setText(Integer.toString(item_videoActivity.get좋아요한사람().length()));
            holder.number_of_comment.setText(Integer.toString(item_videoActivity.get댓글목록().length()));
            holder.number_of_hits.setText(Integer.toString(item_videoActivity.get조회한사람().length()));
            holder.upload_time.setText(formatTimeString(item_videoActivity.get업로드시간()));

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