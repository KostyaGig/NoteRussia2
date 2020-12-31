package com.kostya.noterussia.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "NoteTb")
public class Note implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int uId;

    @ColumnInfo(name = "text")
    private String text;

    //Время создания заметки для филтра,старые заметки - ниже,новые - выше
    @ColumnInfo(name = "timeStamp")
    private long timeStamp;

    //Выполнена или нет

    @ColumnInfo(name = "done")
    private boolean done;

    public Note() {
    }

    protected Note(Parcel in) {
        uId = in.readInt();
        text = in.readString();
        timeStamp = in.readLong();
        done = in.readByte() != 0;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return uId == note.uId &&
                timeStamp == note.timeStamp &&
                done == note.done &&
                Objects.equals(text, note.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uId, text, timeStamp, done);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(uId);
        parcel.writeString(text);
        parcel.writeLong(timeStamp);
        parcel.writeByte((byte) (done ? 1 : 0));
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Boolean getDone(){
        return done;
    }
}
