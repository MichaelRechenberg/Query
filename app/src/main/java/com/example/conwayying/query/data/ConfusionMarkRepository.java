package com.example.conwayying.query.data;

import android.app.Application;

import com.example.conwayying.query.data.entity.ConfusionMark;
import com.example.conwayying.query.data.entity.ConfusionMarkDao;

import java.util.List;

public class ConfusionMarkRepository {

    private ConfusionMarkDao mConfusionMarkDao;

    public ConfusionMarkRepository(Application application){
        mConfusionMarkDao = ConfusionMarkDatabase.getDatabase(application).getConfusionMarkDao();
    }

    /**
     * @param confusionId An id of a ConfusionMark
     * @return The ConfusionMark with the specified confusion_id
     */
    public ConfusionMark getConfusionMark(int confusionId){
        return mConfusionMarkDao.getConfusionMark(confusionId);
    }

    /**
     * @param classId Id of an AcademicClass
     * @return The ConfusionMarks for every Lecture associated with a specified AcademicClass
     */
    public List<ConfusionMark> getAllConfusionMarksForClass(int classId){
        throw new UnsupportedOperationException("Currently not available until we can figure" +
                " out how to issue subquery with Room to another table");
        // return mConfusionMarkDao.getAllConfusionMarksForClass(classId);
    }

    /**
     * @param lectureId Id for a Lecture
     * @return The ConfusionMarks associated with a specified Lecture
     */
    public List<ConfusionMark> getAllConfusionMarksForLecture(int lectureId){
        return mConfusionMarkDao.getAllConfusionMarksForLecture(lectureId);
    }

    /**
     * Insert a new ConfusionMark
     * @param mark The ConfusionMark to insert
     * @return The confusion_id of the newly inserted ConfusionMark
     */
    public Long insert(ConfusionMark mark){
        return mConfusionMarkDao.insert(mark);
    }
}

