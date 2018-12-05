package com.example.conwayying.query.data;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.conwayying.query.LectureSlideContainer;
import com.example.conwayying.query.R;

import java.io.IOException;
import java.io.InputStream;

public class SingleLectureSlideFragment extends Fragment {

    public final static String ARG_SLIDE_NUMBER = "SLIDE_NUMBER";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lecture_slide_of_view_pager, container, false);

        ImageView slideImageView = (ImageView) rootView.findViewById(R.id.lecture_slide_image_view);

        int slideNumber = getArguments().getInt(ARG_SLIDE_NUMBER);
        InputStream inputStream = null;

        Log.d("Foo", "Attempting to make lecture slide fragment single for slide number " + slideNumber);

        try {
            String slideFilePath = LectureSlideContainer.MOCK_LECTURE_ASSET_DIR + "slide" + slideNumber + ".jpg";
            inputStream = getContext().getAssets().open(slideFilePath);
            Drawable slideDrawable = Drawable.createFromStream(inputStream, null);
            slideImageView.setImageDrawable(slideDrawable);
            inputStream.close();
        } catch (IOException ex){
            Log.e("SingleLectureSlideFragment","Could not find slide file " + ex.toString());
        }

        return rootView;
    }
}
