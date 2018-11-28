package com.example.conwayying.query.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.conwayying.query.data.entity.AcademicClass;
import com.example.conwayying.query.data.entity.AcademicClassDao;
import com.example.conwayying.query.data.entity.ConfusionMark;
import com.example.conwayying.query.data.entity.ConfusionMarkDao;
import com.example.conwayying.query.data.entity.Lecture;
import com.example.conwayying.query.data.entity.LectureDao;
import com.example.conwayying.query.data.entity.Note;
import com.example.conwayying.query.data.entity.NoteDao;

import java.util.Date;

import io.reactivex.annotations.NonNull;

/**
 * Database for all Entities for the Query app
 */
@Database(entities = {AcademicClass.class, Lecture.class, Note.class, ConfusionMark.class},
    version = 7)
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
                            // TODO: Might need to remove allowMainThreadQueries for performance reasons
                            QueryAppDatabase.class, "query_room_database").allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * Instantiates the database each time

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
               new PopulateDbAsync(INSTANCE).execute();
            }
    };

    /**
     * AsyncTask to delete contents of database, then populates it

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final LectureDao mDao;
        private final AcademicClassDao classDao;

        PopulateDbAsync(QueryAppRepository db) {
            mDao = db.getLecture();
            classDao = db.getAcademicClass();
        }

        // insert returns a long
        @Override
        protected Void doInBackground(final Void... params) {
            classDao.deleteAll();
            AcademicClass class1 = new AcademicClass("CS 465");
            long classId = classDao.insert(class1);
            mDao.deleteAll();
            Lecture lecture = new Lecture(new Date(), (int) classId);
            mDao.insert(lecture);

            return null;
        }
    }*/

}
