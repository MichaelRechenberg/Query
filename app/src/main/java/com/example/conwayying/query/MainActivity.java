package com.example.conwayying.query;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.conwayying.query.data.LectureSlidesFragment;
import com.example.conwayying.query.data.MyListFragment;
import com.example.conwayying.query.data.QueryAppRepository;
import com.example.conwayying.query.data.QuestionsFragment;
import com.example.conwayying.query.data.TimestampsFragment;


public class MainActivity extends AppCompatActivity implements MyListFragment.OnFragmentInteractionListener,
        TimestampsFragment.OnFragmentInteractionListener, LectureSlidesFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;
    private QueryAppRepository queryAppRepository;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_questions:
                    MyListFragment mlf = new MyListFragment(queryAppRepository);
                    transaction.replace(R.id.frame_layout, mlf);
                    transaction.commit();
                    break;
                case R.id.navigation_timestamps:
                    selectedFragment = TimestampsFragment.newInstance("lol", "zors");
                    transaction.replace(R.id.frame_layout, selectedFragment);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        queryAppRepository = new QueryAppRepository(getApplication());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new MyListFragment(queryAppRepository));
        transaction.replace(R.id.lecture_slides_frame_layout, new LectureSlidesFragment());
        transaction.commit();
    }

}
