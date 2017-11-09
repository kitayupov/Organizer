package com.vudn.kit.organizer.note;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;

public class Note implements Parcelable {

    private String name;
    private String body;
    private long targetDate;
    private long timeCreated;
    private long timeUpdated;
    private boolean isCompleted;

    public Note(@NonNull String name) {
        this.name = name;
        this.body = "";
        this.targetDate = -1;
        this.timeCreated = new Date().getTime();
        this.timeUpdated = new Date().getTime();
        this.isCompleted = false;
    }

    public Note(String name, String body, long targetDate, long timeCreated, long timeUpdated, boolean isCompleted) {
        this.name = name;
        this.body = body;
        this.targetDate = targetDate;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
        this.isCompleted = isCompleted;
    }

    private Note(Parcel in) {
        name = in.readString();
        body = in.readString();
        targetDate = in.readLong();
        timeCreated = in.readLong();
        timeUpdated = in.readLong();
        isCompleted = in.readInt() == 1;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public long getTimeUpdated() {
        return timeUpdated;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Note copy() {
        return new Note(name, body, targetDate, timeCreated, timeUpdated, isCompleted);
    }

    public void setUpdated() {
        timeUpdated = new Date().getTime();
    }

    @Override
    public String toString() {
        return "Note{" +
                "name='" + name + '\'' +
                ", body='" + body + '\'' +
                ", targetDate=" + targetDate +
                ", timeCreated=" + timeCreated +
                ", timeUpdated=" + timeUpdated +
                ", isCompleted=" + isCompleted +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(body);
        dest.writeLong(targetDate);
        dest.writeLong(timeCreated);
        dest.writeLong(timeUpdated);
        dest.writeInt(isCompleted ? 1 : 0);
    }
}
