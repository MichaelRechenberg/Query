package com.example.conwayying.query.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.conwayying.query.data.entity.AcademicClass;
import com.example.conwayying.query.data.entity.AcademicClassDao;
import com.example.conwayying.query.data.entity.ConfusionMark;
import com.example.conwayying.query.data.entity.ConfusionMarkDao;
import com.example.conwayying.query.data.entity.Lecture;
import com.example.conwayying.query.data.entity.LectureDao;
import com.example.conwayying.query.data.entity.Note;
import com.example.conwayying.query.data.entity.NoteDao;

/**
 * Database for all Entities for the Query app
 */
@Database(entities = {AcademicClass.class, Lecture.class, Note.class, ConfusionMark.class},
    version = 6)
public abstract class QueryAppDatabase extends RoomDatabase {

    public abstract AcademicClassDao getAcademicClassDao();
    public abstract LectureDao getLectureDao();
    public abstract ConfusionMarkDao getConfusionMarkDao();
    public abstract NoteDao getNoteDao();


    // Implement the Singleton pattern as per https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#6
    private static volatile QueryAppDatabase INSTANCE;

    /**
     * Acquire the Singleton for this
     * @param context The application context
     * @return The singleton
     */
    public static QueryAppDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (QueryAppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            QueryAppDatabase.class, "query_room_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
