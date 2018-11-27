package com.example.conwayying.query;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private List<AcademicClass> mClasses = new ArrayList<>();
    private Context mContext;



    ClassListAdapter(Context context, List<AcademicClass> list) {
        mInflater = LayoutInflater.from(context);
        mClasses = list;
        mContext = context;
    }

    @Override
    public ClassViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ClassViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        Log.d("Binding", "Binding view");
        if (mClasses != null) {
            final AcademicClass current = mClasses.get(position);
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

    void setClasses(List<AcademicClass> classes) {
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
