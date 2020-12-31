package com.kostya.noterussia.adapter;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.kostya.noterussia.App;
import com.kostya.noterussia.R;
import com.kostya.noterussia.details_screens.AddNoteActivity;
import com.kostya.noterussia.model.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    //for create animation on recyclerview
    private SortedList<Note> sortedList;

    public NoteAdapter() {
        sortedList = new SortedList<>(Note.class, new SortedList.Callback<Note>() {

            @Override
            public int compare(Note o1, Note o2) {
                //Узнает какой из элементов больше
                //Если один не сделан,а второй сделан,то второй больше
                if (!o2.getDone() && o1.getDone()){
                    return 1;
                }
                //Если лдин сделан и воторой не сделан,то второй больше
                if (o2.getDone() && !o1.getDone()){
                    return -1;
                }

                //Если они равны,тоесть либо оба сделаны,либо оба не сделаны ,то сортируем их по времени
                return (int) (o2.getTimeStamp() - o1.getTimeStamp());
            }

            @Override
            public void onChanged(int position, int count) {
                //Когда меняется элемент в позиции изменяются отдельные элементы в диапозоне (int position, int count),что позволет обновлять конкретные
                //элементы и не трогать остальные,что улучшает производительность
                notifyItemRangeChanged(position,count);
            }

            @Override
            public boolean areContentsTheSame(Note oldItem, Note newItem) {
                //возращает true если 2 элемента равны полностью (тоесть содержмиое,логические значения,их id)
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Note item1, Note item2) {
                //Содержимое элементов может быть разное,но id одинаковые
                return item1.getuId() == item2.getuId();
            }

            @Override
            public void onInserted(int position, int count) {
                //Вставляет в определенную позицию
                notifyItemRangeInserted(position,count);
            }

            @Override
            public void onRemoved(int position, int count) {
                //Удаляет в определенной позицию
                notifyItemRangeRemoved(position,count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                //Вставляет в определенную позицию
                notifyItemMoved(fromPosition,toPosition);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(sortedList.get(position));
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        public TextView noteText;
        public CheckBox completed;
        public ImageView deleted;

        public Note note;
        public boolean silentUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noteText = itemView.findViewById(R.id.note_text);
            completed = itemView.findViewById(R.id.completed);
            deleted = itemView.findViewById(R.id.deleted);

            completed.setOnCheckedChangeListener((compoundButton, b) -> {
                //Если по нажатию наш silentupdate = false,то мы устанавливаем значение нащей note.setDone(b) а затем обновляем нашу бд
                if (!silentUpdate){
                    note.setDone(b);
                    App.getInstance().getNoteDao().updateNote(note);
                }
                updateStrokeOut();
            });

            deleted.setOnClickListener(view -> App.getInstance().getNoteDao().deleteNote(note));

            //По нажатию на сам Item
            itemView.setOnClickListener(view -> AddNoteActivity.start((Activity) itemView.getContext(),note));
        }

        void bind(Note note){
            this.note = note;

            noteText.setText(note.getText());
            updateStrokeOut();

            silentUpdate = true;
            completed.setChecked(note.getDone());
            silentUpdate = false;
        }

        //Для чекбокса
        private void updateStrokeOut(){

            if (note.getDone()){
                //Если он равен true (тоесть заметка выполнена,то мы ее зачеркиваем
                //Paint.STRIKE_THRU_TEXT_FLAG - зачеркивает текст
                noteText.setPaintFlags(noteText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                //Иначе МЫ с помощью ~Paint.STRIKE_THRU_TEXT_FLAG отчеркиваем
                //ТОесть ~Paint.STRIKE_THRU_TEXT_FLAG является обратно функции Paint.STRIKE_THRU_TEXT_FLAG
                noteText.setPaintFlags(noteText.getPaintFlags() &  ~Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }

    //В этот метод передается уже обновленный список,тоесть список из Livedata
    //Например мы произвели какие- либо действия с бд.вставили туда новую заметку,соответственно обновитьяс Livedata
    // и мы вызовем у адаптера данный метод,передав в параметр новый list
    //мы старый sortedlist заменяем на новый,таким образом обновили наш adapter)
    public void setItems(List<Note> notes){
        //sortedlist сразвнивает свой старый список с новым (который мы ему передали) затем сотрит отличие и обновляем нужные item,
        // опять же повышет производительность нашего приложения,Он не обновляет полностью а обновляет нужные
        sortedList.replaceAll(notes);
    }

}
