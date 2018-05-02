package com.example.minst.team_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

import io.realm.CertificationRealmProxyInterface;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by minst on 2018-04-06.
 */

public class AuthoClass extends Activity implements View.OnClickListener{
    EditText getcode ;
    Realm realm;
    Certification certification;
    ArrayList<Certification> arrayList = new ArrayList<>();
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.authoclass);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Realm.init(this); // 객체 초기화

        /*realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Certification certification = realm.where(Certification.class).equalTo("certificode","ABCABC").findFirst(); //객체생성
                certification.setApprovement(false);
            }
        });*/

        t1 = (TextView) findViewById(R.id.certi);
        /*realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Certification> cer = realm.where(Certification.class).findAll();
                cer.deleteAllFromRealm();

            }
        });*/
        arrayList = (ArrayList<Certification>) getEventlist1().clone();

        if(arrayList.size() == 0) {
            realm = Realm.getDefaultInstance(); //certification 객체 생성 (코드, ABCABC, false)
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Certification certification = realm.createObject(Certification.class); //객체생성
                    certification.setCertificode("ABCABC");
                    certification.setApprovement(false);
                }
            });
        }
        arrayList = (ArrayList<Certification>) getEventlist1().clone();

      /*  t1.setText(Integer.toString(arrayList.size()));*/

        findViewById(R.id.confirmButton).setOnClickListener(this);
        getcode = (EditText) findViewById(R.id.getcode);
    }

    public ArrayList<Certification> getEventlist1() { // DB에 저장된 객체들 가져오기 by arraylist
        ArrayList<Certification> list = new ArrayList<>();
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<Certification> results = realm.where(Certification.class).findAll();
            list.addAll(realm.copyFromRealm(results));
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return list; // list는 디비정보 다있음
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirmButton: //사용자가 버튼을 누르면.
                realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        //사용자가 입력한 코드가 db에 있으면 관련된 사항이 certification 객체에 저장됨
                        certification = realm.where(Certification.class).equalTo("certificode",getcode.getText().toString()).findFirst();
                    }
                });

                if (certification == null){
                    new AlertDialog.Builder(AuthoClass.this)
                            .setMessage("사용할 수 없는 인증번호힙니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish(); //확인 버튼을 누르면 종료됨
                                }
                            }).show();
                }

               else if(certification.getCertificode().equals(getcode.getText().toString()) && certification.getApprovement() == false) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Certification certification = realm.where(Certification.class).equalTo("certificode",getcode.getText().toString()).findFirst();
                            certification.setApprovement(true); // 당신이입력한 코드는 사용중입니다.
                        }
                    });

                    Realm realm1 = Realm.getDefaultInstance();
                    realm1.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            UserInfo userInfo = realm.where(UserInfo.class).equalTo("user_email",getIntent().getStringExtra("user_email")).findFirst();
                            userInfo.setInitial_autho(true); //당신은 임산부 인증이 완료되었습니다.
                        }
                    });

                    new AlertDialog.Builder(AuthoClass.this)
                            .setMessage("인증등록이 완료되었습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish(); //확인 버튼을 누르면 종료됨
                                }
                            }).show();
                }

                else if(!certification.getCertificode().equals(getcode.getText().toString())){
                    new AlertDialog.Builder(AuthoClass.this)
                            .setMessage("사용할 수 없는 인증번호힙니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish(); //확인 버튼을 누르면 종료됨
                                }
                            }).show();
                }
                else{
                    new AlertDialog.Builder(AuthoClass.this)
                            .setMessage("이미 사용중인 인증번호입니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish(); //확인 버튼을 누르면 종료됨
                                }
                            }).show();
                }
                break;
        }

    }
}
