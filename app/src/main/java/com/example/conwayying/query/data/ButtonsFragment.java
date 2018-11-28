package com.example.conwayying.query.data;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.example.conwayying.query.R;
import com.example.conwayying.query.data.entity.ConfusionMark;

import java.util.Calendar;
import java.util.Date;

public class ButtonsFragment extends Fragment {


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private QueryAppRepository queryAppRepository;
    private GetLectureIdInterface mListener;

    private ImageButton mWTFButton;
    // TODO: do the other ImageButtons
    private ToggleButton mConfToggleButton;


    // Fragment state
    private Date lastStartRecordDate;

    public ButtonsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param lectureId Parameter 2.
     * @return A new instance of fragment ButtonsFragment
     */
    // TODO: Rename and change types and number of parameters
    public static ButtonsFragment newInstance(int lectureId) {
        ButtonsFragment fragment = new ButtonsFragment();
        Bundle args = new Bundle();
        args.putInt("LectureId", lectureId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastStartRecordDate = null;
        queryAppRepository = new QueryAppRepository(getActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lecture_buttons, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: better ID names
        mWTFButton = (ImageButton) view.findViewById(R.id.imageButton3);
        mConfToggleButton = (ToggleButton) view.findViewById(R.id.imageButton);
        Bundle bundle = this.getArguments();
        int lectureId = bundle.getInt("LectureId", -1);
        initWTFButtonEventHandlers(mWTFButton, queryAppRepository, lectureId);
        initRecordIntervalButtonEventHandlers(mConfToggleButton, queryAppRepository, lectureId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GetLectureIdInterface) {
            mListener = (GetLectureIdInterface) context;
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
    public interface GetLectureIdInterface {
        int getLectureId();
    }


    /**
     * Initialize the WTF s.t. when pressed, it inserts a ConfusionMark into the DB
     * @param button The WTF Button
     * @param queryAppRepository Repo to use
     * @param lectureId Lecture id to associate the ConfusionMark with
     */
    private void initWTFButtonEventHandlers(final ImageButton button,
                                            final QueryAppRepository queryAppRepository,
                                            final int lectureId){
        final ButtonsFragment bf = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentDateTime = Calendar.getInstance().getTime();
                int slideNumber = bf.mListener.getLectureId();
                ConfusionMark confusionMark = new ConfusionMark(currentDateTime, lectureId, slideNumber);
                ConfusionMarkAsyncTaskParams param = new ConfusionMarkAsyncTaskParams();
                param.repo = queryAppRepository;
                param.confusionMark = confusionMark;
                Log.d("DebugDB", "Firing and forgetting insertion of confusion mark off of WTF button");
                new InsertConfusionMarkAsyncTask().execute(param);
            }
        });
    }

    /**
     * Set up event handlers for recording interval button s.t. when it is toggled on,
     *  then toggled off, we record the interval of time that elapsed in a ConfusionMark
     *
     *
     * Note that this function modifies the instance field "lastStartedRecordDate"
     *
     * @param button The toggle button
     * @param queryAppRepository The repo
     * @param lectureId The Lecture id to associate the ConfusionMark with
     */
    private void initRecordIntervalButtonEventHandlers(ToggleButton button,
                                                       final QueryAppRepository queryAppRepository,
                                                       final int lectureId){
        final ButtonsFragment bf = this;
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    // Toggle is enabled
                    lastStartRecordDate = Calendar.getInstance().getTime();
                    Log.d("DebugDB", "lastStartRecordDate set to " + lastStartRecordDate.toString());
                }
                else{
                    // Toggle was disabled
                    // Record the confusion mark and reset lastStartRecordDate
                    Date currDate = Calendar.getInstance().getTime();
                    int slideNumber = bf.mListener.getLectureId();
                    ConfusionMark confusionMark = new ConfusionMark(lastStartRecordDate, lectureId, slideNumber);
                    ConfusionMarkAsyncTaskParams param = new ConfusionMarkAsyncTaskParams();
                    confusionMark.setEndDate(currDate);
                    lastStartRecordDate = null;
                    param.repo = queryAppRepository;
                    param.confusionMark = confusionMark;
                    Log.d("DebugDB", "Firing and forgetting interval ConfusionMark");
                    new InsertConfusionMarkAsyncTask().execute(param);
                }
            }
        });
    }



    private class ConfusionMarkAsyncTaskParams {
        QueryAppRepository repo;
        ConfusionMark confusionMark;
    }

    private static class InsertConfusionMarkAsyncTask extends AsyncTask<ConfusionMarkAsyncTaskParams, Void, Void> {
        @Override
        protected Void doInBackground(ConfusionMarkAsyncTaskParams...params) {
            for(ConfusionMarkAsyncTaskParams param : params){
                QueryAppRepository repo = param.repo;
                ConfusionMark confusionMark = param.confusionMark;
                Date endDate = confusionMark.getEndDate();
                Log.d("DebugDB", "Inserting ConfusionMark with start date " + confusionMark.getStartDate().toString() +
                        " and end date " + ((endDate != null) ? endDate.toString() : "NULL") +
                        " and slide number " + confusionMark.getSlideNumber() +
                        " and lecture id " + confusionMark.getLectureId());
                Long confusionMarkId = repo.insert(confusionMark);
                Log.d("DebugDB", "Newly inserted ConfusionMark has id " + confusionMarkId.toString());


                Log.d("DebugDB", Integer.toString(repo.getAllConfusionMarksForLecture(param.confusionMark.getLectureId()).size()));
            }
            return null;
        }

    }

}
