package com.example.conwayying.query.data.entity;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * DAO for Note entities
 */
@Dao
public interface NoteDao {

    // Returns the rowid of the row inserted
    @Insert
    Long insert(Note note);

    /**
     * @param noteId The id of a Note
     * @return The Note with a give note_id
     */
    @Query("SELECT * FROM Note WHERE note_id = :noteId")
    Note getNote(int noteId);

    /**
     * @param lectureId Id of a Lecture
     * @return All of the Notes associated with a given Lecture
     */
    @Query("SELECT * FROM Note WHERE lecture_id = :lectureId")
    List<Note> getAllNotesOfLecture(int lectureId);

    /**
     * Modify the note text for a given Note
     * @param noteId Id of the Note to modify
     * @param noteText The new text for the Note
     */
    @Query("UPDATE Note SET note_text = :noteText WHERE note_id = :noteId")
    void setNoteText(int noteId, String noteText);
}


