package com.example.conwayying.query;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

public class LectureListActivity extends AppCompatActivity {

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);


        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        QueryAppRepository repo = new QueryAppRepository(getApplication());
        int classId = getIntent().getIntExtra("ClassId", -1);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set adapter based on data from DB
        GetLecturesParams param = new GetLecturesParams();
        param.classId = classId;
        param.repo = repo;
        new GetLectureDataEntryAsyncTask(this, recyclerView).execute(param);
    }

    @Override
    public boolean onSupportNavigateUp(){
        //code it to launch an intent to the activity you want
        //Intent intent = new Intent(getApplicationContext(), ClassListActivity.class);
        //getApplicationContext().startActivity(intent);
        onBackPressed();
        finish();
        return true;
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
    private static class GetLectureDataEntryAsyncTask extends AsyncTask<GetLecturesParams, Void, List<LectureDataEntry>> {

        private Context context;
        private RecyclerView recyclerView;

        public GetLectureDataEntryAsyncTask(Context context, RecyclerView recyclerView){
            this.context = context;
            this.recyclerView = recyclerView;
        }

        @Override
        protected List<LectureDataEntry> doInBackground(GetLecturesParams...params) {

            QueryAppRepository repo = params[0].repo;
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


            return lectureDataEntries;
        }

        @Override
        protected void onPostExecute(List<LectureDataEntry> lectureDataEntries) {
            super.onPostExecute(lectureDataEntries);

            LectureListAdapter lectureListAdapter = new LectureListAdapter(this.context, lectureDataEntries);
            this.recyclerView.setAdapter(lectureListAdapter);

        }
    }
}
