package com.kostya.noterussia.main_screens;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kostya.noterussia.App;
import com.kostya.noterussia.model.Note;

import java.util.List;

//Создали для LiveData,логики не будет :)
public class MainViewModel extends ViewModel {

    private LiveData<List<Note>> getAllNotes = App.getInstance().getNoteDao().getAllLiveDataNotes();

    public LiveData<List<Note>> getAllNotes(){
        return getAllNotes;
    }

}
