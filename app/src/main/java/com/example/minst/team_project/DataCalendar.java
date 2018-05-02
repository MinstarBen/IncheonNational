package com.example.minst.team_project;

import io.realm.RealmObject;

/**
 * Created by minst on 2018-03-29.
 */

public class DataCalendar extends RealmObject {
    private long millis;
    private String note; // 노트
    private String mail; // who
    private int image; // image resource

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public long getTime() {
        return millis;
    }

    public String getNote() {
        return note;
    }

    public int getImage() {
        return image;
    }

    public void setDate(long time) {
        this.millis = time;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setImage(int image) {
        this.image = image;
    }

}

