package com.example.conwayying.query.data;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.conwayying.query.R;
import com.example.conwayying.query.data.entity.ConfusionMark;
import com.example.conwayying.query.data.entity.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
// References
// https://gist.github.com/ar-android/002d77cbd935f970ef5c
public class TimestampsListFragment extends android.support.v4.app.ListFragment {

    private String dataArray[];
    private ArrayList<HashMap<String, String>> data;
    private QueryAppRepository qar;
    private OnFragmentInteractionListener mListener;
    private SimpleAdapter adapter;

    private TimestampClicked mCallback;

    public TimestampsListFragment (){
        // required empty constructor
    }

    public TimestampsListFragment(QueryAppRepository qar) {
        data = new ArrayList<HashMap<String,String>>();
        this.qar = qar;
    }

    /**
     * Factory method to make a new TimestampsListFragment, specifying lecture id
     * @param qar
     * @param lectureId
     * @return
     */
    public static TimestampsListFragment newInstance(QueryAppRepository qar, int lectureId){
        TimestampsListFragment tlf = new TimestampsListFragment(qar);
        Bundle args = new Bundle();
        args.putInt("LectureId", lectureId);
        tlf.setArguments(args);
        return tlf;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (TimestampClicked) context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        else {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater
                .inflate(R.layout.simple_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int lectureId = this.getArguments().getInt("LectureId", -1);
        List<ConfusionMark> confusionMarksList = qar.getAllConfusionMarksForLecture(lectureId);

        // We populate the data array list
        // Each hash map in the array list represents a single row's information
        HashMap<String, String> rowInformation;
        // Clear data
        data = new ArrayList<>();

        for (ConfusionMark confusionMark : confusionMarksList) {
            rowInformation = new HashMap<String, String>();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String concatenatedStartEnd;
            if (confusionMark.getEndDate() == null) {
                concatenatedStartEnd = sdf.format(confusionMark.getStartDate());
                Log.d("lol", "falling");
            }
            else {
                concatenatedStartEnd = sdf.format(confusionMark.getStartDate()) + " - " + sdf.format(confusionMark.getEndDate());
            }
            rowInformation.put("Confusion Mark", concatenatedStartEnd);
            rowInformation.put("Slide Number", Integer.toString(confusionMark.getSlideNumber()));
            rowInformation.put("Is Resolved Status", confusionMark.getIsResolved() ? "Resolved" : "Unresolved");
            rowInformation.put("isResolved", String.valueOf(confusionMark.getIsResolved()));
            rowInformation.put("id", Integer.toString(confusionMark.getConfusionId()));

            data.add(rowInformation);
        }

        String[] from = {"Confusion Mark", "Is Resolved Status"};

        int[] to = {R.id.confusionMarkText, R.id.thirdLine};

        adapter = new SimpleAdapter(getActivity(), data, R.layout.timestamps_fragment_list_item, from, to);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {

        Log.d("slidenum", "" + data.get(position).get("Slide Number"));
        clickedOn(Integer.parseInt(data.get(position).get("Slide Number")));

        // Updating the database
        Log.d("isResolved: ", "" + data.get(position).get("isResolved"));
        qar.updateConfusionIsResolved(Integer.parseInt(data.get(position).get("id")), !Boolean.parseBoolean(data.get(position).get("isResolved")));

        // Refreshes the fragment stuff
        Log.d("Refreshing Timestamps", "Let's go");
        String[] from = {"Confusion Mark", "Is Resolved Status"};
        int[] to = {R.id.confusionMarkText, R.id.thirdLine};

        int lectureId = this.getArguments().getInt("LectureId", -1);
        List<ConfusionMark> confusionMarksList = qar.getAllConfusionMarksForLecture(lectureId);

        HashMap<String, String> rowInformation;

        // Need this to keep same reference
        data.clear();

        for (ConfusionMark confusionMark : confusionMarksList) {
            rowInformation = new HashMap<String, String>();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String concatenatedStartEnd;
            if (confusionMark.getEndDate() == null) {
                concatenatedStartEnd = sdf.format(confusionMark.getStartDate());
                Log.d("lol", "falling");
            }
            else {
                concatenatedStartEnd = sdf.format(confusionMark.getStartDate()) + " - " + sdf.format(confusionMark.getEndDate());
            }
            rowInformation.put("Confusion Mark", concatenatedStartEnd);
            rowInformation.put("Slide Number", Integer.toString(confusionMark.getSlideNumber()));
            rowInformation.put("Is Resolved Status", confusionMark.getIsResolved() ? "Resolved" : "Unresolved");
            rowInformation.put("isResolved", String.valueOf(confusionMark.getIsResolved()));
            rowInformation.put("id", Integer.toString(confusionMark.getConfusionId()));

            data.add(rowInformation);
        }
        adapter.notifyDataSetChanged();
    }

    public interface TimestampClicked {
        void sendSlideNumber(int slideNumber);
    }

    public void clickedOn(int slide) {
        mCallback.sendSlideNumber(slide);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
