package com.example.minst.team_project;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.CalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by minst on 2018-03-22.
 */
public class MyCal extends AppCompatActivity {
    Realm realm;
    TextView t1;
    private static final String TAG = MyCal.class.getSimpleName();
    public static final String RESULT = "result";
    public static final String EVENT = "event";
    private static final int ADD_NOTE = 44;
    private CalendarView mCalendarView;
    private ArrayList<EventDay> mEventDays = new ArrayList<>(); // 이게 이벤트 관리 같은거.
    private FloatingActionButton floatingActionButton;
    private ArrayList<DataCalendar> arrayList = new ArrayList<>();
    //private  ArrayList<EventDay> myEvent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // db에 있는것을 arraylist로 가지고 옴.

        /*update();*/

        /*Realm.init(this);
        Realm.setDefaultConfiguration(getRealmConfig());*/
        Intent cal_intent = getIntent();

        t1 = (TextView) findViewById(R.id.confirm);

        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton); //이게 글쓰기 버튼
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote(); //버튼 누르면 addNote함수 실행
            }
        });

        mCalendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) { // 이건 그냥 날짜 누르면 이동하는 함수
                previewNote(eventDay);
            }
        });

        mCalendarView.setEvents(getmEventDays());
/*
        t1.setText(Integer.toString(arrayList.size()));
*/
    }

   /* private RealmConfiguration getRealmConfig() {
        return new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
    }*/

   public ArrayList<EventDay> getmEventDays() { // arraylist gets a data and print that.
        arrayList = (ArrayList<DataCalendar>) getEventlist().clone();
        for (int k = 0; k < arrayList.size(); k++) {
            long date = arrayList.get(k).getTime();
            Calendar calendar = (Calendar) Calendar.getInstance();
            calendar.setTimeInMillis(date);
            String not = arrayList.get(k).getNote(); // note
            int ima = arrayList.get(k).getImage(); // imageresource

            mEventDays.add(k, new MyEventDay(calendar, not, ima));
        }
        return mEventDays;
    }

    public ArrayList<DataCalendar> getEventlist() { // DB에 저장된 객체들 가져오기
        ArrayList<DataCalendar> list = new ArrayList<>();

        try {
            realm.init(this);
            realm = Realm.getDefaultInstance();
            RealmResults<DataCalendar> results = realm.where(DataCalendar.class).findAll();
            list.addAll(realm.copyFromRealm(results));
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return list; // list는 디비정보 다있음
    }

    /*public void update(){
        Realm.init(this);
        realm=Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction(){

            @Override
            public void execute(Realm realm) {
                for(int i=0;i<arrayList.size();i++){
                    DataCalendar dataCalendar = realm.where(DataCalendar.class).equalTo("image",R.drawable.loading).findFirst();
                    dataCalendar.setImage(R.drawable.logo);
                }
            }
        });

    }*/

    @Override // 등록하는 함수
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //성공시 ? 이벤트 등록.
        if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
            mCalendarView.setEvents(getmEventDays());
        }
    }


    private void addNote() { //글쓰기로 이동하는
        Intent intent = new Intent(MyCal.this, AddNoteActivity.class); //액티비티 이동
        intent.putExtra("email", getIntent().getStringExtra("email")); // 이메일 이동
        startActivityForResult(intent, ADD_NOTE);
    }

    private void previewNote(EventDay eventDay) { //이건 날짜를 눌렀을때 이동
       // Intent intent = new Intent(MyCal.this, NotePreviewActivity.class);
        if (eventDay instanceof MyEventDay) {
            //intent.putExtra(EVENT, (MyEventDay) eventDay);
            Toast.makeText(MyCal.this, ((MyEventDay) eventDay).getNote(), Toast.LENGTH_SHORT).show();
        }
        //startActivity(intent);
    }

   /* @Override // 안드로이드 디폴트 백 버튼이 눌렸을 떄.
    public void onBackPressed() {
        Intent content = new Intent(MyCal.this,SuccessActivity.class);
        startActivity(content);
        finish();
    }*/
}
