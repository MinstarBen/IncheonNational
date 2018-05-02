package com.example.minst.team_project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static android.view.ViewGroup.*;


/**
 * Created by minst on 2018-03-22.
 */
public class SuccessActivity extends AppCompatActivity {
    private static final String TAG = SuccessActivity.class.getSimpleName(); // TAG찍는것
    ImageButton ib1, ib2, ib3, ib4; //버튼 4개
    Button b1;
    FloatingActionButton dday; //캘린더 버튼
    TextView year, text_calendar, expectedDay, fromthatday, rlaalstn, rlaalstn2, rlaalstn3;
    ProgressBar progressBar;
    Realm realm;
    ImageView imageView;
    private ArrayList<DataCalendar> arrayList = new ArrayList<>();
    private ArrayList<UserInfo> userInfoArrayList = new ArrayList<>();

    //아래는 D-day 계산
    private int tYear;           //오늘 연월일 변수
    private int tMonth;
    private int tDay;

    private int dYear = 1;        //디데이 연월일 변수
    private int dMonth = 1;
    private int dDay = 1;

    private long d;
    private long t;
    private long r;

    private int resultNumber = 0;
    static final int DATE_DIALOG_ID = 0;
    //======================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success); // activity_success 를 보여줘라
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Realm.init(this);
        Realm.setDefaultConfiguration(getRealmConfig());

        final Intent intent_info = getIntent(); //사용자 정보 받기 !
        BtnOnClickListener onClickListener = new BtnOnClickListener();

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        ib1 = (ImageButton) findViewById(R.id.real_time_seat_button); //실시간 버튼
        ib1.setOnClickListener(onClickListener);

        ib2 = (ImageButton) findViewById(R.id.board_button); // 게시판 버튼
        ib2.setOnClickListener(onClickListener);

        ib3 = (ImageButton) findViewById(R.id.emer_button); // 긴급상황 버튼
        ib3.setOnClickListener(onClickListener);

        ib4 = (ImageButton) findViewById(R.id.setting_button); // 환경설정 버튼
        ib4.setOnClickListener(onClickListener);

        b1 = (Button) findViewById(R.id.calendar);
        b1.setText(getIntent().getStringExtra("user_nickname") + "님의 일정"); //김민수 님의 일정
        b1.setOnClickListener(onClickListener);

        dday = (FloatingActionButton) findViewById(R.id.ddaybutton);

        rlaalstn = (TextView) findViewById(R.id.rlaalstn);
        rlaalstn2 = (TextView) findViewById(R.id.rlaalstn2);
        rlaalstn3 = (TextView) findViewById(R.id.rlaalstn3);

        year = (TextView) findViewById(R.id.year_month_day); // 현재 시간 받아오기
        year.setText(getDateRightnow());

        fromthatday = (TextView) findViewById(R.id.d_day);

        expectedDay = (TextView) findViewById(R.id.yy_mm_dd);

        text_calendar = (TextView) findViewById(R.id.text_calendar);

        imageView = (ImageView) findViewById(R.id.image_baby);

        /*realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                UserInfo user = realm.where(UserInfo.class).equalTo("user_email",getIntent().getStringExtra("user_email")).findFirst();
                user.setInitial_autho(false);
            }
        });*/

        /*realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                UserInfo user = realm.where(UserInfo.class).equalTo("user_email",getIntent().getStringExtra("user_email")).findFirst();
                user.setInitial_autho(true);
            }
        });
*/
        // DB상에 저장되어있는 예상일을 가져와서, 오늘 날짜랑 계산해서 D-day계산하는 것이 필요.

