package com.example.conwayying.query.data;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.app.ListFragment;
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
    private OnFragmentInteractionListener mListener;
    private SimpleAdapter adapter;

    public QuestionsListFragment(QueryAppRepository qar) {
        data = new ArrayList<HashMap<String,String>>();
        this.qar = qar;
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
        // TODO: Actually take in real classId
        List<Note> notesList = qar.getAllNotesForClass(1);

        // We populate the data array list
        // Each hash map in the array list represents a single row's information
        HashMap<String, String> rowInformation;

        for (Note note : notesList) {
            rowInformation = new HashMap<String, String>();
            rowInformation.put("Note Text", note.getNoteText());
            rowInformation.put("Private Status", note.getIsPrivate() ? "Private" : "Public");
            rowInformation.put("Is Resolved Status", note.getIsResolved() ? "Resolved" : "Unresolved");
            data.add(rowInformation);
        }

        String[] from = {"Note Text", "Private Status", "Is Resolved Status"};

        int[] to = {R.id.firstLine, R.id.secondLine, R.id.thirdLine};

        adapter = new SimpleAdapter(getActivity(), data, R.layout.question_fragment_list_item, from, to);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {

        Toast.makeText(getActivity(),
                getListView().getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
