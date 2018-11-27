package com.example.conwayying.query;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.conwayying.query.data.QueryAppRepository;
import com.example.conwayying.query.data.entity.AcademicClass;
import com.example.conwayying.query.data.entity.AcademicClassDao;
import com.example.conwayying.query.data.entity.Lecture;
import com.example.conwayying.query.data.entity.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClassListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        List<AcademicClass> mClasses = new ArrayList<>();
        QueryAppRepository repo = new QueryAppRepository(getApplication());

        //new PopulateDbAsync(new QueryAppRepository(getApplication())).execute();
        new PopulateDbAsync().execute(repo);
        Log.d("Populated", "We populated");

        AcademicClass class1 = new AcademicClass("CS 465");
        class1.setClassId(0);
        AcademicClass class2 = new AcademicClass("CS 233");
        class2.setClassId(1);
        mClasses.add(class1);
        mClasses.add(class2);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        ClassListAdapter adapter = new ClassListAdapter(this, mClasses);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private static class PopulateDbAsync extends AsyncTask<QueryAppRepository, Void, Void> {

        //private final QueryAppRepository repo;

        //PopulateDbAsync(QueryAppRepository db) {
        //    repo = db;
        //}

        @Override
        protected Void doInBackground(final QueryAppRepository... params) {
            QueryAppRepository repo = params[0];
            Log.d("Background", "We background");
            Log.d("Classes", Integer.toString(repo.getAllClasses().size()));
            AcademicClass class1 = new AcademicClass("CS 465");
            long classId = repo.insert(class1);
            Lecture lecture = new Lecture(new Date(), (int) classId);
            long lectureId = repo.insert(lecture);
            repo.insert(new Note((int) lectureId, "Conway is cool"));
            repo.insert(new Note((int) lectureId, "Mike is cool"));
            repo.insert(new Note((int) lectureId, "Heather is cool"));

            return null;
        }
    }
}
