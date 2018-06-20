package com.marko.travelers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marko.travelers.Model.Travel;
import com.marko.travelers.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Travel> mData;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public RecyclerViewAdapter(Context mContext, List <Travel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_travel,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_name.setText(mData.get(position).getDriver().getUserName());
        holder.tv_from.setText(mData.get(position).getFtom());
        holder.tv_to.setText(mData.get(position).get_To());
        holder.tv_date.setText(mData.get(position).get_Date());
        holder.tv_price.setText(String.format("%.2f %s", mData.get(position).getPrice(), mData.get(position).getValute()));
        holder.img.setImageResource(mData.get(position).getDriver().getPhoto());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout item_travel;
        private TextView tv_name;
        private TextView tv_from;
        private TextView tv_to;
        private TextView tv_date;
        private TextView tv_price;
        private ImageView img;

        MyViewHolder(View itemView) {
            super(itemView);

            item_travel = itemView.findViewById(R.id.item_travel_id);
            tv_name = itemView.findViewById(R.id.name_driver);
            tv_from = itemView.findViewById(R.id.from);
            tv_to = itemView.findViewById(R.id.to);
            tv_date = itemView.findViewById(R.id.Date);
            tv_price = itemView.findViewById(R.id.price);
            img = itemView.findViewById(R.id.img_driver);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
