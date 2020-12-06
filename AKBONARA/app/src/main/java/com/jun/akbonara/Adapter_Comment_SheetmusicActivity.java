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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Adapter_Comment_SheetmusicActivity extends RecyclerView.Adapter<Adapter_Comment_SheetmusicActivity.MyViewHolder>  {
    private List<Item_Comment_Sheetmusic> mDataset;
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

    String string_Double_Comment_Content;

    int ArticleNumber;
    int ID_NUMBER;

    SharedPreferences preferences;

    //Double_Comment에 넘겨주는 댓글객체 번호
    int commentobjNumber;

    int ListPosition;

    //댓글 옵션 버튼 불린
    boolean MOREBUTTON = false;
    //댓글 달기(수정모드 or 답글모드) 불린
    boolean EditmodeOrReplymode;
    //좋아요 불린
    boolean like_boolean=false;

    List<Item_Double_Comment_Sheetmusic> Item_Double_Comment_Sheetmusic_List= new ArrayList<>();

    SheetmusicPostActivity sheetmusicPostActivity = new SheetmusicPostActivity();
    VideoPostActivity videoPostActivity = new VideoPostActivity();
    CommunityPostActivity communityPostActivity = new CommunityPostActivity();

    int type;



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
        public TextView user_comment_content;
        public TextView number_of_like;
        public TextView number_of_doublecomment;
        public LinearLayout expand_btn_comment;
        public TextView AddDoubleComment_btn;
        public LinearLayout AddDoubleComment;
        public ImageView Close_AddDoubleComment;
        public TextView AddDoubleComment_register_button;
        public EditText Double_Comment_Content;
        public ImageView image_expand_shrink;
        public TextView letter_show_hide;
        public ImageView more_btn;
        public LinearLayout EditOrDelete_btn;
        public Button Edit_btn,Delete_btn;
        public TextView title_commentfuction;
        public ImageView user_comment_like_1;

        //답글 더보기 불린
        boolean ExpandOrShrink=false;

        public RecyclerView RecyclerView_doublecomment;

        public MyViewHolder(View v) {

            super(v);

            Log.d(TAG, "MyViewHolder");
            ImageView_profile = v.findViewById(R.id.ImageView_profile);
            TextView_name_seller = v.findViewById(R.id.TextView_name_seller);
            upload_time = v.findViewById(R.id.upload_time);
            user_comment_content = v.findViewById(R.id.user_comment_content);
            number_of_like = v.findViewById(R.id.number_of_like);
            number_of_doublecomment = v.findViewById(R.id.number_of_doublecomment);
            expand_btn_comment = v.findViewById(R.id.expand_btn_comment);
            AddDoubleComment_btn = v.findViewById(R.id.AddDoubleComment_btn);
            AddDoubleComment = v.findViewById(R.id.AddDoubleComment);
            Close_AddDoubleComment = v.findViewById(R.id.Close_AddDoubleComment);
            AddDoubleComment_register_button = v.findViewById(R.id.AddDoubleComment_register_button);
            Double_Comment_Content = v.findViewById(R.id.Double_Comment_Content);
            image_expand_shrink = v.findViewById(R.id.image_expand_shrink);
            letter_show_hide = v.findViewById(R.id.letter_show_hide);
            more_btn=v.findViewById(R.id.more_btn);
            EditOrDelete_btn = v.findViewById(R.id.EditOrDelete_btn);
            Edit_btn = v.findViewById(R.id.Edit_btn);
            Delete_btn = v.findViewById(R.id.Delete_btn);
            title_commentfuction = v.findViewById(R.id.title_commentfuction);
            user_comment_like_1 = v.findViewById(R.id.user_comment_like_1);

            RecyclerView_doublecomment = v.findViewById(R.id.RecyclerView_doublecomment);

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
                    AddDoubleComment.setVisibility(View.VISIBLE);
                    Double_Comment_Content.setText(mDataset.get(pos).get댓글내용());
                    title_commentfuction.setText("댓글 수정");
                    EditmodeOrReplymode = false;

                }
            });

            //삭제버튼
            Delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    show(pos,type);
                }
            });


            //답글달기 버튼
            AddDoubleComment_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    else{
                        AddDoubleComment.setVisibility(View.VISIBLE);
                        title_commentfuction.setText("답글 작성");
                        Double_Comment_Content.setText("");
                        EditmodeOrReplymode = true;
                    }

                }
            });

            // 취소 버튼
            Close_AddDoubleComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddDoubleComment.setVisibility(View.GONE);
                    Double_Comment_Content.setText("");
                }
            });

            //더보기 버튼
            expand_btn_comment.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
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

                        JSONObject comment = new JSONObject();
                        for (int i = 0; i < commentList.length(); i++) {
                            comment = commentList.getJSONObject(i);
                            if (comment.getInt("아이디넘버") == mDataset.get(pos).get아이디넘버() && comment.getLong("작성시간") == mDataset.get(pos).get작성시간()) {
                                break;
                            }
                        }

                        if(ExpandOrShrink==false){
                            comment.put("더보기불린",true);
                        }
                        else{
                            comment.put("더보기불린",false);
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


            //작성버튼 ( 답글 작성/ 댓글 수정 )
            AddDoubleComment_register_button.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    AddDoubleComment.setVisibility(View.GONE);
                    string_Double_Comment_Content = Double_Comment_Content.getText().toString();
                    Double_Comment_Content.setText("");
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

                        JSONObject comment = new JSONObject();
                        for (int i = 0; i < commentList.length(); i++) {
                            comment = commentList.getJSONObject(i);
                            if (comment.getInt("아이디넘버") == mDataset.get(pos).get아이디넘버() && comment.getLong("작성시간") == mDataset.get(pos).get작성시간()) {
                                break;
                            }
                        }

                        // 답글 작성일때
                        if(EditmodeOrReplymode==true) {

                            JSONArray doublecommentList = comment.getJSONArray("대댓글목록");
                            JSONArray newjsonArray = new JSONArray();

                            JSONObject a = new JSONObject();

                            a.put("아이디넘버", ID_NUMBER);
                            a.put("댓글내용", string_Double_Comment_Content);
                            a.put("좋아요목록", newjsonArray);
                            a.put("작성시간", time);
                            a.put("태그아이디넘버",-1);

                            doublecommentList.put(a);


                            //게시물 작성자에게 알림 데이터 등록
                            JSONObject replyedClient = array_client.getJSONObject(comment.getInt("아이디넘버"));
                            JSONArray haha = replyedClient.getJSONArray("알림목록");
                            JSONObject b = new JSONObject();

                            String message = "님이 회원님의 댓글에 댓글을 남기셨습니다.";

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



                            if(ID_NUMBER==comment.getInt("아이디넘버")) {
                                //댓글작성 알림 띄우기
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject currentClient = array_client.getJSONObject(ID_NUMBER);
                                            String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
                                            String SERVER_KEY = "AAAAYaTeLd0:APA91bHjijFYatJoNxtkE7bLc3tETYpb1Wepw3334akmTWR3a5SajgMxcSyCnvFD3D6TPl2Ks6ZMdKN2ZwfXCvN2aXQBo87oqWuRQ8gyzeD2Iw3G9tFrZADW4lMQpHUK5TOK58rJail4";

                                            String message = currentClient.getString("닉네임") + "님이 회원님의 댓글에 댓글을 남기셨습니다.";
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
                        //댓글 수정일 때
                        else{
                            comment.put("댓글내용",string_Double_Comment_Content);
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

                            JSONObject comment;
                            JSONArray likeClientList = new JSONArray();
                            for (int i = 0; i < commentList.length(); i++) {
                                comment = commentList.getJSONObject(i);
                                if (comment.getInt("아이디넘버") == mDataset.get(pos).get아이디넘버() && comment.getLong("작성시간") == mDataset.get(pos).get작성시간()) {
                                    likeClientList = comment.getJSONArray("좋아요목록");
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
    public Adapter_Comment_SheetmusicActivity(int type,List<Item_Comment_Sheetmusic> myDataset, JSONObject database, int articleNumber, int idNumber
            , SharedPreferences sharedPreferences,Context context,SheetmusicPostActivity sheetmusicPostActivity,VideoPostActivity videoPostActivity,
                                              CommunityPostActivity communityPostActivity) {


        preferences = sharedPreferences;
        ArticleNumber = articleNumber;
        ID_NUMBER = idNumber;
        this.type = type;

        if(type==0) {
            this.sheetmusicPostActivity = sheetmusicPostActivity;
        }
        else if(type==1){
            this.videoPostActivity = videoPostActivity;
        }
        else if(type==2){
            this.communityPostActivity = communityPostActivity;
        }

        this.context = context;

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
    public Adapter_Comment_SheetmusicActivity.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                              int viewType) {

        Log.d(TAG, "onCreateViewHolder");
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sheetmusic_comment, parent, false);


        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // 값을 셋팅한다.
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final MyViewHolder holder,int position) {


        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        //리스트 정렬 순서
        ListPosition = position;

        Item_Comment_Sheetmusic item_comment_sheetmusic = mDataset.get(position);

        //회원객체 아이디넘버로 매칭
        try {
            client = (JSONObject) array_client.get(item_comment_sheetmusic.get아이디넘버());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //각 아이템 요소들 정보 매칭
        try {
            //로그인한 회원과 댓글 작성자가 다르다면 수정삭제버튼 안보이기
            if(ID_NUMBER!=item_comment_sheetmusic.get아이디넘버()){
                holder.more_btn.setVisibility(View.GONE);
            }

            if(client.getString("프로필사진").equals("") ){
                holder.ImageView_profile.setImageResource(R.drawable.ic_icon_basicprofile);
            }
            else{
                holder.ImageView_profile.setImageURI(Uri.parse(client.getString("프로필사진")));
            }
            holder.TextView_name_seller.setText(client.getString("닉네임"));
            holder.upload_time.setText(timeCalculator.formatTimeString(item_comment_sheetmusic.get작성시간()));
            holder.user_comment_content.setText(item_comment_sheetmusic.get댓글내용());

            //좋아요 상태 체크
            JSONArray likeClientList = item_comment_sheetmusic.get좋아요목록();
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
            holder.number_of_like.setText(Integer.toString(item_comment_sheetmusic.get좋아요목록().length()));

            if(item_comment_sheetmusic.get대댓글목록().length()==0){
                holder.expand_btn_comment.setVisibility(View.GONE);
            }
            else{
                holder.number_of_doublecomment.setText("답글 "+item_comment_sheetmusic.get대댓글목록().length()+"개 ");
            }

            holder.ExpandOrShrink = item_comment_sheetmusic.get더보기불린();
            //더보기 버튼
            if(holder.ExpandOrShrink==false){
                holder.RecyclerView_doublecomment.setVisibility(View.GONE);
                holder.image_expand_shrink.setImageResource(R.drawable.icon_expand);
                holder.letter_show_hide.setText("보기");
            }
            else{
                holder.RecyclerView_doublecomment.setVisibility(View.VISIBLE);
                holder.image_expand_shrink.setImageResource(R.drawable.icon_shrink);
                holder.letter_show_hide.setText("가리기");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //대댓글 리스트 만들기
        try {
            //대댓글 리스트 초기화
            Item_Double_Comment_Sheetmusic_List = new ArrayList<>();

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
            JSONObject comment = new JSONObject();
            for(int i =0; i < commentList.length();i++){
                comment = commentList.getJSONObject(i);
                if(comment.getInt("아이디넘버")== mDataset.get(ListPosition).get아이디넘버() && comment.getLong("작성시간")== mDataset.get(ListPosition).get작성시간()){
                    commentobjNumber = i;
                    break;
                }
            }
            JSONArray doublecommentList = comment.getJSONArray("대댓글목록");

            for(int i =0 ; i<doublecommentList.length();i++){
                JSONObject obj =  doublecommentList.getJSONObject(i);
                Item_Double_Comment_Sheetmusic item_double_comment_sheetmusic = new Item_Double_Comment_Sheetmusic(obj.getInt("아이디넘버"),obj.getString("댓글내용"),obj.getJSONArray("좋아요목록"),obj.getLong("작성시간"),obj.getInt("태그아이디넘버"));
                Item_Double_Comment_Sheetmusic_List.add(item_double_comment_sheetmusic);
            }
            Item_Double_Comment_Sheetmusic_List.sort(new TimeAscending());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Adapter_Double_Comment_SheetmusicActivity adapter = new Adapter_Double_Comment_SheetmusicActivity(type,Item_Double_Comment_Sheetmusic_List,DATABASE,
                ArticleNumber,ID_NUMBER,preferences,commentobjNumber,sheetmusicPostActivity,videoPostActivity,communityPostActivity
                ,context);

        holder.RecyclerView_doublecomment.setHasFixedSize(true);
        holder.RecyclerView_doublecomment.setLayoutManager(new LinearLayoutManager(context));
        holder.RecyclerView_doublecomment.setAdapter(adapter);


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


    //좋아요순 정렬 메소드
    public class TimeAscending implements  Comparator<Item_Double_Comment_Sheetmusic>{
        @Override
        public int compare(Item_Double_Comment_Sheetmusic o1, Item_Double_Comment_Sheetmusic o2) {
            //오래된순
            return o1.get작성시간() <= o2.get작성시간() ? -1 : 1;

        }
    }


    //삭제 다이얼로그
   public void show(final int pos, final int type){
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
                           long time = System.currentTimeMillis();
                           JSONObject thisArticle = null;
                           if(type==0) {
                               thisArticle = (JSONObject) array_Article_Sheetmusic.get(ArticleNumber);
                           }
                           if(type==1){
                               thisArticle = (JSONObject) array_Article_Video.get(ArticleNumber);
                           }
                           if(type==2){
                               thisArticle = (JSONObject) array_Article_Community.get(ArticleNumber);
                           }
                           JSONArray commentList = thisArticle.getJSONArray("댓글목록");

                           JSONObject comment = new JSONObject();
                           for (int i = 0; i < commentList.length(); i++) {
                               comment = commentList.getJSONObject(i);
                               if (comment.getInt("아이디넘버") == mDataset.get(pos).get아이디넘버() && comment.getLong("작성시간") == mDataset.get(pos).get작성시간()) {
                                   commentList.remove(i);

                                   thisArticle.put("댓글수",commentList.length());

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
                               else if(type==2){
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