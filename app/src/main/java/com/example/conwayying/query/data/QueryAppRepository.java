package com.example.conwayying.query.data;

import android.app.Application;

import com.example.conwayying.query.data.entity.AcademicClass;
import com.example.conwayying.query.data.entity.AcademicClassDao;
import com.example.conwayying.query.data.entity.ConfusionMark;
import com.example.conwayying.query.data.entity.ConfusionMarkDao;
import com.example.conwayying.query.data.entity.Lecture;
import com.example.conwayying.query.data.entity.LectureDao;
import com.example.conwayying.query.data.entity.Note;
import com.example.conwayying.query.data.entity.NoteDao;

import java.util.List;

/**
 * Repository for all entities for the Query app
 */
public class QueryAppRepository {
    private AcademicClassDao mAcademicClassDao;
    private LectureDao mLectureDao;
    private NoteDao mNoteDao;
    private ConfusionMarkDao mConfusionMarkDao;

    public QueryAppRepository(Application application){
        QueryAppDatabase db = QueryAppDatabase.getDatabase(application);
        mAcademicClassDao = db.getAcademicClassDao();
        mLectureDao = db.getLectureDao();
        mNoteDao = db.getNoteDao();
        mConfusionMarkDao = db.getConfusionMarkDao();
    }

    /**
     * @return All the AcademicClass entities
     */
    public List<AcademicClass> getAllClasses(){
        return mAcademicClassDao.getAllClasses();
    }

    /**
     * @param classId An id of an AcademicClass
     * @return The AcademicClass with the given classId
     */
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

    /**
     * @param lectureId Id of a Lecture
     * @return All of the Notes for a given Lecture
     */
    public List<Note> getAllNotesForLecture(int lectureId){
        return mNoteDao.getAllNotesOfLecture(lectureId);
    }

    /**
     * @param noteId Id of a Note
     * @return The Note with the specified id
     */
    public Note getNote(int noteId){
        return mNoteDao.getNote(noteId);
    }

    /**
     * Update the text of a Note
     * @param noteId The id of the Note to update
     * @param updatedText The text that will replace the old text for this Note
     */
    public void updateNoteText(int noteId, String updatedText){
        mNoteDao.setNoteText(noteId, updatedText);
    }

    public Long insert(Note note){
        return mNoteDao.insert(note);
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
        return mConfusionMarkDao.getAllConfusionMarksForClass(classId);
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
