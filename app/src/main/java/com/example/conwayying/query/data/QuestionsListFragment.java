package com.example.conwayying.query.data;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.conwayying.query.R;
import com.example.conwayying.query.data.entity.Note;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

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
public class QuestionsListFragment extends android.support.v4.app.ListFragment {

    public final static int SORT_BY_IS_RESOLVED = 1;
    public final static int SORT_BY_TIME_CREATED = 2;

    private final static String[] from = {"Note Text", "Private Status", "Is Resolved Status"};
    private final static int[] to = {R.id.firstLine, R.id.secondLine, R.id.thirdLine};

    private String dataArray[];
    private ArrayList<HashMap<String, String>> data;
    private QueryAppRepository qar;
    private QuestionClicked mCallback;
    private SimpleAdapter adapter;

    // What SORT_BY* criteria we are using for the list of questions
    private int mSortCriteria;

    public QuestionsListFragment() {
        // required empty constructor
    }

    public QuestionsListFragment(QueryAppRepository qar) {
        data = new ArrayList<HashMap<String,String>>();
        this.qar = qar;
    }

    /**
     * Factory method to make a new TimestampsListFragment, specifying lecture id
     * @param qar
     * @param lectureId
     * @return
     */
    public static QuestionsListFragment newInstance(QueryAppRepository qar, int lectureId) {
        QuestionsListFragment qlf = new QuestionsListFragment(qar);
        Bundle args = new Bundle();
        args.putInt("LectureId", lectureId);
        qlf.setArguments(args);

        return qlf;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (QuestionClicked) context;
        if (context instanceof QuestionClicked) {
            mCallback = (QuestionClicked) context;
        } else {
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

        // default to sorting questions by time created
        mSortCriteria = SORT_BY_TIME_CREATED;

        //adapter = new SimpleAdapter(getActivity(), data, R.layout.question_fragment_list_item, from, to);
        adapter = new CustomSimpleAdapter(getActivity(), data, R.layout.question_fragment_list_item, from, to);
        setListAdapter(adapter);

        refreshQuestionList(mSortCriteria);
    }

    /**
     * Refresh the list of questions, sorting the list based on the sortBy parameter
     * @param sortBy QuestionListFragment.SORT_BY_DATE_CREATED or QuestionListFragment.SORT_BY_RESOLVED_STATE
     */
    public void refreshQuestionList(int sortBy) {

        mSortCriteria = sortBy;

        // Refreshes the fragment stuff and sorts
        Log.d("Refreshing Notes", "Let's go, sorting using criteria " + mSortCriteria);

        int lectureId = this.getArguments().getInt("LectureId", -1);

        ArrayList<Note> notesList = (ArrayList<Note>) qar.getAllNotesForLecture(lectureId);
        sortNotes(sortBy, notesList);

        HashMap<String, String> rowInformation;
        data.clear();

        for (Note note : notesList) {
            rowInformation = new HashMap<String, String>();
            rowInformation.put("Note Text", note.getNoteText());
            rowInformation.put("Private Status", note.getIsPrivate() ? "Private" : "Public");
            rowInformation.put("Is Resolved Status", note.getIsResolved() ? "Resolved" : "Unresolved");
            rowInformation.put("Slide Number", Integer.toString(note.getSlideNumber()));
            rowInformation.put("id", Integer.toString(note.getNoteId()));
            data.add(rowInformation);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Refresh the questions list, using the current sorting criteria of the list
     */
    public void refreshQuestionList(){
       refreshQuestionList(this.mSortCriteria);
    }

    /**
     * Sorts the input array of notes based on sortBy attribute
     *
     * SORT_BY_IS_RESOLVED: All unresolved notes show up first, breaking ties by dateCreated
     * SORT_BY_START_DATE: Sort by dateCreated
     * @param sortBy sorting condition
     * @param notes Array of notes to sort...modified by this function
     */
    private void sortNotes(int sortBy, ArrayList<Note> notes){
        switch (sortBy){
            case SORT_BY_IS_RESOLVED:
                notes.sort(new Comparator<Note>() {
                    @Override
                    public int compare(Note note1, Note note2) {
                        boolean note1Resolved = note1.getIsResolved();
                        boolean note2Resolved = note2.getIsResolved();

                        // If resolved status is the same, use date created to tie break
                        if (note1Resolved == note2Resolved){
                            return note1.getDateCreated().compareTo(note2.getDateCreated());
                        }
                        // If note 1 is resolved and note2 is not, have note2 show up first in the ordering
                        else if (note1Resolved && !note2Resolved){
                            return 1;
                        }
                        else{
                            return -1;
                        }
                    }
                });

                break;
            case SORT_BY_TIME_CREATED:
                notes.sort(new Comparator<Note>() {
                    @Override
                    public int compare(Note note1, Note note2) {
                        return note1.getDateCreated().compareTo(note2.getDateCreated());
                    }
                });
                break;
            default:
                Log.e("Foo", "Invalid sorting criteria given to sorting questions list");
                break;
        }
    }

    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {
        clickedOn(Integer.parseInt(data.get(position).get("Slide Number")));

        // Updating the database
        Log.d("isResolved: ", "" + data.get(position).get("Is Resolved Status"));
        if (data.get(position).get("Is Resolved Status").equals("Resolved")) {
            qar.updateNoteIsResolved(Integer.parseInt(data.get(position).get("id")), false);
        } else {
            qar.updateNoteIsResolved(Integer.parseInt(data.get(position).get("id")), true);
        }

        // Comment out if we want to refresh and re-sort the question list
        refreshQuestionList();
    }

    public void clickedOn(int slide) {
        mCallback.sendSlideNumber(slide);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public interface QuestionClicked {
        void sendSlideNumber(int slideNumber);
    }
}
