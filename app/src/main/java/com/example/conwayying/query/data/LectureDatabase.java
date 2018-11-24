package com.example.conwayying.query.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.conwayying.query.data.entity.LectureDao;
import com.example.conwayying.query.data.entity.Lecture;

/**
 * Database for Lecture Entities
 */
@Database(entities = {Lecture.class},
        version = 2)
public abstract class LectureDatabase extends RoomDatabase {

    public abstract LectureDao getLectureDao();


    // Implement the Singleton pattern as per https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#6
    private static volatile LectureDatabase INSTANCE;

    /**
     * Acquire the Singleton for this LectureDatabase
     * @param context The application context
     * @return The singleton
     */
    public static LectureDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (LectureDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LectureDatabase.class, "query_room_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

