package com.example.minst.team_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.applandeo.materialcalendarview.EventDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by minst on 2018-03-22.
 */

public class NotePreviewActivity extends AppCompatActivity {
    private TextView note;
    // private static List<EventData> myEventManages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_preview_activity);


        Intent intent = getIntent();

        note = (TextView) findViewById(R.id.note);

        if (intent != null) {
            Object event = intent.getParcelableExtra(MyCal.EVENT);
            if (event instanceof MyEventDay) {
                MyEventDay myEventDay = (MyEventDay) event;
                //String set_dd = getFormattedDate(myEventDay.getCalendar().getTime());
                //getSupportActionBar().setTitle(set_dd);
                note.setText(myEventDay.getNote());
                return;
            }
            if (event instanceof EventDay) {
                EventDay eventDay = (EventDay) event;
                // String set_dd = getFormattedDate(eventDay.getCalendar().getTime());
                // getSupportActionBar().setTitle(set_dd);
            }
        }
    }

    public static String getFormattedDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy mm dd", Locale.getDefault());
        return simpleDateFormat.format(date);
    }
}