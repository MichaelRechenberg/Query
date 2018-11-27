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
import android.widget.Toast;

import com.example.conwayying.query.R;
import com.example.conwayying.query.data.entity.Note;

import java.util.ArrayList;
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
public class MyListFragment extends android.support.v4.app.ListFragment {

    private String dataArray[];
    private QueryAppRepository qar;
    private OnFragmentInteractionListener mListener;

    public MyListFragment(QueryAppRepository qar) {
        dataArray = new String[] { "One", "Two", "Three", };
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

        ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, dataArray);
        setListAdapter(listAdapter);
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
        List<Note> notesList = qar.getAllNotesForClass(1);
        List<String> notes = new ArrayList();
        for (Note note : notesList) {
            notes.add(note.getNoteText());
        }
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, notes);
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
