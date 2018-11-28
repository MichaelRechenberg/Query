package com.example.conwayying.query;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;

import com.example.conwayying.query.data.LectureDataEntry;
import com.example.conwayying.query.data.QueryAppRepository;
import com.example.conwayying.query.data.entity.Lecture;
import com.example.conwayying.query.data.entity.Lecture;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LectureListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);


        QueryAppRepository repo = new QueryAppRepository(getApplication());
        int classId = getIntent().getIntExtra("ClassId", -1);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set adapter based on data from DB
        Log.d("Foo", "REEEE");
        GetLecturesParams param = new GetLecturesParams();
        param.classId = classId;
        param.context = this;
        param.repo = repo;
        param.recyclerView = recyclerView;
        new GetLectureDataEntryAsyncTask().execute(param);

    }

    public class GetLecturesParams {
        // The repo to use to query for data
        public QueryAppRepository repo;
        // The RecyclerView to set the adapter for
        public RecyclerView recyclerView;
        // Context to use for instantiating the QueryAppRepository
        public Context context;
        // Class Id of the AcademicClass we are getting lectures for
        public int classId;
    }

    // Populates the List of LectureDataEntry objects for the LectureListAdapter and sets
    //  the adapter for the RecyclerView
    // Uses first GetLecturesParams to get repo, recycler view, context, and class id
    private static class GetLectureDataEntryAsyncTask extends AsyncTask<GetLecturesParams, Void, Void> {
        @Override
        protected Void doInBackground(GetLecturesParams...params) {

            QueryAppRepository repo = params[0].repo;
            RecyclerView recyclerView = params[0].recyclerView;
            Context context = params[0].context;
            int classId = params[0].classId;
            Log.d("Foo", "Getting all Lectures and resolved counts for class with id " + classId);

            List<Lecture> lectures = repo.getAllLecturesForClass(classId);

            // No Streams in Java 7 -> Sad Mike
            List<LectureDataEntry> lectureDataEntries = new ArrayList<>();
            for(Lecture lecture : lectures){
                int lectureId = lecture.getLectureId();
                Pair<Integer, Integer> noteResolutionCountPair = repo.getNoteResolvedCountForLecture(lectureId);
                Pair<Integer, Integer> confusionMarkResolutionCountPair = repo.getConfusionMarkResolvedCountForLecture(lectureId);

                Log.d("Foo", Integer.toString(lectureId));
                Log.d("Foo", noteResolutionCountPair.toString());
                Log.d("Foo", confusionMarkResolutionCountPair.toString());

                LectureDataEntry dataEntry = new LectureDataEntry();
                dataEntry.lecture = lecture;
                dataEntry.noteResolvedCountPair = noteResolutionCountPair;
                dataEntry.confusionMarkResolvedCountPair = confusionMarkResolutionCountPair;
                lectureDataEntries.add(dataEntry);
            }


            LectureListAdapter lectureListAdapter = new LectureListAdapter(context, lectureDataEntries);
            recyclerView.setAdapter(lectureListAdapter);
            return null;
        }
    }
}
