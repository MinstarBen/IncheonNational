package com.example.minst.team_project;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by minst on 2018-04-02.
 */

public class UserInfo extends RealmObject {
    private String user_email; //한개만 있어야 되는데.
    private String nick_name;
    private String expected_date;
    private boolean initial_autho;
    private int resultDay;


    public int getResultDay() {
        return resultDay;
    }

    public void setResultDay(int resultDay) {
        this.resultDay = resultDay;
    }




    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getExpected_date() {
        return expected_date;
    }

    public void setExpected_date(String expected_date) {
        this.expected_date = expected_date;
    }

    public boolean getInitial_autho() {
        return initial_autho;
    }

    public void setInitial_autho(boolean initial_autho) {
        this.initial_autho = initial_autho;
    }

}
