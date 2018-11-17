package com.example.conwayying.query.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.conwayying.query.data.entity.AcademicClass;
import com.example.conwayying.query.data.entity.AcademicClassDao;

/**
 * Database for AcademicClass Entities
 */
@Database(entities = {AcademicClass.class},
    version = 1)
public abstract class AcademicClassDatabase extends RoomDatabase {

    public abstract AcademicClassDao getAcademicClassDao();


    // Implement the Singleton pattern as per https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#6
    private static volatile AcademicClassDatabase INSTANCE;

    /**
     * Acquire the Singleton for this AcademicClassDatabase
     * @param context The application context
     * @return The singleton
     */
    public static AcademicClassDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (AcademicClassDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AcademicClassDatabase.class, "query_room_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
