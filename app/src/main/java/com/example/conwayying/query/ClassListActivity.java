package com.example.conwayying.query;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;

import com.example.conwayying.query.data.AcademicClassDataEntry;
import com.example.conwayying.query.data.QueryAppRepository;
import com.example.conwayying.query.data.entity.AcademicClass;
import com.example.conwayying.query.data.entity.AcademicClassDao;
import com.example.conwayying.query.data.entity.ConfusionMark;
import com.example.conwayying.query.data.entity.Lecture;
import com.example.conwayying.query.data.entity.Note;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClassListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        QueryAppRepository repo = new QueryAppRepository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Populate test DB (if we haven't already)
        new PopulateDataInitiallyAsyncTask().execute(repo);

        // Set the adapter for the RecyclerView in an AsyncTask that queries the DB
        GetClassesParams getClassesParams = new GetClassesParams();
        getClassesParams.repo = repo;
        new GetAcademicClassDataEntryAsyncTask(this, recyclerView).execute(getClassesParams);
    }

    // TODO: if no classes exist, insert all our mock data (questions, notes, etc.)
    private static class PopulateDataInitiallyAsyncTask extends AsyncTask<QueryAppRepository, Void, Void> {


        @Override
        protected Void doInBackground(final QueryAppRepository... params) {
            QueryAppRepository repo = params[0];
            Log.d("Foo", "We background");

            // If there are no classes in the DB, add some demo data to the DB
            if (repo.getAllClasses().size() == 0){

                Log.d("Foo", "Adding initial demo data to DB");


                AcademicClass cs465Class = new AcademicClass("CS 465");
                AcademicClass cs357Class = new AcademicClass("CS 357");
                AcademicClass cs411Class = new AcademicClass("CS 411");

                try {
                    DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                    Date d = null;
                    Lecture lecture = null;

                    // CS 465 has 3 lectures on Oct 20, Oct 25, and Oct 27
                    long cs465ClassId = repo.insert(cs465Class);
                    d = df.parse("10-20-2018");
                    lecture = new Lecture(d, (int) cs465ClassId);
                    repo.insert(lecture);

                    d = df.parse("10-25-2018");
                    lecture = new Lecture(d, (int) cs465ClassId);
                    long demoLectureId = repo.insert(lecture);

                    d = df.parse("10-27-2018");
                    lecture = new Lecture(d, (int) cs465ClassId);
                    repo.insert(lecture);


                    // CS 357 has 2 lectures on Oct 20, and Oct 23
                    long cs357ClassId = repo.insert( cs357Class);
                    d = df.parse("10-20-2018");
                    lecture = new Lecture(d, (int) cs357ClassId);
                    repo.insert(lecture);

                    d = df.parse("10-23-2018");
                    lecture = new Lecture(d, (int) cs357ClassId);
                    repo.insert(lecture);


                    // CS 411 has 8 lectures on Oct 20, 21, 22, 23, 24, 25, 26, 27
                    long cs411ClassId = repo.insert( cs411Class);
                    d = df.parse("10-20-2018");
                    lecture = new Lecture(d, (int) cs411ClassId);
                    repo.insert(lecture);

                    d = df.parse("10-21-2018");
                    lecture = new Lecture(d, (int) cs411ClassId);
                    repo.insert(lecture);

                    d = df.parse("10-22-2018");
                    lecture = new Lecture(d, (int) cs411ClassId);
                    repo.insert(lecture);

                    d = df.parse("10-23-2018");
                    lecture = new Lecture(d, (int) cs411ClassId);
                    repo.insert(lecture);

                    d = df.parse("10-24-2018");
                    lecture = new Lecture(d, (int) cs411ClassId);
                    repo.insert(lecture);

                    d = df.parse("10-25-2018");
                    lecture = new Lecture(d, (int) cs411ClassId);
                    repo.insert(lecture);

                    d = df.parse("10-26-2018");
                    lecture = new Lecture(d, (int) cs411ClassId);
                    repo.insert(lecture);

                    d = df.parse("10-27-2018");
                    lecture = new Lecture(d, (int) cs411ClassId);
                    repo.insert(lecture);

                    // Add some confusion marks and notes for Oct 25 lecture of CS 465
                    // 3 notes are unresolved, 1 is resolved
                    // magic slide number of 7 for demo purposes
                    int BOGUS_SLIDE_NUMBER = 7;
                    repo.insert(new Note((int) demoLectureId, "What is 2 + 2?", df.parse("10-25-2018 12:34"), BOGUS_SLIDE_NUMBER));
                    repo.insert(new Note((int) demoLectureId, "What is the meaning of life?", df.parse("10-25-2018 12:40"), BOGUS_SLIDE_NUMBER));
                    repo.insert(new Note((int) demoLectureId, "TODO: Look at the definition of 'affordance'", df.parse("10-25-2018 12:45"), BOGUS_SLIDE_NUMBER));
                    Note n = new Note((int) demoLectureId, "How to design for mobile?", df.parse("10-25-2018 12:46"), BOGUS_SLIDE_NUMBER);
                    n.setIsResolved(true);
                    repo.insert(n);

                    // 5 timestamps
                    // 2 of which are WTF and unresolved
                    // 3 of which are intervals and are resolved
                    DateFormat cmDF = new SimpleDateFormat("MM-dd-yyyy HH:mm");
                    repo.insert(new ConfusionMark(cmDF.parse("10-25-2018 12:30"), (int) demoLectureId, BOGUS_SLIDE_NUMBER));
                    repo.insert(new ConfusionMark(cmDF.parse("10-25-2018 12:37"), (int) demoLectureId, BOGUS_SLIDE_NUMBER));

                    ConfusionMark cm = new ConfusionMark(cmDF.parse("10-25-2018 12:31"), (int) demoLectureId, BOGUS_SLIDE_NUMBER);
                    cm.setIsResolved(true);
                    repo.insert(cm);

                    cm = new ConfusionMark(cmDF.parse("10-25-2018 12:37"), (int) demoLectureId, BOGUS_SLIDE_NUMBER);
                    cm.setIsResolved(true);
                    repo.insert(cm);

                    cm = new ConfusionMark(cmDF.parse("10-25-2018 12:40"), (int) demoLectureId, BOGUS_SLIDE_NUMBER);
                    cm.setIsResolved(true);
                    repo.insert(cm);

                    Log.d("Foo", "All functional prototype initial data added to DB");

                }
                catch (ParseException e){
                    Log.e("Foo", "Bad parse yo");
                    Log.e("Foo", e.toString());
                }
            }
            else{
                Log.d("Foo", "Using data in DB that is currently on device");
            }


            return null;
        }
    }

    public class GetClassesParams {
        // The repo to use to query for data
        public QueryAppRepository repo;
    }

    // Populates the List of AcademicClassDataEntry objects for the ClassListAdapter and sets
    //  the adapter for the RecyclerView
    // Uses first GetClassParams to get repo, recycler view, and context
    private static class GetAcademicClassDataEntryAsyncTask extends AsyncTask<GetClassesParams, Void, List<AcademicClassDataEntry>> {

        private Context context;
        private RecyclerView recyclerView;

        public GetAcademicClassDataEntryAsyncTask(Context context, RecyclerView recyclerView){
            this.context = context;
            this.recyclerView = recyclerView;
        }

        @Override
        protected List<AcademicClassDataEntry> doInBackground(GetClassesParams... getClassesParams) {
            Log.d("Foo", "Getting all AcademicClasses and resolved counts");

            QueryAppRepository repo = getClassesParams[0].repo;
            List<AcademicClass> academicClasses = repo.getAllClasses();

            // No Streams in Java 7 -> Sad Mike
            List<AcademicClassDataEntry> academicClassDataEntries = new ArrayList<>();
            for(AcademicClass academicClass : academicClasses){
                int classId = academicClass.getClassId();
                Pair<Integer, Integer> noteResolutionCountPair = repo.getNoteResolvedCountForClass(classId);
                Pair<Integer, Integer> confusionMarkResolutionCountPair = repo.getConfusionMarkResolvedCountForClass(classId);

                Log.d("Foo", Integer.toString(classId));
                Log.d("Foo", noteResolutionCountPair.toString());
                Log.d("Foo", confusionMarkResolutionCountPair.toString());

                AcademicClassDataEntry dataEntry = new AcademicClassDataEntry();
                dataEntry.academicClass = academicClass;
                dataEntry.noteResolvedCountPair = noteResolutionCountPair;
                dataEntry.confusionMarkResolvedCountPair = confusionMarkResolutionCountPair;
                academicClassDataEntries.add(dataEntry);
            }


            return academicClassDataEntries;
        }

        @Override
        protected void onPostExecute(List<AcademicClassDataEntry> academicClassDataEntries) {
            super.onPostExecute(academicClassDataEntries);

            ClassListAdapter classListAdapter = new ClassListAdapter(this.context, academicClassDataEntries);
            this.recyclerView.setAdapter(classListAdapter);
        }
    }
}
