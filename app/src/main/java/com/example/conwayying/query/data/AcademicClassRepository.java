package com.example.conwayying.query.data;

import android.app.Application;

import com.example.conwayying.query.data.entity.AcademicClass;
import com.example.conwayying.query.data.entity.AcademicClassDao;

import java.util.List;

public class AcademicClassRepository {
    private AcademicClassDao mAcademicClassDao;

    public AcademicClassRepository(Application application){
       mAcademicClassDao =  AcademicClassDatabase.getDatabase(application).getAcademicClassDao();
    }

    public List<AcademicClass> getAllClasses(){
        return mAcademicClassDao.getAllClasses();
    }

    public AcademicClass getAcademicClass(int classId){
        return mAcademicClassDao.getClass(classId);
    }

    /**
     * Insert a new AcademicClass
     * @param academicClass The AcademicClass to insert
     * @return The classId of the newly inserted AcademicClass
     */
    public Long insert(AcademicClass academicClass){
        return mAcademicClassDao.insert(academicClass);
    }
}
