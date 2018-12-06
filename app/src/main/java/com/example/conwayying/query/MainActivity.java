package com.example.conwayying.query;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.conwayying.query.data.ButtonsFragment;
import com.example.conwayying.query.data.LectureSlidesFragment;
import com.example.conwayying.query.data.QuestionsListFragment;
import com.example.conwayying.query.data.QueryAppRepository;
import com.example.conwayying.query.data.TimestampsFragment;
import com.example.conwayying.query.data.TimestampsListFragment;

import java.sql.Time;


public class MainActivity extends AppCompatActivity implements QuestionsListFragment.OnFragmentInteractionListener, TimestampsListFragment.OnFragmentInteractionListener,
        TimestampsFragment.OnFragmentInteractionListener, LectureSlidesFragment.OnFragmentInteractionListener,
        ButtonsFragment.GetSlideNumberInterface, TimestampsListFragment.TimestampClicked, QuestionsListFragment.QuestionClicked,
        ButtonsFragment.RefreshTimestampsInterface, ButtonsFragment.RefreshQuestionsInterface {

    private TextView mTextMessage;
    private QueryAppRepository queryAppRepository;
    private ActionBar actionBar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            int lectureId = getIntent().getIntExtra("LectureId", -1);
            QuestionsListFragment mlf = QuestionsListFragment.newInstance(queryAppRepository, lectureId);
            TimestampsListFragment tlf = TimestampsListFragment.newInstance(queryAppRepository, lectureId);
            switch (item.getItemId()) {
                case R.id.navigation_questions:
                    transaction.replace(R.id.frame_layout, mlf, "Questions");
                    transaction.commit();
                    break;
                case R.id.navigation_timestamps:
                    transaction.replace(R.id.frame_layout, tlf, "Timestamps");
                    transaction.commit();
                    break;
            }
            return true;
        }

    };

    public void onFragmentInteraction(Uri uri) {
        // do nothing
    }

    @Override
    public void sendSlideNumber(int slideNumber) {
        // Get Fragment with slides to do update
        Log.d("sendingSlides", "See here");
        LectureSlidesFragment slidesFrag = (LectureSlidesFragment) getSupportFragmentManager().findFragmentById(R.id.lecture_slides_frame_layout);
        slidesFrag.setAndRedrawSlideNumber(slideNumber);
    }

    @Override
    public int getSlideNumber() {
        LectureSlidesFragment slidesFrag = (LectureSlidesFragment) getSupportFragmentManager().findFragmentById(R.id.lecture_slides_frame_layout);
        return slidesFrag.getLectureSlideNumber();
    }

    @Override
    public void refreshTime() {
        TimestampsListFragment times = (TimestampsListFragment) getSupportFragmentManager().findFragmentByTag("Timestamps");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (times != null && times.isVisible()) {
            //times.refreshTimestampFragment();
            Fragment frg = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
            transaction.detach(frg);
            transaction.attach(frg);
            transaction.commit();
        } else {

            int lectureId = getIntent().getIntExtra("LectureId", -1);
            TimestampsListFragment tlf = TimestampsListFragment.newInstance(queryAppRepository, lectureId);
            transaction.replace(R.id.frame_layout, tlf, "Timestamps");
            transaction.commit();
        }

        Log.d("REFRESH", "We trying");
    }

    @Override
    public void refreshQuestions() {
        QuestionsListFragment times = (QuestionsListFragment) getSupportFragmentManager().findFragmentByTag("Questions");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (times != null && times.isVisible()) {
            //times.refreshTimestampFragment();
            Fragment frg = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
            transaction.detach(frg);
            transaction.attach(frg);
            transaction.commit();
        } else {

            int lectureId = getIntent().getIntExtra("LectureId", -1);
            QuestionsListFragment tlf = QuestionsListFragment.newInstance(queryAppRepository, lectureId);
            transaction.replace(R.id.frame_layout, tlf, "Questions");
            transaction.commit();
        }

        Log.d("REFRESH", "We trying");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Added actionBar stuff
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        queryAppRepository = new QueryAppRepository(getApplication());

        int lectureId = getIntent().getIntExtra("LectureId", -1);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, TimestampsListFragment.newInstance(queryAppRepository, lectureId));
        transaction.replace(R.id.lecture_slides_frame_layout, new LectureSlidesFragment());
        transaction.replace(R.id.buttons_frame_layout, ButtonsFragment.newInstance(lectureId));
        transaction.commit();
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

}
