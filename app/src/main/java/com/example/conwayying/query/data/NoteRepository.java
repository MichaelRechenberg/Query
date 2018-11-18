package com.example.conwayying.query.data;

import android.app.Application;

import com.example.conwayying.query.data.entity.Note;
import com.example.conwayying.query.data.entity.NoteDao;

import java.util.List;

public class NoteRepository {

    private NoteDao mNoteDao;

    public NoteRepository(Application application) {
        mNoteDao = NoteDatabase.getDatabase(application).getNoteDao();
    }

    /**
     * @param lectureId Id of a Lecture
     * @return All of the Notes for a given Lecture
     */
    public List<Note> getAllNotesForLecture(int lectureId){
        return mNoteDao.getAllNotesOfLecture(lectureId);
    }

    /**
     * @param noteId Id of a Note
     * @return The Note with the specified id
     */
    public Note getNote(int noteId){
        return mNoteDao.getNote(noteId);
    }

    /**
     * Update the text of a Note
     * @param noteId The id of the Note to update
     * @param updatedText The text that will replace the old text for this Note
     */
    public void updateNoteText(int noteId, String updatedText){
        mNoteDao.setNoteText(noteId, updatedText);
    }

    public Long insert(Note note){
        return mNoteDao.insert(note);
    }
}


