package com.example.minst.team_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;

import java.text.SimpleDateFormat;

import io.realm.DynamicRealm;
import io.realm.Realm;

/**
 * Created by minst on 2018-03-22.
 */

public class AddNoteActivity extends AppCompatActivity {
    private static final String TAG = AddNoteActivity.class.getSimpleName();
    private CalendarView datePicker; // calendar view.
    private AppCompatButton button;
    private EditText noteEditText; // 글쓰는 공간
    private TextView text;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Realm.init(getApplicationContext());


        datePicker = findViewById(R.id.datePicker);
        button = findViewById(R.id.addNoteButton);
        noteEditText = findViewById(R.id.noteEditText);

        button.setOnClickListener(new View.OnClickListener() { //저장 버튼을 누르면
            @Override
            public void onClick(View v) { //객체의 초기화
                Intent returnIntent = new Intent(); // 이벤트 생성.
                MyEventDay myEventDay = new MyEventDay(
                        datePicker.getSelectedDate(), // 누른 날짜
                        noteEditText.getText().toString(), // 날짜 밑에 넣을 로고
                        R.drawable.logo // 입력한 텍스트
                );

                realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        DataCalendar dataCalendar = realm.createObject(DataCalendar.class);
                        dataCalendar.setDate(datePicker.getSelectedDate().getTimeInMillis());
                        dataCalendar.setNote(noteEditText.getText().toString());
                        dataCalendar.setImage(R.drawable.logo);
                        dataCalendar.setMail(getIntent().getStringExtra("email"));

                    }
                });

                returnIntent.putExtra(MyCal.RESULT, myEventDay); // 메인으로 넘겨줌.
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }

    public static String calendarToString(java.util.Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        String calendarString = simpleDateFormat.format(calendar.getTime());
        return calendarString;
    }


}
