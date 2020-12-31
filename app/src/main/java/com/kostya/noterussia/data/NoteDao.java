package com.kostya.noterussia.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.kostya.noterussia.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    //Если мы вставляем заметку в бд с уже существующим id,то она заменить страрую заметку
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("SELECT * FROM NoteTb")
    LiveData<List<Note>> getAllLiveDataNotes();

    @Query("SELECT * FROM NoteTb")
    List<Note> getAllNotes();

    //Выборка по id
    @Query("SELECT * FROM NoteTb WHERE uId = :uId LIMIT 1")
    Note findNoteById(int uId);

    @Query("DELETE FROM NoteTb")
    void deleteAllNotes();
}
