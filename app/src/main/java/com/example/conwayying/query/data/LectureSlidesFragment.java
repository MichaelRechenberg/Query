package com.example.conwayying.query.data;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.conwayying.query.LectureSlideContainer;
import com.example.conwayying.query.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LectureSlidesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LectureSlidesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LectureSlidesFragment extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // ImageView to render the slides
    private ImageView mImageView;
    // SeekBar to adjust which slide is displayed
    private SeekBar mSeekBar;

    // LectureSlideContainer to link up ImageView and SeekBar
    private LectureSlideContainer lectureSlideContainer;

    private OnFragmentInteractionListener mListener;

    public LectureSlidesFragment() {
        // Required empty public constructor
    }

    public void setAndRedrawSlideNumber(int slideNumber) {
        this.lectureSlideContainer.setSlideNumber(slideNumber);
        this.lectureSlideContainer.redrawSlide();
    }

    public int getLectureSlideNumber(){
        return this.lectureSlideContainer.getCurrentSlideNumber();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LectureSlidesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LectureSlidesFragment newInstance(String param1, String param2) {
        LectureSlidesFragment fragment = new LectureSlidesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    // Setup views from the fragment's XML layout here
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mImageView = (ImageView) view.findViewById(R.id.lecture_slides_image_view);
        this.mSeekBar = (SeekBar) view.findViewById(R.id.lecture_slides_seek_bar);

        this.lectureSlideContainer = new LectureSlideContainer(getActivity(), this.mImageView, this.mSeekBar);
        this.lectureSlideContainer.redrawSlide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lecture_slides, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
