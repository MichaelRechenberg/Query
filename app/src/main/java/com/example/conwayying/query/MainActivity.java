package com.example.conwayying.query;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.conwayying.query.data.ButtonsFragment;
import com.example.conwayying.query.data.LectureSlidesFragment;
import com.example.conwayying.query.data.QueryAppRepository;
import com.example.conwayying.query.data.QuestionsListFragment;
import com.example.conwayying.query.data.TimestampsFragment;
import com.example.conwayying.query.data.TimestampsListFragment;


public class MainActivity extends AppCompatActivity implements QuestionsListFragment.OnFragmentInteractionListener, TimestampsListFragment.OnFragmentInteractionListener,
        TimestampsFragment.OnFragmentInteractionListener, LectureSlidesFragment.OnFragmentInteractionListener,
        ButtonsFragment.GetSlideNumberInterface, TimestampsListFragment.TimestampClicked, QuestionsListFragment.QuestionClicked {

    private TextView mTextMessage;
    private QueryAppRepository queryAppRepository;

    // MainActivity state
    // The lecture id for this lecture
    private int lectureId;

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
                    transaction.replace(R.id.frame_layout, mlf);
                    transaction.commit();
                    break;
                case R.id.navigation_timestamps:
                    transaction.replace(R.id.frame_layout, tlf);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If this activity is created while in landscape mode, switch activities to the
        //  landscape in-lecture activity at starting slide 1 instead, and .finish() this activity
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Intent intent = new Intent(getApplicationContext(), LandscapeInLectureActivity.class);
            intent.putExtra("LectureId", getIntent().getIntExtra("LectureId", -1));
            intent.putExtra("SlideNumber", 0);
            startActivity(intent);

            // Close this activity
            this.finish();
        }

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        queryAppRepository = new QueryAppRepository(getApplication());

        lectureId = getIntent().getIntExtra("LectureId", -1);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, TimestampsListFragment.newInstance(queryAppRepository, lectureId));
        transaction.replace(R.id.lecture_slides_frame_layout, new LectureSlidesFragment());
        transaction.replace(R.id.buttons_frame_layout, ButtonsFragment.newInstance(lectureId));
        transaction.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();

        int slideNumber = getIntent().getIntExtra("SlideNumber", 0);
        sendSlideNumber(slideNumber);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.d("Foo", "onConfigCalled");

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("Foo", "Switching to landscape in-lecture activity");
            // Start the landscape version of the view, sending it the lecture id and slide number in the intent
            Intent intent = new Intent(getApplicationContext(), LandscapeInLectureActivity.class);
            intent.putExtra("LectureId", lectureId);
            intent.putExtra("SlideNumber", getSlideNumber());
            startActivity(intent);

            // Close this activity
            this.finish();
        }
    }

}
