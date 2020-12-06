package com.jun.akbonara;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Adapter_Double_Comment_SheetmusicActivity extends RecyclerView.Adapter<Adapter_Double_Comment_SheetmusicActivity.MyViewHolder>  {
    private List<Item_Double_Comment_Sheetmusic> mDataset;
    private OnItemClickListener mListener = null;
    private static final String TAG = "Adapter_Comment";
    private Context context;

    TimeCalculator timeCalculator;

    JSONObject DATABASE = new JSONObject();
    JSONArray array_client =new JSONArray();
    JSONArray array_Article_Sheetmusic = new JSONArray();
    JSONArray array_Article_Video = new JSONArray();
    JSONArray array_Article_Community = new JSONArray();

    JSONObject client = new JSONObject();
    String clientNickname;

    String string_Double_Comment_Content;

    int ArticleNumber;
    int ID_NUMBER;

    SharedPreferences preferences;

    SheetmusicPostActivity sheetmusicPostActivity = new SheetmusicPostActivity();
    VideoPostActivity videoPostActivity = new VideoPostActivity();
    CommunityPostActivity communityPostActivity = new CommunityPostActivity();
    int type;

    //Double_Comment에 넘겨주는 댓글객체 번호
    int commentobjNumber;

    //댓글 옵션 버튼 불린
    boolean MOREBUTTON = false;
    //댓글 달기(수정모드 or 답글모드) 불린
    boolean EditmodeOrReplymode;
    //좋아요 불린
    boolean like_boolean=false;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    //아이템에 들어가는 요소들
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //뷰홀더는 데이터셋의 크기(길이)만큼 반복
        public ImageView doublecomment_ImageView_profile;
        public TextView doublecomment_TextView_name_seller;
        public TextView doublecomment_upload_time;
        public TextView doublecomment_user_comment_content;
        public TextView doublecomment_number_of_like;
        public TextView doublecomment_AddDoubleComment_btn;
        public LinearLayout doublecomment_AddDoubleComment;
        public ImageView doublecomment_Close_AddDoubleComment;
        public TextView doublecomment_tag;
        public TextView doublecomment_AddDoubleComment_register_button;
        public EditText doublecomment_Double_Comment_Content;
        public ImageView more_btn;
        public LinearLayout EditOrDelete_btn;
        public Button Edit_btn,Delete_btn;
        public ImageView user_comment_like_1;


        public MyViewHolder(View v) {

            super(v);

            Log.d(TAG, "MyViewHolder");
            doublecomment_ImageView_profile = v.findViewById(R.id.doublecomment_ImageView_profile);
            doublecomment_TextView_name_seller = v.findViewById(R.id.doublecomment_TextView_name_seller);
            doublecomment_upload_time = v.findViewById(R.id.doublecomment_upload_time);
            doublecomment_user_comment_content = v.findViewById(R.id.doublecomment_user_comment_content);
            doublecomment_number_of_like = v.findViewById(R.id.doublecomment_number_of_like);
            doublecomment_AddDoubleComment_btn = v.findViewById(R.id.doublecomment_AddDoubleComment_btn);
            doublecomment_AddDoubleComment = v.findViewById(R.id.doublecomment_AddDoubleComment);
            doublecomment_Close_AddDoubleComment = v.findViewById(R.id.doublecomment_Close_AddDoubleComment);
            doublecomment_tag = v.findViewById(R.id.doublecomment_tag);
            doublecomment_AddDoubleComment_register_button = v.findViewById(R.id.doublecomment_AddDoubleComment_register_button);
            doublecomment_Double_Comment_Content = v.findViewById(R.id.doublecomment_Double_Comment_Content);
            more_btn = v.findViewById(R.id.more_btn);
            EditOrDelete_btn = v.findViewById(R.id.EditOrDelete_btn);
            Edit_btn = v.findViewById(R.id.Edit_btn);
            Delete_btn = v.findViewById(R.id.Delete_btn);
            user_comment_like_1 = v.findViewById(R.id.user_comment_like_1);


            //수정삭제 기능 버튼
            more_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MOREBUTTON==false) {
                        EditOrDelete_btn.setVisibility(View.VISIBLE);
                        MOREBUTTON=true;
                    }
                    else{
                        EditOrDelete_btn.setVisibility(View.GONE);
                        MOREBUTTON=false;
                    }
                }
            });

            //수정 버튼
            Edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    doublecomment_AddDoubleComment.setVisibility(View.VISIBLE);
                    doublecomment_Double_Comment_Content.setText(mDataset.get(pos).get댓글내용());
                    doublecomment_tag.setText("답글 수정");
                    EditmodeOrReplymode = false;

                }
            });

            //삭제버튼
            Delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    show(pos,commentobjNumber);
                }
            });

            //답글달기 버튼
            doublecomment_AddDoubleComment_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //비회원일대
                    if(ID_NUMBER==-1){
                        Intent intent = new Intent(context,LoginActivity.class);
                        if(type==0) {
                            sheetmusicPostActivity.loginrequireIntent(intent);
                        }
                        else if(type==1){
                            videoPostActivity.loginrequireIntent(intent);
                        }
                        else if(type==2){
                            communityPostActivity.loginrequireIntent(intent);
                        }
                    }
                    //회원일때
                    else {
                        int pos = getAdapterPosition();
                        String doublecommentobjName = "";
                        try {
                            JSONObject doublecommentobj = (JSONObject) array_client.get(mDataset.get(pos).get아이디넘버());
                            doublecommentobjName = doublecommentobj.getString("닉네임");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        doublecomment_AddDoubleComment.setVisibility(View.VISIBLE);
                        doublecomment_tag.setText("@" + doublecommentobjName);
                        EditmodeOrReplymode = true;
                    }
                }
            });

            //답글달기 취소 버튼
            doublecomment_Close_AddDoubleComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                doublecomment_AddDoubleComment.setVisibility(View.GONE);
                doublecomment_Double_Comment_Content.setText("");
                }
            });

            //작성버튼
            doublecomment_AddDoubleComment_register_button.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    doublecomment_AddDoubleComment.setVisibility(View.GONE);
                    string_Double_Comment_Content = doublecomment_Double_Comment_Content.getText().toString();
                    doublecomment_Double_Comment_Content.setText("");

                    try {
                        int pos = getAdapterPosition();

                        long time = System.currentTimeMillis();
                        JSONObject thisArticle = new JSONObject();
                        if(type==0){
                            thisArticle = (JSONObject) array_Article_Sheetmusic.get(ArticleNumber);
                        }
                        else if(type==1){
                            thisArticle = (JSONObject) array_Article_Video.get(ArticleNumber);
                        }
                        else if(type==2){
                            thisArticle = (JSONObject) array_Article_Community.get(ArticleNumber);
                        }
                        JSONArray commentList = thisArticle.getJSONArray("댓글목록");
                        JSONObject thiscomment = (JSONObject) commentList.get(commentobjNumber);
                        JSONArray doublecommentList = thiscomment.getJSONArray("대댓글목록");

                        JSONObject doublecommentobj = new JSONObject();
                        for (int i = 0; i < doublecommentList.length(); i++) {
                            doublecommentobj = doublecommentList.getJSONObject(i);
                            if (doublecommentobj.getInt("아이디넘버") == mDataset.get(pos).get아이디넘버() && doublecommentobj.getLong("작성시간") == mDataset.get(pos).get작성시간()) {
                                break;
                            }
                        }

                        //답글 작성일때
                        if(EditmodeOrReplymode==true) {
                            JSONArray newjsonArray = new JSONArray();
                            JSONObject a = new JSONObject();

                            a.put("아이디넘버", ID_NUMBER);
                            a.put("댓글내용", string_Double_Comment_Content);
                            a.put("좋아요목록", newjsonArray);
                            a.put("작성시간", time);
                            a.put("태그아이디넘버",mDataset.get(pos).get아이디넘버());

                            doublecommentList.put(a);

                            //게시물 작성자에게 알림 데이터 등록
                            JSONObject replyedClient = array_client.getJSONObject(doublecommentobj.getInt("아이디넘버"));
                            JSONArray haha = replyedClient.getJSONArray("알림목록");
                            JSONObject b = new JSONObject();

                            String message = "님이 회원님의 댓글에 답장을 남기셨습니다.";

                            b.put("아이디넘버",ID_NUMBER);
                            if(type==0){
                                b.put("게시물타입","sheetmusic");
                            }
                            else if(type==1){
                                b.put("게시물타입","video");
                            }
                            else if(type==2){
                                b.put("게시물타입","community");
                            }
                            b.put("게시글넘버",ArticleNumber);
                            b.put("체크여부",false);
                            b.put("알림메세지",message);

                            haha.put(b);

                            if(ID_NUMBER==doublecommentobj.getInt("아이디넘버")) {
                                //댓글작성 알림 띄우기
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject currentClient = array_client.getJSONObject(ID_NUMBER);
                                            String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
                                            String SERVER_KEY = "AAAAYaTeLd0:APA91bHjijFYatJoNxtkE7bLc3tETYpb1Wepw3334akmTWR3a5SajgMxcSyCnvFD3D6TPl2Ks6ZMdKN2ZwfXCvN2aXQBo87oqWuRQ8gyzeD2Iw3G9tFrZADW4lMQpHUK5TOK58rJail4";

                                            String message = currentClient.getString("닉네임") + "님이 회원님의 댓글에 답장을 남기셨습니다.";
                                            // FMC 메시지 생성 start
                                            JSONObject root = new JSONObject();
                                            JSONObject notification = new JSONObject();
                                            notification.put("body", message);
                                            if (type == 0) {
                                                notification.put("title", sheetmusicPostActivity.getString(R.string.app_name));
                                            } else if (type == 1) {
                                                notification.put("title", videoPostActivity.getString(R.string.app_name));
                                            } else if (type == 2) {
                                                notification.put("title", communityPostActivity.getString(R.string.app_name));
                                            }
                                            root.put("notification", notification);
                                            root.put("to", FirebaseInstanceId.getInstance().getToken());
                                            Log.d("토큰값", FirebaseInstanceId.getInstance().getToken());
                                            // FMC 메시지 생성 end

                                            URL Url = new URL(FCM_MESSAGE_URL);
                                            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                                            conn.setRequestMethod("POST");
                                            conn.setDoOutput(true);
                                            conn.setDoInput(true);
                                            conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
                                            conn.setRequestProperty("Accept", "application/json");
                                            conn.setRequestProperty("Content-type", "application/json");
                                            OutputStream os = conn.getOutputStream();
                                            os.write(root.toString().getBytes("utf-8"));
                                            os.flush();
                                            conn.getResponseCode();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        }
                        //답글 수정일때
                        else if(EditmodeOrReplymode==false){
                            for (int i = 0; i < doublecommentList.length(); i++) {
                                JSONObject checkdoublecommentobj = doublecommentList.getJSONObject(i);
                                if (checkdoublecommentobj.getInt("아이디넘버") == mDataset.get(pos).get아이디넘버() && checkdoublecommentobj.getLong("작성시간") == mDataset.get(pos).get작성시간()) {
                                    checkdoublecommentobj.put("댓글내용",string_Double_Comment_Content);
                                    break;
                                }
                            }
                        }
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("데이터베이스", DATABASE.toString());
                        editor.commit();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(type==0) {
                        sheetmusicPostActivity.onResume();
                    }
                    else if(type==1){
                        videoPostActivity.onResume();
                    }
                    else if(type==2){
                        communityPostActivity.onResume();
                    }

                }
            });

            //좋아요 버튼
            user_comment_like_1.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    //비회원일때
                    if(ID_NUMBER==-1){
                        Intent intent = new Intent(context,LoginActivity.class);
                        if(type==0) {
                            sheetmusicPostActivity.loginrequireIntent(intent);
                        }
                        else if(type==1){
                            videoPostActivity.loginrequireIntent(intent);
                        }
                        else if(type==2){
                            communityPostActivity.loginrequireIntent(intent);
                        }
                    }
                    //회원일때
                    else {
                        int pos = getAdapterPosition();
                        try {
                            JSONObject thisArticle = new JSONObject();
                            if(type==0){
                                thisArticle = (JSONObject) array_Article_Sheetmusic.get(ArticleNumber);
                            }
                            else if(type==1){
                                thisArticle = (JSONObject) array_Article_Video.get(ArticleNumber);
                            }
                            else if(type==2){
                                thisArticle = (JSONObject) array_Article_Community.get(ArticleNumber);
                            }

                            JSONArray commentList = thisArticle.getJSONArray("댓글목록");
                            JSONObject thiscomment = (JSONObject) commentList.get(commentobjNumber);
                            JSONArray doublecommentList = thiscomment.getJSONArray("대댓글목록");

                            JSONObject doublecomment;
                            JSONArray likeClientList = new JSONArray();
                            for (int i = 0; i < doublecommentList.length(); i++) {
                                doublecomment = doublecommentList.getJSONObject(i);
                                if (doublecomment.getInt("아이디넘버") == mDataset.get(pos).get아이디넘버() && doublecomment.getLong("작성시간") == mDataset.get(pos).get작성시간()) {
                                    likeClientList = doublecomment.getJSONArray("좋아요목록");
                                    break;
                                }
                            }

                            if(likeClientList.length()==0){
                                likeClientList.put(ID_NUMBER);
                            }
                            else {
                                for (int i = 0; i < likeClientList.length(); i++) {
                                    if (likeClientList.get(i).equals(ID_NUMBER)) {
                                        likeClientList.remove(i);
                                        break;
                                    }
                                    if(i==likeClientList.length()-1){
                                        likeClientList.put(ID_NUMBER);
                                        Log.d(TAG, "onClick: 추가축추가추가"+likeClientList.get(likeClientList.length()-1));
                                        break;
                                    }
                                }

                            }
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("데이터베이스", DATABASE.toString());
                            editor.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(type==0) {
                            sheetmusicPostActivity.onResume();
                        }
                        else if(type==1){
                            videoPostActivity.onResume();
                        }
                        else if(type==2){
                            communityPostActivity.onResume();
                        }
                    }

                }
            });



        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Adapter_Double_Comment_SheetmusicActivity(int type,List<Item_Double_Comment_Sheetmusic> myDataset, JSONObject database, int articleNumber, int idNumber,
                                                     SharedPreferences sharedPreferences, int commentobjNumber, SheetmusicPostActivity sheetmusicPostActivity,
                                                     VideoPostActivity videoPostActivity,CommunityPostActivity communityPostActivity ,Context context) {
        this.context = context;
        this.type = type;

        this.sheetmusicPostActivity = sheetmusicPostActivity;
        this.videoPostActivity = videoPostActivity;
        this.communityPostActivity = communityPostActivity;
        this.commentobjNumber = commentobjNumber;
        preferences = sharedPreferences;

        //해당글 넘버
        ArticleNumber = articleNumber;

        //로그인한 회원
        ID_NUMBER = idNumber;

        mDataset = myDataset;
        DATABASE = database;
        //데이터 베이스 불러오기
        try {
            array_client =  DATABASE.getJSONArray("회원정보");
            array_Article_Sheetmusic = DATABASE.getJSONArray("악보글정보");
            array_Article_Video = DATABASE.getJSONArray("영상글정보");
            array_Article_Community = DATABASE.getJSONArray("커뮤니티글정보");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Adapter_SheetmusicActivity 객체 생성 ");;

    }



    // Create new views (invoked by the layout manager)
    @Override // 하나의 아이템 전체목록(요소들의 집합)
    public Adapter_Double_Comment_SheetmusicActivity.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                     int viewType) {

        Log.d(TAG, "onCreateViewHolder");
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sheetmusic_double_comment, parent, false);


        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // 값을 셋팅한다.
    @Override
    public void onBindViewHolder(final MyViewHolder holder,int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        //리스트 정렬 순서


        Item_Double_Comment_Sheetmusic item_double_comment_sheetmusic = mDataset.get(position);


        //회원객체 아이디넘버로 매칭
        try {
            //대댓글남긴 회원 정보
            client = (JSONObject) array_client.get(item_double_comment_sheetmusic.get아이디넘버());
            clientNickname = client.getString("닉네임");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            //로그인한 회원과 댓글 작성자가 다르다면 수정삭제버튼 안보이기
            if(ID_NUMBER!=item_double_comment_sheetmusic.get아이디넘버()){
                holder.more_btn.setVisibility(View.GONE);
            }
            if(client.getString("프로필사진").equals("")){
                holder.doublecomment_ImageView_profile.setImageResource(R.drawable.ic_icon_basicprofile);
            }
            else{
                holder.doublecomment_ImageView_profile.setImageURI(Uri.parse(client.getString("프로필사진")));
            }
            holder.doublecomment_TextView_name_seller.setText(clientNickname);
            holder.doublecomment_upload_time.setText(timeCalculator.formatTimeString(item_double_comment_sheetmusic.get작성시간()));

            int TagID_NUMBER = item_double_comment_sheetmusic.get태그아이디넘버();
            if(TagID_NUMBER!=-1) {
                JSONObject a = (JSONObject) array_client.get(TagID_NUMBER);
                String TagID_NICKNAME = a.getString("닉네임");
                holder.doublecomment_user_comment_content.setText("@" + TagID_NICKNAME + "  " + item_double_comment_sheetmusic.get댓글내용());
            }
            else{
                holder.doublecomment_user_comment_content.setText(item_double_comment_sheetmusic.get댓글내용());
            }

            //좋아요 상태 체크
            JSONArray likeClientList = item_double_comment_sheetmusic.get좋아요목록();
            if(likeClientList.length()==0){
                like_boolean=false;
            }
            for(int i = 0 ; i< likeClientList.length();i++){
                if(likeClientList.get(i).equals(ID_NUMBER)){
                    like_boolean = true;
                    break;
                }
                like_boolean=false;
            }
            //좋아요 상태에 따른 좋아요 표시
            if(like_boolean==true){
                holder.user_comment_like_1.setImageResource(R.drawable.icon_like_selected);
            }
            else{
                holder.user_comment_like_1.setImageResource(R.drawable.icon_like);
            }
            holder.doublecomment_number_of_like.setText(Integer.toString(item_double_comment_sheetmusic.get좋아요목록().length()));


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


    public void show(final int pos, final int commentobjNumber){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("삭제하시겠습니까?");
        builder.setPositiveButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.setNegativeButton("예",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            JSONObject thisArticle = new JSONObject();
                            if(type==0){
                                thisArticle = (JSONObject) array_Article_Sheetmusic.get(ArticleNumber);
                            }
                            else if(type==1){
                                thisArticle = (JSONObject) array_Article_Video.get(ArticleNumber);
                            }
                            else if(type==2){
                                thisArticle = (JSONObject) array_Article_Community.get(ArticleNumber);
                            }
                            JSONArray commentList = thisArticle.getJSONArray("댓글목록");
                            JSONObject thiscomment = (JSONObject) commentList.get(commentobjNumber);
                            JSONArray doublecommentList = thiscomment.getJSONArray("대댓글목록");

                            for (int i = 0; i < doublecommentList.length(); i++) {
                                JSONObject checkdoublecommentobj = doublecommentList.getJSONObject(i);
                                if (checkdoublecommentobj.getInt("아이디넘버") == mDataset.get(pos).get아이디넘버() && checkdoublecommentobj.getLong("작성시간") == mDataset.get(pos).get작성시간()) {
                                    doublecommentList.remove(i);

                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("데이터베이스", DATABASE.toString());
                                    editor.commit();
                                    break;
                                }
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                if(type==0) {
                                    sheetmusicPostActivity.onResume();
                                }
                                else if(type==1){
                                    videoPostActivity.onResume();
                                }
                                else if(type==1){
                                    communityPostActivity.onResume();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();

                    }
                });
        builder.show();

    }




}