package com.jun.akbonara;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_main_item_sheetmusic extends RecyclerView.Adapter<Adapter_main_item_sheetmusic.MyViewHolder> {
    private List<Item_SheetmusicActivity> mDataset;
    private OnItemClickListener mListener = null ;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    //아이템에 들어가는 요소들
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //뷰홀더는 데이터셋의 크기(길이)만큼 반복
        public TextView TextView_ranking;
        public TextView TextView_name_sheetmusic;
        public TextView TextView_name_seller;
        public TextView TextView_price;

        public MyViewHolder(View v) {
            super(v);

            TextView_ranking = v.findViewById(R.id.TextView_ranking);
            TextView_name_sheetmusic = v.findViewById(R.id.TextView_name_sheetmusic);
            TextView_name_seller = v.findViewById(R.id.TextView_name_seller);
            TextView_price = v.findViewById(R.id.TextView_price);

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
    public Adapter_main_item_sheetmusic(List<Item_SheetmusicActivity> myDataset) {
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
        Item_SheetmusicActivity SheetmusicArticleData = mDataset.get(position);

        holder.TextView_ranking.setText(Integer.toString(position+1));
        holder.TextView_name_sheetmusic.setText(SheetmusicArticleData.get글제목());
        holder.TextView_name_seller.setText(SheetmusicArticleData.get닉네임());
        if(SheetmusicArticleData.get가격()==0){
            holder.TextView_price.setText("무료");
        }
        else {
            holder.TextView_price.setText(Integer.toString(SheetmusicArticleData.get가격()) + "원");
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