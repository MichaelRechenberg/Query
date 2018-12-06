package com.example.conwayying.query.data;

import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.conwayying.query.R;
import com.example.conwayying.query.data.entity.ConfusionMark;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
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


    // Sorting criteria for ConfusionMarks/Timestamps
    // Sort by unresolved, then resolved ConfusionMarks...breaking ties with dateCreated
    public final static int SORT_BY_IS_RESOLVED = 1;
    // Sort by startDate
    public final static int SORT_BY_START_DATE = 2;

    private static final String[] from = {"Confusion Mark", "Is Resolved Status"};
    private static final int[] to = {R.id.confusionMarkText, R.id.thirdLine};

    private ArrayList<HashMap<String, String>> data;
    private QueryAppRepository qar;
    private OnFragmentInteractionListener mListener;
    private SimpleAdapter adapter;

    private TimestampClicked mCallback;

    // Current sorting criteria of Timestamps
    private int mSortCriteria;

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

        adapter = new SimpleAdapter(getActivity(), data, R.layout.timestamps_fragment_list_item, from, to);
        setListAdapter(adapter);

        // Default to time created
        mSortCriteria = TimestampsListFragment.SORT_BY_START_DATE;
        // Pull timestamps from DB and sort
        refreshTimestampsList(mSortCriteria);
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

        // Comment out if we want to refresh and re-sort the list onListItemClick
        refreshTimestampsList(this.mSortCriteria);
    }

    /**
     * Refresh the list of timestamps by pulling from the DB, specifying a sorting criteria
     * @param sortBy SORT_BY_IS_RESOLVED or SORT_BY_START_DATE
     */
    public void refreshTimestampsList(int sortBy){

        this.mSortCriteria = sortBy;

        int lectureId = this.getArguments().getInt("LectureId", -1);
        ArrayList<ConfusionMark> confusionMarksList = (ArrayList<ConfusionMark>) qar.getAllConfusionMarksForLecture(lectureId);
        sortConfusionMarks(mSortCriteria, confusionMarksList);

        // We populate the data array list

        // Clear data
        data.clear();

        for (ConfusionMark confusionMark : confusionMarksList) {
            // Each hash map in the array list represents a single row's information
            HashMap<String, String> rowInformation = new HashMap<String, String>();
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

    /**
     * Refresh the timestamps list, using the current sorting criteria
     */
    public void refreshTimestampsList(){
       refreshTimestampsList(this.mSortCriteria);
    }

    /**
     * Sorts the input array of notes based on sortBy attribute
     *
     * SORT_BY_IS_RESOLVED: All unresolved notes show up first, breaking ties by dateCreated
     * SORT_BY_START_DATE: Sort by dateCreated
     * @param sortBy sorting condition
     * @param confusionMarks Array of notes to sort...modified by this function
     */
    private void sortConfusionMarks(int sortBy, ArrayList<ConfusionMark> confusionMarks){
        switch (sortBy){
            case SORT_BY_IS_RESOLVED:
                confusionMarks.sort(new Comparator<ConfusionMark>() {
                    @Override
                    public int compare(ConfusionMark cm1, ConfusionMark cm2) {
                        boolean cm1Resolved = cm1.getIsResolved();
                        boolean cm2Resolved = cm2.getIsResolved();

                        // If resolved status is the same, use start date as tie breaking
                        if (cm1Resolved == cm2Resolved){
                            return cm1.getStartDate().compareTo(cm2.getStartDate());
                        }
                        // Otherwise, have have the unresolved confusion mark show up first in the ordering
                        else if (cm1Resolved && !cm2Resolved){
                            return 1;
                        }
                        else{
                            return -1;
                        }
                    }
                });

                break;
            case SORT_BY_START_DATE:
                confusionMarks.sort(new Comparator<ConfusionMark>() {
                    @Override
                    public int compare(ConfusionMark cm1, ConfusionMark cm2) {
                        return cm1.getStartDate().compareTo(cm2.getStartDate());
                    }
                });
                break;
            default:
                Log.e("Foo", "Invalid sorting criteria given to sorting questions list");
                break;
        }
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
