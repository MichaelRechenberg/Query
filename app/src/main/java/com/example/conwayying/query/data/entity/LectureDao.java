package com.example.conwayying.query.data.entity;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * DAO for Lecture entities
 */
@Dao
public interface LectureDao {

    // Returns the rowid of the row inserted (same as the primary key for a Lecture)
    @Insert
    Long insert(Lecture lecture);

    /**
     * @param lectureId Id of a Lecture
     * @return The Lecture with a given id
     */
    @Query("SELECT * FROM Lecture WHERE lecture_id = :lectureId")
    Lecture getLecture(int lectureId);

    /**
     * @param classId Id of a Class
     * @return The Lectures associated with a given AcademicClass
     */
    @Query("SELECT * FROM Lecture WHERE class_id = :classId")
    List<Lecture> getAllLecturesForClass(int classId);

    @Query("DELETE FROM Lecture")
    void deleteAll();

}
