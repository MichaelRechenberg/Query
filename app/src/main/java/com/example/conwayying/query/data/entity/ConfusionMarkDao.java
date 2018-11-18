package com.example.conwayying.query.data.entity;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * DAO for ConfusionMark entities
 */
@Dao
public interface ConfusionMarkDao {

    // Returns the rowid of the row inserted
    @Insert
    Long insert(ConfusionMark confusionMark);

    /**
     * @param confusionMarkId Id of a ConfusionMark
     * @return The ConfusionMark with a specified id
     */
    @Query("SELECT * FROM ConfusionMark WHERE confusion_id = :confusionMarkId")
    ConfusionMark getConfusionMark(int confusionMarkId);

    /**
     * @param lectureId Id of a Lecture
     * @return The ConfusionMarks for a given Lecture
     */
    @Query("SELECT * FROM ConfusionMark WHERE lecture_id = :lectureId")
    List<ConfusionMark> getAllConfusionMarksForLecture(int lectureId);

    // TODO: Figure out why this always returns no results
    // I think the issue is that we are trying to refer to another table (Lecture) other than ConfusionMark
    // I tried running (SELECT * FROM Lecture) in a separate query and this returned 0 results
    // Perhaps use @RawQuery? Perhaps its a limitation with SQLlite?
    /**
     * @param classId Id of the AcademicClass you want to retrieve all ConfusionMarks for
     * @return The ConfusionMarks for all Lectures that associated with a specified AcademicClass
     */
    @Query(
            "SELECT *" +
            "FROM ConfusionMark " +
            "WHERE ConfusionMark.lecture_id IN (SELECT Lecture.lecture_id AS foo FROM Lecture WHERE Lecture.class_id = :classId)"
    )
    List<ConfusionMark> getAllConfusionMarksForClass(int classId);
}

