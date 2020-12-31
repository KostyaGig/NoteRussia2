package com.kostya.noterussia.details_screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kostya.noterussia.App;
import com.kostya.noterussia.R;
import com.kostya.noterussia.model.Note;

import java.util.Currency;

public class AddNoteActivity extends AppCompatActivity {

    public static final String EXTRA_CURRENT_NOTE = "com.kostya.noterussia.details_screens.CURRENT.NOTE";

    private EditText edText;

    private Note note;

    //Этот код нужен для того,чтобы по постоянно не писать его
    //1 параметр - Activity(Context), 2 - note,по которому мы нажали из адаптера
    //Здесь мы создаем inetnt и если note по которому мы нажали из адаптера не равен null,то мы отправляем данный note и уже здесь обрабатываем его
    //Например обновляем ui,тоесть заполняем данными из прешедшего Note
    //Если он равен null,то это значит,что мы просто нажали на fab add из нашего MainActivty и просто хотим открыть это активити)))
    public static void start(Activity caller, Note note){
        Intent intent = new Intent(caller,AddNoteActivity.class);

        if (note != null){
            intent.putExtra(EXTRA_CURRENT_NOTE,note);
        }
        caller.startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note_activity);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        edText = findViewById(R.id.ed_text);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_CURRENT_NOTE)){
            //Принимаем note и устанавливаем title у toolbar EditNote
            getSupportActionBar().setTitle("Edit Note");

            //received our clicked Note in adapter
            note = getIntent().getParcelableExtra(EXTRA_CURRENT_NOTE);

            //update UI
            edText.setText(note.getText());
        } else {
            //Иначе создаем новую заметку,котрую мы будем вставлять в нашу бд при нажатии на icon save in toolbar menu check 86 string code
            note = new Note();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_saves) {
            saveNote();
        }
        else if (id == android.R.id.home){
            finish();
        }

        return true;
    }

    private void saveNote() {
        String text = edText.getText().toString();

        if (TextUtils.isEmpty(text)){
            Toast.makeText(this, "Введите текст!", Toast.LENGTH_SHORT).show();
        } else {
            //Инициализируем нашу заметку,заполняя данными
            note.setText(text);
            note.setDone(false);
            note.setTimeStamp(System.currentTimeMillis());

            //Если  это существующая заметка,то мы ее обновляем
            //Если нет - добавляем в бд
            if (getIntent().hasExtra(EXTRA_CURRENT_NOTE)){
                //Если у нас существую extra,то это значит,что мы не добавли новую заметку,а кликнули на нее из adapter,а значит мы ее обновляем
                App.getInstance().getNoteDao().updateNote(note);
            } else {
                //Вставляем нашу note в бд
                App.getInstance().getNoteDao().insertNote(note);
            }
            finish();
        }
    }

}
