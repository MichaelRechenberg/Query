package com.example.conwayying.query;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Pair;

import com.example.conwayying.query.data.QueryAppDatabase;
import com.example.conwayying.query.data.QueryAppRepository;
import com.example.conwayying.query.data.entity.AcademicClass;
import com.example.conwayying.query.data.entity.Lecture;
import com.example.conwayying.query.data.entity.Note;
import com.example.conwayying.query.data.entity.NoteDao;

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

    @Test
    public void testNoteResolvedCount() throws Exception {

        // Arrange
        QueryAppRepository repo = new QueryAppRepository(queryAppDB);

        int testClassId = repo.insert(new AcademicClass("Test Class Title")).intValue();
        int lectureAId = repo.insert(new Lecture(Calendar.getInstance().getTime(), testClassId)).intValue();
        int lectureBId = repo.insert(new Lecture(Calendar.getInstance().getTime(), testClassId)).intValue();

        int otherTestClassId = repo.insert(new AcademicClass("Other Test Class Title")).intValue();
        int otherLectureId = repo.insert(new Lecture(Calendar.getInstance().getTime(), otherTestClassId)).intValue();


        // Act


        // Add 3 notes to lectureA, 2 of which are unresolved
        repo.insert(new Note(lectureAId, "Unresolved 1"));
        repo.insert(new Note(lectureAId, "Unresolved 2"));
        Note n = new Note(lectureAId, "Resolved 1");
        n.setIsResolved(true);
        repo.insert(n);


        // Add 2 notes to lecture B, one which is resolved and one that is unresolved
        repo.insert(new Note(lectureBId, "Unresolved 1"));
        n = new Note(lectureBId, "Resolved 1");
        n.setIsResolved(true);
        repo.insert(n);

        // Add 2 notes to otherLectureId, one which is resolved and one that is unresolved
        repo.insert(new Note(otherLectureId, "Unresolved 1"));
        n = new Note(otherLectureId, "Resolved 1");
        n.setIsResolved(true);
        repo.insert(n);

        // Assert

        // Per-lecture asserts
        Assert.assertEquals(new Pair<Integer, Integer>(1, 2), repo.getNoteResolvedCountForLecture(lectureAId));
        Assert.assertEquals(new Pair<Integer, Integer>(1, 1), repo.getNoteResolvedCountForLecture(lectureBId));
        Assert.assertEquals(new Pair<Integer, Integer>(0, 0), repo.getNoteResolvedCountForLecture(1337));

        // Per-class asserts
        Assert.assertEquals(new Pair<Integer, Integer>(2, 3), repo.getNoteResolvedCountForClass(testClassId));
        Assert.assertEquals(new Pair<Integer, Integer>(0, 0), repo.getNoteResolvedCountForClass(1337));

    }
}

