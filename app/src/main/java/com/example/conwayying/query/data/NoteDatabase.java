package com.example.conwayying.query.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.conwayying.query.data.entity.NoteDao;
import com.example.conwayying.query.data.entity.Note;

/**
 * Database for Note Entities
 */
@Database(entities = {Note.class},
        version = 4)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao getNoteDao();


    // Implement the Singleton pattern as per https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#6
    private static volatile NoteDatabase INSTANCE;

    /**
     * Acquire the Singleton for this NoteDatabase
     * @param context The application context
     * @return The singleton
     */
    public static NoteDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (NoteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NoteDatabase.class, "query_room_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}