        dday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(0);//----------------
            }
        });

        Calendar calendar = Calendar.getInstance();              //현재 날짜 불러옴
        tYear = calendar.get(Calendar.YEAR);
        tMonth = calendar.get(Calendar.MONTH)+1;
        tDay = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar dCalendar = Calendar.getInstance();
        dCalendar.set(dYear, dMonth, dDay);

        t = calendar.getTimeInMillis();                 //오늘 날짜를 밀리타임으로 바꿈
        d = dCalendar.getTimeInMillis();              //디데이날짜를 밀리타임으로 바꿈
        r = (d - t) / (24 * 60 * 60 * 1000);                 //디데이 날짜에서 오늘 날짜를 뺀 값을 '일'단위로 바꿈

        resultNumber = (int) r + 1; // D-day 계산임


       /*realm = Realm.getDefaultInstance(); // realm 객체의 테이블 비우기.
        realm.executeTransaction(new Realm.Transaction(){

            @Override
            public void execute(Realm realm) {
                RealmResults<UserInfo> us = realm.where(UserInfo.class).findAll();
                us.deleteAllFromRealm();
            }
        } );*/


     //check
        userInfoArrayList = (ArrayList<UserInfo>) getUserInfo().clone();

        if (userInfoArrayList.size() == 0) { // 무조건 DB상에 객체가 하나있게끔 보장함.
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    UserInfo userInfo = realm.createObject(UserInfo.class);
                    userInfo.setUser_email(getIntent().getStringExtra("user_email")); // 유저이메일
                    userInfo.setNick_name(getIntent().getStringExtra("user_nickname")); // 유저이름
                    userInfo.setExpected_date("2018년 0월 0일"); // 예상일
                    userInfo.setInitial_autho(false); // 임산부 인증을 받았는지
                    userInfo.setResultDay(0); // D-day 0으로 설정.
                }
            });
        }

        arrayList = (ArrayList<DataCalendar>) getEventlist().clone(); // arrayList는 db에 저장된 디비를 복제한다. <DataCalendar>
        userInfoArrayList = (ArrayList<UserInfo>) getUserInfo().clone(); // <UserInfo> 복제.


        for (int i = 0; i < arrayList.size(); i++) { //오늘의 일정 띄우는 것.
            if (getDateRightnow().equals(formattedTimeString(arrayList.get(i).getTime()))) {
                text_calendar.setText(arrayList.get(i).getNote());
            } //오늘 일정 띄우기
        }

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() { //가져와서
            @Override
            public void execute(Realm realm) {
                UserInfo info = realm.where(UserInfo.class).equalTo("user_email", getIntent().getStringExtra("user_email")).findFirst(); //자신의 이메일을 찾아서 수정한다.
                expectedDay.setText(info.getExpected_date()); // 예상일 수정
            }
        });

        fromthatday.setText(Integer.toString(getD_daymethod()));
        progressBar.setProgress(280-getD_daymethod());

        Set_image_View_byPixel();
        /*rlaalstn.setText(Integer.toString(userInfoArrayList.size()));
        rlaalstn2.setText(userInfoArrayList.get(0).getExpected_date());
        rlaalstn3.setText(Integer.toString(userInfoArrayList.get(0).getResultDay()));*/
    }

    public int getD_daymethod(){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                UserInfo userInfo = realm.where(UserInfo.class).equalTo("user_email",getIntent().getStringExtra("user_email")).findFirst();
                String selected_day = userInfo.getExpected_date(); //예상일을 가지고옴
                int idx = selected_day.indexOf("년");
                int idx1 = selected_day.indexOf("월");
                int idx2 = selected_day.indexOf("일");

                int year = Integer.parseInt(selected_day.substring(0,idx)); //여기에 년이 들어간다.
                int month= Integer.parseInt(selected_day.substring(idx+2,idx1)); // 월
                int day = Integer.parseInt(selected_day.substring(idx1+2,idx2)); // 데이

                Calendar calendar = Calendar.getInstance();              //현재 날짜 불러옴
                tYear = calendar.get(Calendar.YEAR);
                tMonth  = calendar.get(Calendar.MONTH)+1;
                tDay = calendar.get(Calendar.DAY_OF_MONTH);
                Log.e(TAG,"현재 시간"+tYear+tMonth+tDay); // 2018 04 08

                Calendar dCalendar = Calendar.getInstance();
                dCalendar.set(year,month,day);
                Log.e(TAG,"DB 시간"+year+month+day);

                t = calendar.getTimeInMillis();                 //오늘 날짜를 밀리타임으로 바꿈
                d = dCalendar.getTimeInMillis();              //디데이날짜를 밀리타임으로 바꿈
                r = (d - t) / (24 * 60 * 60 * 1000);                 //디데이 날짜에서 오늘 날짜를 뺀 값을 '일'단위로 바꿈

                resultNumber = (int) r + 1 - 31; // 이게 최신화 된 d-day.
                userInfo.setResultDay(resultNumber); // 최신화된 d-day를 넣어준다.
            }
        });
        return resultNumber;
    }

    public void getNewobject() { //날짜 수정 메소드
        for (int i = 0; i < userInfoArrayList.size(); i++) {
            if (userInfoArrayList.get(i).getUser_email().equals(getIntent().getStringExtra("user_email"))) {
                //만약 이메일이 등록되어있으면.. ==> 날짜 수정만 !
                //Log.e(TAG, "이게 내 계정이 db에 있을 때");
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (int i = 0; i < userInfoArrayList.size(); i++) {
                            UserInfo userInfo = realm.where(UserInfo.class).equalTo("user_email", getIntent().getStringExtra("user_email")).findFirst();
                            String expect = Integer.toString(dYear) + "년 " + Integer.toString(dMonth + 1) + "월 " + Integer.toString(dDay) + "일";
                            userInfo.setExpected_date(expect);
                            userInfo.setResultDay(resultNumber);
                        }
                    }
                });
            }
        }
    }

    public void AfterchoiceDate() { // 사용자가 날짜를 선택하면, 셋팅
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() { //가져와서
            @Override
            public void execute(Realm realm) {
                UserInfo info = realm.where(UserInfo.class).equalTo("user_email", getIntent().getStringExtra("user_email")).findFirst(); //자신의 이메일을 찾아서 수정한다.
                expectedDay.setText(info.getExpected_date());
                fromthatday.setText(Integer.toString(info.getResultDay()));
                progressBar.setProgress(280 - info.getResultDay());
            }
        });
    }

    public ArrayList<UserInfo> getUserInfo() { // DB에서 UserInfo클라스를 Arraylist로 반환한다.
        ArrayList<UserInfo> list = new ArrayList<>();
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<UserInfo> results = realm.where(UserInfo.class).findAll();
            list.addAll(realm.copyFromRealm(results));
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return list; // list는 디비정보 다있음
    }

    public ArrayList<DataCalendar> getEventlist() { // DB에 저장된 객체들 가져오기 by arraylist
        ArrayList<DataCalendar> list = new ArrayList<>();
        try {
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

    private DatePickerDialog.OnDateSetListener dDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {// 사용자가 ㄴ날짜를 선택하면 by datepicker
            // TODO Auto-generated method stub
            dYear = year;
            dMonth = monthOfYear;
            dDay = dayOfMonth;
            final Calendar dCalendar = Calendar.getInstance();
            dCalendar.set(dYear, dMonth, dDay); // 목표로 한 날짜;

            d = dCalendar.getTimeInMillis(); // 미리 설정해둔 ?
            r = (d - t) / (24 * 60 * 60 * 1000);

            resultNumber = (int) r;

            final String yy_mm_dd = Integer.toString(dYear) + "년 " + Integer.toString(dMonth + 1) + "월 " + Integer.toString(dDay) + "일";

            getNewobject();
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    UserInfo info = realm.where(UserInfo.class).equalTo("user_email", getIntent().getStringExtra("user_email")).findFirst(); //자신의 이메일을 찾아서 수정한다.
                    expectedDay.setText(info.getExpected_date());
                    fromthatday.setText(Integer.toString(info.getResultDay()));
                    progressBar.setProgress(280 - info.getResultDay());
                }
            });
            Set_image_View_byPixel();
            AfterchoiceDate();
        }
    };

    public void Set_image_View_byPixel(){
        RelativeLayout.LayoutParams params = null;
        params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        params.leftMargin = Math.toIntExact(Math.round(3.75*(280-getD_daymethod())));
        params.topMargin = 225; //고정. ()
        imageView.setLayoutParams(params);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_DIALOG_ID) {
            return new DatePickerDialog(this, dDateSetListener, tYear, tMonth, tDay);
        }
        return null;
    }

    public String formattedTimeString(long timeInMillis) {   //db에 저장되있는 long->String
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format.format(timeInMillis);
        return formatted;
    }


    public String getDateRightnow() { // 오늘날짜 가져오기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);
        return getTime;
    }


    private RealmConfiguration getRealmConfig() {
        return new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
    }

    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) { // you can choose four button which you can see
            switch (view.getId()) {
                case R.id.real_time_seat_button: //실시간 누르면 -> 인증 유무 확인.
                    realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            UserInfo user = realm.where(UserInfo.class).equalTo("user_email",getIntent().getStringExtra("user_email")).findFirst();
                            if(user.getInitial_autho() == false){ // User가 아직 임산부 인증을 안했을 경우.
                                new AlertDialog.Builder(SuccessActivity.this)
                                        .setTitle("미인증 회원")
                                        .setMessage("임산부인증 회원이 아닙니다.\n설정 >> 임산부인증 후 이용가능합니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).show();
                            }
                            else{ //인증이 되어있으면 Choiceseat로 넘어간다. by intent
                                Intent intent = new Intent (SuccessActivity.this,Choicetheseat.class);
                                startActivity(intent);
                            }
                        }
                    });
                    break;

                case R.id.board_button: // 게시판 ==> 탁
                    break;

                case R.id.emer_button: // 이건 그냥 119;;
                    Intent my_intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:119")); // emergency situation.
                    startActivity(my_intent); //start 119 dialog.
                    break;

                case R.id.setting_button: // 설정 버튼
                    Intent new_info = new Intent(SuccessActivity.this, setting.class);
                    new_info.putExtra("new_nickname", getIntent().getStringExtra("user_nickname"));
                    new_info.putExtra("new_email", getIntent().getStringExtra("user_email"));
                    new_info.putExtra("new_img", getIntent().getStringExtra("user_img"));
                    startActivity(new_info);
                    //finish(); <<- if you set the finish fucntion, you can't reach a previous xml.
                    break;

                case R.id.calendar: // 중간에 달력버튼
                    Intent intent = new Intent(SuccessActivity.this, com.example.minst.team_project.MyCal.class);
                    intent.putExtra("email", getIntent().getStringExtra("user_email"));
                    startActivity(intent);
                    break;
                default:
                    break;

            }
        }
    }
}