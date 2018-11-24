package com.example.conwayying.query;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.conwayying.query.data.AcademicClassDatabase;
import com.example.conwayying.query.data.LectureDatabase;
import com.example.conwayying.query.data.NoteDatabase;
import com.example.conwayying.query.data.entity.AcademicClass;
import com.example.conwayying.query.data.entity.Lecture;
import com.example.conwayying.query.data.entity.Note;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Calendar;

@RunWith(AndroidJUnit4.class)
public class NoteDatabaseTest {

    private AcademicClassDatabase academicClassDB;
    private LectureDatabase lectureDB;
    private NoteDatabase noteDB;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getContext();
        academicClassDB = Room.inMemoryDatabaseBuilder(context, AcademicClassDatabase.class).build();
        lectureDB = Room.inMemoryDatabaseBuilder(context, LectureDatabase.class).build();
        noteDB = Room.inMemoryDatabaseBuilder(context, NoteDatabase.class).build();
    }

    @After
    public void closeDb() throws IOException {
        academicClassDB.close();
        noteDB.close();
        lectureDB.close();
    }

    @Test
    public void testUpdateNote() throws Exception {

        // Arrange
        int classId = academicClassDB.getAcademicClassDao().insert(new AcademicClass("Test Class Title")).intValue();
        int lectureId = lectureDB.getLectureDao().insert(new Lecture(Calendar.getInstance().getTime(), classId)).intValue();

        int testNoteId = noteDB.getNoteDao().insert(new Note(lectureId, "This is some test text")).intValue();
        int unmodifiedNoteId = noteDB.getNoteDao().insert(new Note(lectureId, "This text should not be modified")).intValue();

        // Act

        String updatedText = "This is some SUPER AWESOME UPDATED TEXT";
        noteDB.getNoteDao().setNoteText(testNoteId, updatedText);

        // Assert
        Assert.assertEquals("Note should have been updated", updatedText, noteDB.getNoteDao().getNote(testNoteId).getNoteText());
        Assert.assertNotSame("Note should NOT have been updated", updatedText, noteDB.getNoteDao().getNote(unmodifiedNoteId).getNoteText());

    }
}

