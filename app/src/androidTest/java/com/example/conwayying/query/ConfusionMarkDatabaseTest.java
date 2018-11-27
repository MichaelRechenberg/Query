package com.example.conwayying.query;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Pair;

import com.example.conwayying.query.data.QueryAppDatabase;
import com.example.conwayying.query.data.QueryAppRepository;
import com.example.conwayying.query.data.entity.AcademicClass;
import com.example.conwayying.query.data.entity.ConfusionMark;
import com.example.conwayying.query.data.entity.ConfusionMarkDao;
import com.example.conwayying.query.data.entity.Lecture;
import com.example.conwayying.query.data.entity.NoteDao;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ConfusionMarkDatabaseTest {

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
    public void testConfusionMarks() throws Exception {

        // Arrange

        // Create two classes with 3 lectures each
        AcademicClass cs465 = new AcademicClass("CS 465");
        AcademicClass cs357 = new AcademicClass("CS 357");

        Long cs465Id = queryAppDB.getAcademicClassDao().insert(cs465);
        Long cs357Id = queryAppDB.getAcademicClassDao().insert(cs357);


        List<Date> testDates = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            Calendar cal = new Calendar.Builder()
                    .set(Calendar.YEAR, 2000)
                    .set(Calendar.MONTH, Calendar.MARCH)
                    .set(Calendar.DAY_OF_MONTH, i)
                    .build();
            Date testDate = cal.getTime();
            testDates.add(testDate);
        }

        // No lambda expressions in Java => Sad Mike
        List<Long> cs357LectureIds = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            Long id = queryAppDB.getLectureDao().insert(new Lecture(testDates.get(i), cs357Id.intValue()));
            cs357LectureIds.add(id);
        }

        Long firstLectureId = cs357LectureIds.get(0);
        Long secondLectureId = cs357LectureIds.get(1);


        Long cs465LectureId = queryAppDB.getLectureDao().insert(new Lecture(testDates.get(0), cs465Id.intValue()));



        // Act
        ConfusionMarkDao confusionMarkDao = queryAppDB.getConfusionMarkDao();

        // The first CS 357 lecture gets 3 confusion marks
        for (int i = 0; i < 3; i++){
            confusionMarkDao.insert(new ConfusionMark(Calendar.getInstance().getTime(), firstLectureId.intValue()));
        }

        // The second CS 357 lecture gets 2 confusion marks
        for (int i = 0; i < 2; i++){
            confusionMarkDao.insert(new ConfusionMark(Calendar.getInstance().getTime(), secondLectureId.intValue()));
        }

        // The only CS 465 lecture gets 7 confusion marks
        for (int i = 0; i < 7; i++){
            confusionMarkDao.insert(new ConfusionMark(Calendar.getInstance().getTime(), cs465LectureId.intValue()));
        }


        // Assert

        // Per-Lecture asserts
        Assert.assertEquals(3, confusionMarkDao.getAllConfusionMarksForLecture(firstLectureId.intValue()).size());
        Assert.assertEquals(2, confusionMarkDao.getAllConfusionMarksForLecture(secondLectureId.intValue()).size());
        Assert.assertEquals(7, confusionMarkDao.getAllConfusionMarksForLecture(cs465LectureId.intValue()).size());
        Assert.assertEquals(0, confusionMarkDao.getAllConfusionMarksForLecture(Integer.MAX_VALUE - 1).size());

        // Per-AcademicClass asserts
        Assert.assertEquals(2 + 3, confusionMarkDao.getAllConfusionMarksForClass(cs357Id.intValue()).size());
        Assert.assertEquals(7, confusionMarkDao.getAllConfusionMarksForClass(cs465Id.intValue()).size());
        Assert.assertEquals(0, confusionMarkDao.getAllConfusionMarksForClass(Integer.MAX_VALUE - 1).size());
    }

    @Test
    public void testResolvedConfusionMarkCount() throws Exception {
        // Arrange
        QueryAppRepository repo = new QueryAppRepository(queryAppDB);


        int testClassId = repo.insert(new AcademicClass("Test Class Title")).intValue();
        int lectureAId = repo.insert(new Lecture(Calendar.getInstance().getTime(), testClassId)).intValue();
        int lectureBId = repo.insert(new Lecture(Calendar.getInstance().getTime(), testClassId)).intValue();

        int otherTestClassId = repo.insert(new AcademicClass("Other Test Class Title")).intValue();
        int otherLectureId = repo.insert(new Lecture(Calendar.getInstance().getTime(), otherTestClassId)).intValue();

        // Act

        // Add 3 confusion marks to lectureA, 2 of which are unresolved
        repo.insert(new ConfusionMark(Calendar.getInstance().getTime(), lectureAId));
        repo.insert(new ConfusionMark(Calendar.getInstance().getTime(), lectureAId));

        ConfusionMark c = new ConfusionMark(Calendar.getInstance().getTime(), lectureAId);
        c.setIsResolved(true);
        repo.insert(c);


        // Add 2 confusion marks to lecture B, one which is resolved and one that is unresolved
        repo.insert(new ConfusionMark(Calendar.getInstance().getTime(), lectureBId));
        c = new ConfusionMark(Calendar.getInstance().getTime(), lectureBId);
        c.setIsResolved(true);
        repo.insert(c);

        // Add 2 confusion marks to otherLectureId, one which is resolved and one that is unresolved
        repo.insert(new ConfusionMark(Calendar.getInstance().getTime(), otherLectureId));
        c = new ConfusionMark(Calendar.getInstance().getTime(), otherLectureId);
        c.setIsResolved(true);
        repo.insert(c);

        // Assert

        // Per-lecture asserts
        Assert.assertEquals(new Pair<Integer, Integer>(1, 2), repo.getConfusionMarkResolvedCountForLecture(lectureAId));
        Assert.assertEquals(new Pair<Integer, Integer>(1, 1), repo.getConfusionMarkResolvedCountForLecture(lectureBId));
        Assert.assertEquals(new Pair<Integer, Integer>(0, 0), repo.getConfusionMarkResolvedCountForLecture(1337));

        // Per-class asserts
        Assert.assertEquals(new Pair<Integer, Integer>(2, 3), repo.getConfusionMarkResolvedCountForClass(testClassId));
        Assert.assertEquals(new Pair<Integer, Integer>(0, 0), repo.getConfusionMarkResolvedCountForClass(1337));
    }
}
