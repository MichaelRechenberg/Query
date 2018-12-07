package com.example.conwayying.query;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.conwayying.query.data.ButtonsFragment;
import com.example.conwayying.query.data.LectureSlidesFragment;
import com.example.conwayying.query.data.QueryAppRepository;
import com.example.conwayying.query.data.QuestionsListFragment;
import com.example.conwayying.query.data.TimestampsFragment;
import com.example.conwayying.query.data.TimestampsListFragment;

import java.sql.Time;
import java.util.List;


public class MainActivity extends AppCompatActivity implements QuestionsListFragment.OnFragmentInteractionListener, TimestampsListFragment.OnFragmentInteractionListener,
        TimestampsFragment.OnFragmentInteractionListener, LectureSlidesFragment.OnFragmentInteractionListener,
        ButtonsFragment.GetSlideNumberInterface, TimestampsListFragment.TimestampClicked, QuestionsListFragment.QuestionClicked,
        ButtonsFragment.RefreshTimestampsInterface, ButtonsFragment.RefreshQuestionsInterface,
        AdapterView.OnItemSelectedListener
    {

    private TextView mTextMessage;
    private QueryAppRepository queryAppRepository;
    private ActionBar actionBar;
    private BottomNavigationView bottomNavigationView;

    private Spinner sortListSpinner;

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
                    transaction.replace(R.id.frame_layout, mlf, "Questions");
                    transaction.commit();

                    // Set the sort criteria of spinner to default "Date Created"
                    // QuestionListFragment default sorts by Date Created as well
                    Log.d("Foo", "REEE");
                    sortListSpinner.setSelection(0);
                    break;
                case R.id.navigation_timestamps:
                    transaction.replace(R.id.frame_layout, tlf, "Timestamps");
                    transaction.commit();

                    // Set the sort criteria of spinner to default "Date Created"
                    // TimestampsListFragment default sorts by Date Created as well
                    sortListSpinner.setSelection(0);
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

    // Refresh the timestamp list
    // If the timestamp list fragment is not currently visible, replace the current list fragment
    //  with the timestamp list fragment and set the bottom navigation bar to "Timestamps"
    @Override
    public void refreshTime() {
        Log.d("REFRESH", "We trying time");

        TimestampsListFragment times = (TimestampsListFragment) getSupportFragmentManager().findFragmentByTag("Timestamps");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (times != null && times.isVisible()) {
            Log.d("REFRESH", "refreshing timestamps when list is visible");
            times.refreshTimestampsList();
        } else {

            Log.d("REFRESH", "Refreshing timestamps when not visible");
            int lectureId = getIntent().getIntExtra("LectureId", -1);
            TimestampsListFragment tlf = TimestampsListFragment.newInstance(queryAppRepository, lectureId);
            transaction.replace(R.id.frame_layout, tlf, "Timestamps");
            transaction.commit();
            bottomNavigationView.setSelectedItemId(R.id.navigation_timestamps);
            // When switching the list fragment, default the sort spinner to "Date Created"
            sortListSpinner.setSelection(0);
        }

    }

    // Refresh the questions list
    // If the questions list fragment is not currently visible, replace the current list fragment
    //  with the questions list fragment and set the bottom navigation bar to "Questions"
    @Override
    public void refreshQuestions() {
        QuestionsListFragment times = (QuestionsListFragment) getSupportFragmentManager().findFragmentByTag("Questions");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (times != null && times.isVisible()) {
            times.refreshQuestionList();
        } else {

            int lectureId = getIntent().getIntExtra("LectureId", -1);
            QuestionsListFragment tlf = QuestionsListFragment.newInstance(queryAppRepository, lectureId);
            transaction.replace(R.id.frame_layout, tlf, "Questions");
            transaction.commit();
            bottomNavigationView.setSelectedItemId(R.id.navigation_questions);
            // When switching the list fragment, default the sort spinner to "Date Created"
            sortListSpinner.setSelection(0);
        }

        Log.d("REFRESH", "We trying");
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


        queryAppRepository = new QueryAppRepository(getApplication());

        lectureId = getIntent().getIntExtra("LectureId", -1);
        // Spinner requires fragments to be created, so they must be initialized before the spinner
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, TimestampsListFragment.newInstance(queryAppRepository, lectureId), "Timestamps");
        transaction.replace(R.id.lecture_slides_frame_layout, new LectureSlidesFragment(), "LectureSlides");
        transaction.replace(R.id.buttons_frame_layout, ButtonsFragment.newInstance(lectureId), "ConfusionButtons");
        transaction.commit();



        // Since bottomNavigationView uses sortListSpinner to change sort criteria on navigation change,
        //  sortListSpinner must be initialized before bottomNavigationView
        sortListSpinner = (Spinner) findViewById(R.id.sort_list_spinner);
        sortListSpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.sort_list_menu_choices, R.layout.support_simple_spinner_dropdown_item));
        sortListSpinner.setOnItemSelectedListener(this);

        // Added actionBar stuff (back arrow on top of screen)
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mTextMessage = (TextView) findViewById(R.id.message);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // We start in the timestamps view, so set the menu navigation to that
        bottomNavigationView.setSelectedItemId(R.id.navigation_timestamps);





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

    @Override
    public boolean onSupportNavigateUp(){
        //code it to launch an intent to the activity you want
        //Intent intent = new Intent(getApplicationContext(), ClassListActivity.class);
        //getApplicationContext().startActivity(intent);
        onBackPressed();
        finish();
        return true;
    }

    // spinner methods

    // After the user clicks a drop-down option from the spinner, sort the list of entities
    //  based on the sort criteria, selecting the questions list fragment or timestamp list
    //  fragment based on fragment tag
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long rowId) {


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag("Questions");

        Log.d("Foo", "bar");
        if (fragment != null){

           Log.d("Foo", "found qlf");
           QuestionsListFragment qlf = (QuestionsListFragment) fragment;

           // rowId mapping taken from spinner's menu array
           int sortBy = QuestionsListFragment.SORT_BY_TIME_CREATED;
           if (rowId == 1){
               sortBy = QuestionsListFragment.SORT_BY_IS_RESOLVED;
           }

           qlf.refreshQuestionList(sortBy);
        }
        else {
            fragment = fm.findFragmentByTag("Timestamps");

            if (fragment != null){
                Log.d("Foo", "found tlf");
                TimestampsListFragment tlf = (TimestampsListFragment) fragment;

                // rowId mapping taken from spinner's menu array
                int sortBy = TimestampsListFragment.SORT_BY_START_DATE;
                if (rowId == 1){
                    sortBy = TimestampsListFragment.SORT_BY_IS_RESOLVED;
                }
                tlf.refreshTimestampsList(sortBy);
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.d("Foo", "asdfsdf");
        // do nothing
    }
}
