package com.example.conwayying.query;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.conwayying.query.data.AcademicClassDataEntry;
import com.example.conwayying.query.data.entity.AcademicClass;

import java.util.ArrayList;
import java.util.List;

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ClassViewHolder> {

    class ClassViewHolder extends RecyclerView.ViewHolder {
        private final TextView classItemView;

        private ClassViewHolder(View itemView) {
            super(itemView);
            classItemView = itemView.findViewById(R.id.textView);
        }


    }

    private final LayoutInflater mInflater;
    private List<AcademicClassDataEntry> mClassDataEntries = new ArrayList<>();
    private Context mContext;



    ClassListAdapter(Context context, List<AcademicClassDataEntry> list) {
        mInflater = LayoutInflater.from(context);
        mClassDataEntries = list;
        mContext = context;
    }

    @Override
    public ClassViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ClassViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        if (mClassDataEntries != null) {
            final AcademicClassDataEntry dataEntry = mClassDataEntries.get(position);
            final AcademicClass current = dataEntry.academicClass;

            // TODO: Heather use these for the colored indicator
            final Pair<Integer, Integer> noteResolvedCountPair = dataEntry.noteResolvedCountPair;
            final Pair<Integer, Integer> confusionMarkResolvedCountPair = dataEntry.confusionMarkResolvedCountPair;

            holder.classItemView.setText(current.getClassTitle());
            holder.classItemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("Clicked", "Hello");
                    mContext.startActivity(new Intent(mContext, LectureListActivity.class).putExtra("ClassId", current.getClassId()));
                }
            });
        } else {
            holder.classItemView.setText("No Classes");
        }
    }

    void setClasses(List<AcademicClassDataEntry> classes) {
        mClassDataEntries = classes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mClassDataEntries != null)
            return mClassDataEntries.size();
        else return 0;
    }

}
