package com.example.conwayying.query;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.conwayying.query.data.QueryAppRepository;
import com.example.conwayying.query.data.entity.AcademicClass;
import com.example.conwayying.query.data.entity.Lecture;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LectureListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);

        List<Lecture> mClasses = new ArrayList<>();
        //QueryAppRepository repo = new QueryAppRepository(getApplication());

        Lecture class1 = new Lecture(new Date(), 1);
        Lecture class2 = new Lecture(new Date(), 1);
        mClasses.add(class1);
        mClasses.add(class2);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LectureListAdapter adapter = new LectureListAdapter(this, mClasses);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
