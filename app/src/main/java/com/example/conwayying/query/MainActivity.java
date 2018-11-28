package com.example.conwayying.query;

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
import com.example.conwayying.query.data.QuestionsListFragment;
import com.example.conwayying.query.data.QueryAppRepository;
import com.example.conwayying.query.data.TimestampsFragment;
import com.example.conwayying.query.data.TimestampsListFragment;


public class MainActivity extends AppCompatActivity implements QuestionsListFragment.OnFragmentInteractionListener, TimestampsListFragment.OnFragmentInteractionListener,
        TimestampsFragment.OnFragmentInteractionListener, LectureSlidesFragment.OnFragmentInteractionListener,
        ButtonsFragment.OnFragmentInteractionListener, TimestampsListFragment.TimestampClicked {

    private TextView mTextMessage;
    private QueryAppRepository queryAppRepository;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            QuestionsListFragment mlf = new QuestionsListFragment(queryAppRepository);
            TimestampsListFragment tlf = new TimestampsListFragment(queryAppRepository);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        queryAppRepository = new QueryAppRepository(getApplication());

        int lectureId = getIntent().getIntExtra("LectureId", -1);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new QuestionsListFragment(queryAppRepository));
        transaction.replace(R.id.lecture_slides_frame_layout, new LectureSlidesFragment());
        transaction.replace(R.id.buttons_frame_layout, ButtonsFragment.newInstance(lectureId));
        transaction.commit();
    }

}
