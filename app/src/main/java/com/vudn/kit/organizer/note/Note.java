package com.vudn.kit.organizer.note;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {

    private String body;

    public Note() {
        body = "";
    }

    public Note(String body) {
        this.body = body;
    }

    private Note(Parcel in) {
        body = in.readString();
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

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Note{" +
                "body='" + body + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(body);
    }
}
