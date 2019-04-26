package com.example.conwayying.query.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.conwayying.query.data.converters.DateConverters;

import java.util.Date;

import io.reactivex.annotations.NonNull;

/**
 * Entity that represents a lecture in our app (like the lecture for October 25 in CS 465)
 */
@Entity(tableName = "Lecture")
@TypeConverters({DateConverters.class})
public class Lecture {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "lecture_id")
    private int mLectureId;

    //The date the lecture was first given
    @NonNull
    @ColumnInfo(name = "lecture_date")
    private Date mLectureDate;

    @ForeignKey(entity = AcademicClass.class, parentColumns = "class_id", childColumns = "class_id")
    @NonNull
    @ColumnInfo(name = "class_id")
    private int mClassId;

    /**
     * Construct a new Lecture
     * @param lectureDate The date when the lecture was first given
     * @param classId The id of the AcademicClass this Lecture is a lecture for
     */
    public Lecture(@NonNull Date lectureDate, int classId){
        this.mLectureDate = lectureDate;
        this.mClassId = classId;
    }

    public int getLectureId() {
        return mLectureId;
    }

    public void setLectureId(int mLectureId) {
        this.mLectureId = mLectureId;
    }

    public Date getLectureDate() {
        return mLectureDate;
    }

    public void setLectureDate(Date mLectureDate) {
        this.mLectureDate = mLectureDate;
    }

    public int getClassId() {
        return mClassId;
    }

    public void setClassId(int mClassId) {
        this.mClassId = mClassId;
    }
}
