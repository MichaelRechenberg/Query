package com.example.conwayying.query.data;

import android.app.Application;

import com.example.conwayying.query.data.entity.Lecture;
import com.example.conwayying.query.data.entity.LectureDao;

import java.util.List;

public class LectureRepository {
    private LectureDao mLectureDao;

    public LectureRepository(Application application){
        mLectureDao =  LectureDatabase.getDatabase(application).getLectureDao();
    }

    /**
     * @param classId The id for the AcademicClass
     * @return List of all the Lectures that are for a given AcademicClass
     */
    public List<Lecture> getAllLecturesForClass(int classId){
        return mLectureDao.getAllLecturesForClass(classId);
    }

    /**
     * @param lectureId The id for the Lecture
     * @return The Lecture
     */
    public Lecture getLecture(int lectureId){
        return mLectureDao.getLecture(lectureId);
    }

    /**
     * Insert a new Lecture
     * @param lecture The Lecture to insert
     * @return The lecture_id of the newly inserted Lecture
     */
    public Long insert(Lecture lecture){
        return mLectureDao.insert(lecture);
    }
}

