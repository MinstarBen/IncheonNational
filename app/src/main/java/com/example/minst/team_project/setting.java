package com.example.minst.team_project;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by minst on 2018-03-22.
 */
public class setting extends AppCompatActivity {
    String login_propic, login_nicname, login_email;
    CircleImageView circle_view;
    private AQuery aq = new AQuery(this);
    TextView nickname;
    Button b1;
    ImageButton ib1, ib2, ib3, pregnant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting); // activity_success 를 보여줘라
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BtnOnClickListener onClickListener = new BtnOnClickListener();

        b1 = (Button) findViewById(R.id.logout_button); //실시간 버튼
        b1.setOnClickListener(onClickListener);

        ib1 = (ImageButton) findViewById(R.id.manager); //관리
        ib1.setOnClickListener(onClickListener);

        ib2 = (ImageButton) findViewById(R.id.problem); //문제
        ib2.setOnClickListener(onClickListener);

        ib3 = (ImageButton) findViewById(R.id.version_button); //버전
        ib3.setOnClickListener(onClickListener);

        pregnant = (ImageButton) findViewById(R.id.pregnant_autho);
        pregnant.setOnClickListener(onClickListener);

        login_nicname = getIntent().getStringExtra("new_nickname"); //닉네임 가져오기
        login_email = getIntent().getStringExtra("new_email"); // 이메일 가져오기
        login_propic = getIntent().getStringExtra("new_img"); //이미지 가져오기

        circle_view = (CircleImageView) findViewById(R.id.user_img);
        nickname = (TextView) findViewById(R.id.user_nick);

        nickname.setText(login_nicname);
        aq.id(circle_view).image(login_propic);
    }

    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) { // you can choose four button which you can see

            switch (view.getId()) {
                case R.id.logout_button: //logout button : goto Mainactivity
                   Intent i = new Intent(setting.this,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Intent flag로 종료한다.
                    startActivity(i);
                    break;
                case R.id.manager:
                    Toast.makeText(setting.this, "버튼이 눌렸습니다 manager", Toast.LENGTH_LONG).show();
                    break;
                case R.id.problem:
                    Toast.makeText(setting.this, "문제 버튼이 눌렸습니다", Toast.LENGTH_LONG).show();
                    break;
                case R.id.version_button:
                    Toast.makeText(setting.this, "version 1.0.0", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pregnant_autho:
                    Intent intent1 = new Intent(setting.this,AuthoClass.class);
                    intent1.putExtra("user_email",getIntent().getStringExtra("new_email"));
                    startActivity(intent1);
                    break;


            }
        }
    }


}