package com.kostya.noterussia.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kostya.noterussia.model.Note;

@Database(entities = {Note.class},version = 1 , exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase dataBase;

    public static synchronized AppDatabase getInstance(Context context){

        if (dataBase == null){
            dataBase = Room.databaseBuilder(context, AppDatabase.class,"Note.db")
                    .allowMainThreadQueries()
                    .build();
        }

        return dataBase;
    }

    public abstract NoteDao getNoteDao();
}
