package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_main_item_sheetmusic extends RecyclerView.Adapter<Adapter_main_item_sheetmusic.MyViewHolder> {
    private List<main_item_sheetmusic> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    //아이템에 들어가는 요소들
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //뷰홀더는 데이터셋의 크기(길이)만큼 반복
        public TextView TextView_ranking;
        public TextView TextView_name_sheetmusic;
        public TextView TextView_name_seller;

        public MyViewHolder(View v) {
            super(v);
            TextView_ranking = v.findViewById(R.id.TextView_ranking);
            TextView_name_sheetmusic = v.findViewById(R.id.TextView_name_sheetmusic);
            TextView_name_seller = v.findViewById(R.id.TextView_name_seller);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Adapter_main_item_sheetmusic(List<main_item_sheetmusic> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override // 하나의 아이템 전체목록(요소들의 집합)
    public Adapter_main_item_sheetmusic.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_item_sheetmusic, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // 값을 셋팅한다.
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        main_item_sheetmusic ClinetInfo = mDataset.get(position);

        holder.TextView_ranking.setText(ClinetInfo.get닉네임());
        holder.TextView_name_sheetmusic.setText(ClinetInfo.get아이디());
        holder.TextView_name_seller.setText(ClinetInfo.get비밀번호());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        //삼항 연산자 mDataset이 null이면 0 return, 아니면 size리턴
        return mDataset == null ? 0 : mDataset.size();
    }
}