package com.example.conwayying.query;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.conwayying.query.data.entity.AcademicClass;

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
    private List<AcademicClass> mClasses;

    ClassListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ClassViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        if (mClasses != null) {
            AcademicClass current = mClasses.get(position);
            holder.classItemView.setText(((AcademicClass) current).getClassTitle());
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
