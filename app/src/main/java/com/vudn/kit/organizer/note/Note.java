package com.vudn.kit.organizer.note;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {

    private String body;
    private long timeCreated;
    private long timeUpdated;

    public Note(String body, long timeCreated, long timeUpdated) {
        this.body = body;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
    }

    private Note(Parcel in) {
        body = in.readString();
        timeCreated = in.readLong();
        timeUpdated = in.readLong();
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

    @Override
    public String toString() {
        return "Note{" +
                "body='" + body + '\'' +
                ", timeCreated=" + timeCreated +
                ", timeUpdated=" + timeUpdated +
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
    }
}
