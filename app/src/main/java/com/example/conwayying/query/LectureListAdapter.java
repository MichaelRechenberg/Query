package com.example.conwayying.query;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.conwayying.query.data.LectureDataEntry;
import com.example.conwayying.query.data.entity.Lecture;

import java.util.ArrayList;
import java.util.List;

public class LectureListAdapter extends RecyclerView.Adapter<LectureListAdapter.LectureViewHolder> {

    class LectureViewHolder extends RecyclerView.ViewHolder {
        private final TextView classItemView;
        private final Button indicatorView;

        private LectureViewHolder(View itemView) {
            super(itemView);
            classItemView = itemView.findViewById(R.id.textView);
            indicatorView = itemView.findViewById(R.id.indicator);
        }


    }

    private final LayoutInflater mInflater;
    private List<LectureDataEntry> mLectureDataEntries = new ArrayList<>();
    private Context mContext;



    LectureListAdapter(Context context, List<LectureDataEntry> list) {
        mInflater = LayoutInflater.from(context);
        mLectureDataEntries = list;
        mContext = context;
    }

    @Override
    public LectureViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_itemlecture, parent, false);

        Log.d("OnCreateViewHolder", "Added to list");
        return new LectureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LectureViewHolder holder, int position) {
        Log.d("Binding", "Binding view");
        if (mLectureDataEntries != null) {
            final LectureDataEntry dataEntry = mLectureDataEntries.get(position);
            final Lecture current = dataEntry.lecture;

            // TODO: Heather: Use these for displaying colored indicator
            final Pair<Integer, Integer> noteResolvedCountPair = dataEntry.noteResolvedCountPair;
            final Pair<Integer, Integer> confusionMarkResolvedCountPair = dataEntry.confusionMarkResolvedCountPair;

            holder.classItemView.setText(current.getLectureDate().toString());
            holder.classItemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Change the LectureListActivity to what it needs to go to next
                    mContext.startActivity(new Intent(mContext, MainActivity.class).putExtra("LectureId", current.getLectureId()));
                }
            });
            if (confusionMarkResolvedCountPair.first + confusionMarkResolvedCountPair.second > 0) {
                Log.d("Confusion", "" + confusionMarkResolvedCountPair.first);
                if (confusionMarkResolvedCountPair.first / (confusionMarkResolvedCountPair.second + confusionMarkResolvedCountPair.first) > .8) {
                    holder.indicatorView.setBackgroundColor(Color.parseColor("#006400"));
                    //Log.d("Confusion", "here");
                } else {
                    holder.indicatorView.setBackgroundColor(Color.parseColor("#FF0000"));
                    //Log.d("Confusion", "there");
                }
            }
        } else {
            holder.classItemView.setText("No Classes");
        }
    }

    void setClasses(List<LectureDataEntry> classes) {
        mLectureDataEntries = classes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mLectureDataEntries != null)
            return mLectureDataEntries.size();
        else return 0;
    }

}

