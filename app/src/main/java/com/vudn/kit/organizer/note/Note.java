package com.vudn.kit.organizer.note;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;

public class Note implements Parcelable {

    static final String DEFAULT_BODY = "";
    static final int DEFAULT_DATE_TARGET = -1;

    private static final boolean DEFAULT_COMPLETED_STATE = false;
    private static final long CURRENT_TIME = new Date().getTime();

    private String name;
    private String body;
    private long dateTarget;
    private long timeCreated;
    private long timeUpdated;
    private boolean isCompleted;

    public Note(@NonNull String name) {
        this.name = name;
        this.body = DEFAULT_BODY;
        this.dateTarget = DEFAULT_DATE_TARGET;
        this.timeCreated = CURRENT_TIME;
        this.timeUpdated = CURRENT_TIME;
        this.isCompleted = DEFAULT_COMPLETED_STATE;
    }

    public Note(String name, String body, long dateTarget, long timeCreated, long timeUpdated, boolean isCompleted) {
        this.name = name;
        this.body = body;
        this.dateTarget = dateTarget;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
        this.isCompleted = isCompleted;
    }

    private Note(Parcel in) {
        name = in.readString();
        body = in.readString();
        dateTarget = in.readLong();
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

    public long getDateTarget() {
        return dateTarget;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Note copy() {
        return new Note(name, body, dateTarget, timeCreated, timeUpdated, isCompleted);
    }

    public void setUpdated() {
        timeUpdated = new Date().getTime();
    }

    @Override
    public String toString() {
        return "Note{" +
                "name='" + name + '\'' +
                ", body='" + body + '\'' +
                ", dateTarget=" + dateTarget +
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
        dest.writeLong(dateTarget);
        dest.writeLong(timeCreated);
        dest.writeLong(timeUpdated);
        dest.writeInt(isCompleted ? 1 : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        return (name != null ? name.equals(note.name) : note.name == null)
                && (body != null ? body.equals(note.body) : note.body == null)
                && dateTarget == note.dateTarget && isCompleted == note.isCompleted
                && timeCreated == note.timeCreated && timeUpdated == note.timeUpdated;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (int) (dateTarget ^ (dateTarget >>> 32));
        result = 31 * result + (int) (timeCreated ^ (timeCreated >>> 32));
        result = 31 * result + (int) (timeUpdated ^ (timeUpdated >>> 32));
        result = 31 * result + (isCompleted ? 1 : 0);
        return result;
    }
}
