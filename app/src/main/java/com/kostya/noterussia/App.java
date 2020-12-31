package com.kostya.noterussia;

import android.app.Application;

import com.kostya.noterussia.data.AppDatabase;
import com.kostya.noterussia.data.NoteDao;

public class App extends Application {

    //Класс,который живет вечно и я думаю логично создать нашу базу данных на самом старте приложения
    //Даже перед вызовом метода onCreate в MainActivity
    //Наша база данных создастся 1 раз и будет жить вечно,пока юзер не захочет удалить наше приложение :)

    private NoteDao noteDao;

    private static App instance;

    public static App getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Данный метод вызывается Даже перед вызовом метода onCreate в MainActivity
        //Здесь Initialize our AppDatabase

        AppDatabase database = AppDatabase.getInstance(getApplicationContext());
        noteDao = database.getNoteDao();
    }

    public NoteDao getNoteDao(){
        return noteDao;
    }

}
