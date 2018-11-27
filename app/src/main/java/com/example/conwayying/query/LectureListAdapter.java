package com.example.conwayying.query;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.conwayying.query.data.entity.AcademicClass;
import com.example.conwayying.query.data.entity.Lecture;

import java.util.ArrayList;
import java.util.List;

public class LectureListAdapter extends RecyclerView.Adapter<LectureListAdapter.LectureViewHolder> {

    class LectureViewHolder extends RecyclerView.ViewHolder {
        private final TextView classItemView;

        private LectureViewHolder(View itemView) {
            super(itemView);
            classItemView = itemView.findViewById(R.id.textView);
        }


    }

    private final LayoutInflater mInflater;
    private List<Lecture> mClasses = new ArrayList<>();
    private Context mContext;



    LectureListAdapter(Context context, List<Lecture> list) {
        mInflater = LayoutInflater.from(context);
        mClasses = list;
        mContext = context;
    }

    @Override
    public LectureViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        Log.d("OnCreateViewHolder", "Added to list");
        return new LectureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LectureViewHolder holder, int position) {
        Log.d("Binding", "Binding view");
        if (mClasses != null) {
            final Lecture current = mClasses.get(position);
            holder.classItemView.setText(current.getLectureDate().toString());
            holder.classItemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Change the LectureListActivity to what it needs to go to next
                    mContext.startActivity(new Intent(mContext, MainActivity.class).putExtra("LectureId", current.getLectureId()));
                }
            });
        } else {
            holder.classItemView.setText("No Classes");
        }
    }

    void setClasses(List<Lecture> classes) {
        mClasses = classes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mClasses != null)
            return mClasses.size();
        else return 0;
    }

}

