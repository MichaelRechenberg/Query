package com.example.conwayying.query.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.conwayying.query.data.entity.ConfusionMarkDao;
import com.example.conwayying.query.data.entity.ConfusionMark;
import com.example.conwayying.query.data.entity.Lecture;

/**
 * Database for ConfusionMark Entities
 */
@Database(entities = {ConfusionMark.class, Lecture.class},
        version = 3)
public abstract class ConfusionMarkDatabase extends RoomDatabase {

    public abstract ConfusionMarkDao getConfusionMarkDao();


    // Implement the Singleton pattern as per https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#6
    private static volatile ConfusionMarkDatabase INSTANCE;

    /**
     * Acquire the Singleton for this ConfusionMarkDatabase
     * @param context The application context
     * @return The singleton
     */
    public static ConfusionMarkDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (ConfusionMarkDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ConfusionMarkDatabase.class, "query_room_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
