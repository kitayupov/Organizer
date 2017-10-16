package com.vudn.kit.organizer.note;

public class Note {

    private String body;

    public Note() {
        body = "";
    }

    public Note(String body) {
        this.body = body;
    }

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
}
