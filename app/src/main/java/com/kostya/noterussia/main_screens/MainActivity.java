package com.kostya.noterussia.main_screens;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kostya.noterussia.App;
import com.kostya.noterussia.R;
import com.kostya.noterussia.adapter.NoteAdapter;
import com.kostya.noterussia.details_screens.AddNoteActivity;
import com.kostya.noterussia.model.Note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mainRecView;
    private NoteAdapter adapter;

    private MainViewModel mainViewModel;
    private Observer<List<Note>> observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initRecView();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
            {
                AddNoteActivity.start((Activity) MainActivity.this,null);
            }
        );

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        observer = notes -> {
            adapter.setItems(notes);
        };
    }

    private void initRecView() {

        mainRecView = findViewById(R.id.mainRecView);
        mainRecView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //Разделитель между item
        mainRecView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        adapter = new NoteAdapter();
        mainRecView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_deletes) {
            App.getInstance().getNoteDao().deleteAllNotes();
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainViewModel.getAllNotes().observe(observer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainViewModel.getAllNotes().removeObserver(observer);
    }
}