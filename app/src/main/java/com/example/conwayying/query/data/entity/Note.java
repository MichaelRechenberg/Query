package com.example.conwayying.query.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.conwayying.query.data.converters.DateConverters;

import java.util.Date;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "Note")
@TypeConverters({DateConverters.class})
public class Note {

    // Id for the note
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "note_id")
    private int mNoteId;

    // Id of the Lecture this Note is associated with
    @ForeignKey(entity = Lecture.class, parentColumns = "lecture_id", childColumns = "lecture_id")
    @NonNull
    @ColumnInfo(name = "lecture_id")
    private int mLectureId;

    // The actual text of the note
    @NonNull
    @ColumnInfo(name = "note_text")
    private String mNoteText;

    // Flag indicating if this note is still private (true if haven't sent it to lecturer)
    @NonNull
    @ColumnInfo(name ="is_private")
    private boolean mIsPrivate;

    // Boolean indicating whether this Note is resolved or not (e.g. does the student
    //  now understand what made him/her confused when they made this Note)
    @NonNull
    @ColumnInfo(name = "is_resolved")
    private boolean mIsResolved;

    // Date when the note was first created
    @NonNull
    @ColumnInfo(name = "date_created")
    private Date mDateCreated;

    // The slide number for the slide deck associated with Lecture with id mLectureId
    @NonNull
    @ColumnInfo(name = "slide_number")
    private int mSlideNumber;

    public Date getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(Date mDateCreated) {
        this.mDateCreated = mDateCreated;
    }

    public int getSlideNumber() {
        return mSlideNumber;
    }

    public void setSlideNumber(int mSlideNumber) {
        this.mSlideNumber = mSlideNumber;
    }

    /**
     * Construct a new Note. It is automatically set to being private and unresolved
     *
     * @param lectureId The id of the Lecture this Note is associated with
     * @param noteText The actual text of the note
     * @param dateCreated The Date this Note was created
     * @param slideNumber The slide number that was on the screen when this Note was created
     */
    public Note(int lectureId, String noteText, Date dateCreated, int slideNumber){
        this.mLectureId = lectureId;
        this.mNoteText = noteText;
        this.mDateCreated = dateCreated;
        this.mSlideNumber = slideNumber;
        this.mIsPrivate = true;
        this.mIsResolved = false;
    }

    public int getNoteId() {
        return mNoteId;
    }

    public void setNoteId(int mNoteId) {
        this.mNoteId = mNoteId;
    }

    public int getLectureId() {
        return mLectureId;
    }

    public void setLectureId(int mLectureId) {
        this.mLectureId = mLectureId;
    }

    public String getNoteText() {
        return mNoteText;
    }

    public void setNoteText(String mNoteText) {
        this.mNoteText = mNoteText;
    }

    public boolean getIsPrivate() {
        return mIsPrivate;
    }

    public void setIsPrivate(boolean mIsPrivate) {
        this.mIsPrivate = mIsPrivate;
    }

    public boolean getIsResolved() {
        return mIsResolved;
    }

    public void setIsResolved(boolean isResolved) {
        this.mIsResolved = isResolved;
    }
}
