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
import android.widget.Toast;

import com.example.conwayying.query.R;
import com.example.conwayying.query.data.entity.Note;

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
public class QuestionsListFragment extends android.support.v4.app.ListFragment {

    private String dataArray[];
    private ArrayList<HashMap<String, String>> data;
    private QueryAppRepository qar;
    private QuestionClicked mCallback;
    private SimpleAdapter adapter;

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
        int lectureId = this.getArguments().getInt("LectureId", -1);
        List<Note> notesList = qar.getAllNotesForLecture(lectureId);

        // We populate the data array list
        // Each hash map in the array list represents a single row's information
        HashMap<String, String> rowInformation;
        // Clear data
        data = new ArrayList<>();

        for (Note note : notesList) {
            rowInformation = new HashMap<String, String>();
            rowInformation.put("Note Text", note.getNoteText());
            rowInformation.put("Private Status", note.getIsPrivate() ? "Private" : "Public");
            rowInformation.put("Is Resolved Status", note.getIsResolved() ? "Resolved" : "Unresolved");
            rowInformation.put("Slide Number", Integer.toString(note.getSlideNumber()));
            rowInformation.put("id", Integer.toString(note.getNoteId()));
            data.add(rowInformation);
        }

        String[] from = {"Note Text", "Private Status", "Is Resolved Status"};

        int[] to = {R.id.firstLine, R.id.secondLine, R.id.thirdLine};

        //adapter = new SimpleAdapter(getActivity(), data, R.layout.question_fragment_list_item, from, to);
        adapter = new CustomSimpleAdapter(getActivity(), data, R.layout.question_fragment_list_item, from, to);
        setListAdapter(adapter);
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

        // Refreshes the fragment stuff
        Log.d("Refreshing Notes", "Let's go");
        String[] from = {"Note Text", "Private Status", "Is Resolved Status"};

        int[] to = {R.id.firstLine, R.id.secondLine, R.id.thirdLine};
        int lectureId = this.getArguments().getInt("LectureId", -1);
        List<Note> notesList = qar.getAllNotesForLecture(lectureId);
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
