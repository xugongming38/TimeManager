package com.xugongming38.timemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.base.OnItemOnClickListener;
import com.xugongming38.timemanager.datamodel.Work;


import java.util.List;

/**
 * Created by dell on 2018/4/20.
 */
public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.TodayViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Work> mWorkList;

    private OnItemOnClickListener mOnItemOnClickListener;

    public void setOnItemOnClickListener(OnItemOnClickListener listener){
        this.mOnItemOnClickListener = listener;
    }

    public TodayAdapter(Context context, List<Work> mWorkList){
        mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mWorkList = mWorkList;
    }
    @Override
    public TodayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TodayViewHolder(mLayoutInflater.inflate(R.layout.layout_today_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final TodayViewHolder holder, final int position) {

        if(position==0)
            mWorkList.get(position).setState(0);

        if(mWorkList.get(position).getState()==0){
            holder.mFirstWork.setImageResource(R.drawable.circle_doing);
        }
        else
            holder.mFirstWork.setImageResource(R.drawable.circle_undo);

        holder.mWorkTitle.setText(mWorkList.get(position).getTitle());
        holder.mImportant.setProgress(mWorkList.get(position).getImportant());
        holder.mDeadLine.setText("最后期限: "+mWorkList.get(position).getDeadLine());

        if(mWorkList.get(position).getState()==0)
            holder.mStatus.setImageResource(R.drawable.start);
        else
            holder.mStatus.setImageResource(R.drawable.pause);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemOnClickListener!=null)
                    mOnItemOnClickListener.onItemOnClick(holder.itemView,position,mWorkList.get(position).getId());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mOnItemOnClickListener!=null)
                    mOnItemOnClickListener.onItemLongOnClick(holder.itemView,position,mWorkList.get(position).getId());
                return false;
            }
        });
        holder.mStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position==0)
                {
                    if(mWorkList.get(position).getState()==0) {
                        mWorkList.get(position).setState(1);
                        holder.mStatus.setImageResource(R.drawable.pause);
                        holder.mFirstWork.setImageResource(R.drawable.circle_undo);

                    }
                    else {
                        mWorkList.get(position).setState(0);
                        holder.mStatus.setImageResource(R.drawable.start);
                        holder.mFirstWork.setImageResource(R.drawable.circle_doing);
                    }
                    //notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(mContext,"只有列表首任务才能暂停！",Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(mContext,"---"+position,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mWorkList.size();
    }

    public static class TodayViewHolder extends RecyclerView.ViewHolder{


        ImageView mFirstWork;
        TextView mWorkTitle;
        RatingBar mImportant;
        TextView mDeadLine;
        ImageView  mStatus;

        TodayViewHolder(View view){
            super(view);
            mFirstWork = (ImageView) view.findViewById(R.id.firstWork);
            mWorkTitle = (TextView) view.findViewById(R.id.workTitle);

            mImportant = (RatingBar) view.findViewById(R.id.important);
            mDeadLine = (TextView) view.findViewById(R.id.deadLine);

            mStatus = (ImageView) view.findViewById(R.id.status);
        }
    }

    public void removeItem(int pos){
        mWorkList.remove(pos);
        notifyItemRemoved(pos);
    }
}