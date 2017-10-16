package com.vudn.kit.organizer.note;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {

    private String body;
    private long timeCreated;
    private long timeUpdated;
    private boolean isCompleted;

    public Note(String body, long timeCreated, long timeUpdated, boolean isCompleted) {
        this.body = body;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
        this.isCompleted = isCompleted;
    }

    private Note(Parcel in) {
        body = in.readString();
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

    @Override
    public String toString() {
        return "Note{" +
                "body='" + body + '\'' +
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
        dest.writeString(body);
        dest.writeLong(timeCreated);
        dest.writeLong(timeUpdated);
        dest.writeInt(isCompleted ? 1 : 0);
    }
}
