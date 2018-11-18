package com.example.conwayying.query;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.conwayying.query.data.AcademicClassRepository;
import com.example.conwayying.query.data.LectureDatabase;
import com.example.conwayying.query.data.LectureRepository;
import com.example.conwayying.query.data.entity.AcademicClass;
import com.example.conwayying.query.data.entity.Lecture;

import java.util.Calendar;


// Test Room code using a dummy AsyncTask (since you cannot query Room
//   in the main UI thread (unless the Repository returns a LiveData
//   object, where you use the Observer pattern)
class TestDBParams {
    public Application application;
    public AcademicClass academicClass;
}

class TestDBAsyncTask extends AsyncTask<TestDBParams, Void, Long> {
    protected Long doInBackground(TestDBParams... params) {

        AcademicClassRepository repo = null;
        try {
            repo = new AcademicClassRepository(params[0].application);
        }
        catch(Exception e) {
            Log.e("Foo", "Error in testing Room code");
            Log.e("Foo", e.toString());
            throw e;
        }

        for (TestDBParams param : params){
            repo.insert(param.academicClass);
        }

        for(AcademicClass theClass : repo.getAllClasses()){
            Log.d("Foo",  "Class id " + theClass.getClassId() + " -> title " + theClass.getClassTitle());
        }

        Log.d("Foo", repo.getAcademicClass(3).toString());

        LectureRepository lectureRepo = new LectureRepository(params[0].application);
        Lecture testLecture = new Lecture(Calendar.getInstance().getTime(), 1);
        try {
           lectureRepo.insert(testLecture);

           for(Lecture lecture : lectureRepo.getAllLecturesForClass(1)){
               Log.d("Foo", "Lecture id " + lecture.getLectureId() + " was given on " + lecture.getLectureDate().toString() + " and is assoc w/ class id " + lecture.getClassId());
           }
        }
        catch (Exception e){
            Log.e("Foo", "Error in testing Lecture room code");
            Log.e("Foo", e.toString());
            throw e;
        }

        return new Long(0);
    }
}


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: make an entity, DAO, and repo for each of [Lecture, ConfusionTime, Notes/Questions]

        // Do a quick and dirty test of our Room code
        TestDBParams params = new TestDBParams();
        params.application = getApplication();
        params.academicClass = new AcademicClass("FOO BAR BAZ");
        new TestDBAsyncTask().execute(params);


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
