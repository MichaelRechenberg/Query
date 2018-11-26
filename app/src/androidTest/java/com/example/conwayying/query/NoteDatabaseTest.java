package com.example.conwayying.query;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.conwayying.query.data.QueryAppDatabase;
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

    private QueryAppDatabase queryAppDB;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getContext();
        queryAppDB = Room.inMemoryDatabaseBuilder(context, QueryAppDatabase.class).build();
    }

    @After
    public void closeDb() throws IOException {
        queryAppDB.close();
    }

    @Test
    public void testUpdateNote() throws Exception {

        // Arrange
        int classId = queryAppDB.getAcademicClassDao().insert(new AcademicClass("Test Class Title")).intValue();
        int lectureId = queryAppDB.getLectureDao().insert(new Lecture(Calendar.getInstance().getTime(), classId)).intValue();

        int testNoteId = queryAppDB.getNoteDao().insert(new Note(lectureId, "This is some test text")).intValue();
        int unmodifiedNoteId = queryAppDB.getNoteDao().insert(new Note(lectureId, "This text should not be modified")).intValue();

        // Act

        String updatedText = "This is some SUPER AWESOME UPDATED TEXT";
        queryAppDB.getNoteDao().setNoteText(testNoteId, updatedText);

        // Assert
        Assert.assertEquals("Note should have been updated", updatedText, queryAppDB.getNoteDao().getNote(testNoteId).getNoteText());
        Assert.assertNotSame("Note should NOT have been updated", updatedText, queryAppDB.getNoteDao().getNote(unmodifiedNoteId).getNoteText());

    }
}

