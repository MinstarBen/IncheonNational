package com.example.minst.team_project;

import io.realm.RealmObject;

/**
 * Created by minst on 2018-04-07.
 */

public class Certification extends RealmObject {
    private String certificode;
    private boolean approvement;

    public String getCertificode() {
        return certificode;
    }

    public void setCertificode(String certificode) {
        this.certificode = certificode;
    }

    public boolean getApprovement() {
        return approvement;
    }

    public void setApprovement(boolean approvement) {
        this.approvement = approvement;
    }

}
