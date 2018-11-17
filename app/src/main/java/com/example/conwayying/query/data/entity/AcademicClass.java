package com.example.conwayying.query.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;


/**
 * Entity that represents a class like "CS 465" in our app
 */
@Entity(tableName = "Academic_Class")
public class AcademicClass {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "class_id")
    private int mClassId;

    // The title of the class, like "CS 465"
    @NonNull
    @ColumnInfo(name = "class_title")
    private String mClassTitle;

    public AcademicClass(@NonNull String mClassTitle) {
        this.mClassTitle = mClassTitle;
    }

    public int getClassId() {
        return mClassId;
    }

    public String getClassTitle() {
        return mClassTitle;
    }

    public void setClassTitle(String classTitle){
        this.mClassTitle = classTitle;
    }

    public void setClassId(int classId){
        this.mClassId = classId;
    }
}
