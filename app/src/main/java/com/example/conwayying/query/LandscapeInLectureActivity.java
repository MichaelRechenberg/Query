package com.example.conwayying.query;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.conwayying.query.data.ButtonsFragment;
import com.example.conwayying.query.data.LectureSlidesFragment;

public class LandscapeInLectureActivity extends AppCompatActivity
    implements LectureSlidesFragment.OnFragmentInteractionListener,
        ButtonsFragment.GetSlideNumberInterface {


    // Store the lecture id taken from the Intent that started this activity
    private int lectureId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscape_in_lecture);

        lectureId = getIntent().getIntExtra("LectureId", -1);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.landscape_in_lecture_slides_frame_layout, new LectureSlidesFragment());
        transaction.replace(R.id.landscape_in_lecture_button_frame_layout, ButtonsFragment.newInstance(lectureId));
        transaction.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();

        int slideNumber = getIntent().getIntExtra("SlideNumber", 1);
        LectureSlidesFragment lsf = (LectureSlidesFragment) getSupportFragmentManager().findFragmentById(R.id.landscape_in_lecture_slides_frame_layout);
        lsf.setAndRedrawSlideNumber(slideNumber);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Do nothing
    }

    @Override
    public int getSlideNumber() {
        LectureSlidesFragment slidesFrag = (LectureSlidesFragment) getSupportFragmentManager().findFragmentById(R.id.landscape_in_lecture_slides_frame_layout);
        return slidesFrag.getLectureSlideNumber();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.d("Foo", "onConfigurationChanged called");

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.d("Foo", "Switching to portrait in-lecture screen");

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("SlideNumber", getSlideNumber());
            intent.putExtra("LectureId", lectureId);

            startActivity(intent);

            // Remove this activity from the activity stack
            this.finish();
        }
    }
}
