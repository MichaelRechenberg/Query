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
 * Entity to represent a mark of confusion at a specific timestamp
 *  or over an interval of time
 */
@Entity(tableName = "ConfusionMark")
@TypeConverters({DateConverters.class})
public class ConfusionMark {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "confusion_id")
    private int mConfusionId;

    //The date the confusion mark was first made
    @NonNull
    @ColumnInfo(name = "start_datetime")
    private Date mStartDate;

    //The date the confusion mark was concluded (null for "WTF" marks)
    @ColumnInfo(name = "end_datetime")
    private Date mEndDate;

    // The Lecture that this ConfusionMark was made for
    @ForeignKey(entity = Lecture.class, parentColumns = "lecture_id", childColumns = "lecture_id")
    @NonNull
    @ColumnInfo(name = "lecture_id")
    private int mLectureId;

    // Boolean indicating whether this ConfusionMark is resolved or not (e.g. does the student
    //  now understand what made him/her confused when they made this ConfusionMark)
    @NonNull
    @ColumnInfo(name = "is_resolved")
    private boolean mIsResolved;

    // The slide number for the slide deck associated with Lecture with id mLectureId
    @NonNull
    @ColumnInfo(name = "slide_number")
    private int mSlideNumber;

    /**
     * Represent a confusion mark
     *
     * By default, the endDate and isResolved fields are set to null and false, respectively
     *
     * To represent an interval of time, set endDate with setEndDate after calling
     *  the constructor
     *
     * To mark a ConfusionMark as resolved, call setIsResolved()
     *
     * @param startDate The start date for the confusion mark
     * @param lectureId The id of the lecture that this confusion mark was made for
     * @param slideNumber The slide number that was present on the screen when this ConfusionMark was made
     */
    public ConfusionMark(@NonNull Date startDate, int lectureId, int slideNumber){
        this.mStartDate = startDate;
        this.mEndDate = null;
        this.mLectureId = lectureId;
        this.mSlideNumber = slideNumber;
        this.mIsResolved = false;
    }

    /**
     * @param mark A ConfusionMark
     * @return Whether a given ConfusionMark is for an interval of time
     */
    public static boolean isConfusionInterval(ConfusionMark mark){
        return mark.mEndDate == null;
    }

    public int getConfusionId() {
        return mConfusionId;
    }

    public void setConfusionId(int mConfusionId) {
        this.mConfusionId = mConfusionId;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date mStartDate) {
        this.mStartDate = mStartDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date mEndDate) {
        this.mEndDate = mEndDate;
    }

    public int getLectureId() {
        return mLectureId;
    }

    public void setLectureId(int mLectureId) {
        this.mLectureId = mLectureId;
    }

    public boolean getIsResolved() {
        return mIsResolved;
    }

    public void setIsResolved(boolean isResolved) {
        this.mIsResolved = isResolved;
    }

    public int getSlideNumber() {
        return mSlideNumber;
    }

    public void setSlideNumber(int slideNumber) {
        this.mSlideNumber = slideNumber;
    }
}
