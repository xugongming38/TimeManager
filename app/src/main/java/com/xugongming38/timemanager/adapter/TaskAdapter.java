package com.xugongming38.timemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.datamodel.Work;

import java.util.List;

/**
 * Created by dell on 2018/4/20.
 */


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Work> mWorkList;

    public TaskAdapter(Context context, List<Work> mWorkList){
        mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mWorkList = mWorkList;
    }
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskViewHolder(mLayoutInflater.inflate(R.layout.layout_today_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final TaskViewHolder holder, final int position) {

        if(position==0){
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

    }

    @Override
    public int getItemCount() {
        return mWorkList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{


        ImageView mFirstWork;
        TextView mWorkTitle;
        RatingBar mImportant;
        TextView mDeadLine;
        ImageView  mStatus;

        TaskViewHolder(View view){
            super(view);
            mFirstWork = (ImageView) view.findViewById(R.id.firstWork);
            mWorkTitle = (TextView) view.findViewById(R.id.workTitle);

            mImportant = (RatingBar) view.findViewById(R.id.important);
            mDeadLine = (TextView) view.findViewById(R.id.deadLine);

            mStatus = (ImageView) view.findViewById(R.id.status);
        }
    }
}
