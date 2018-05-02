package com.example.minst.team_project;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    SessionCallback callback; //위의 콜백함수를 만들어주셔야 사용가능합니다. 위의 함수를 만들어주세요~~
    String user_nickname, user_email; // 유저의 정보
    String user_img; // 유저 프로필 사진

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
            }
        });

        callback = new SessionCallback(); //세션콜백을 부르고
        Session.getCurrentSession().addCallback(callback); // 추가시키면 끝입니다!!
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.getInstance().requestMe(new MeResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {

                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {

                }

                @Override
                public void onNotSignedUp() {

                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    // 로그인 성공시, 로그인하는 유저의 닉네임,이메일,프로필 사진을 SuccessActivity로 넘겨줌.
                    // SuccessActivity가 사실상 메인 엑티비티

                    user_nickname = userProfile.getNickname();
                    user_email = userProfile.getEmail();
                    user_img = userProfile.getThumbnailImagePath();

                    Intent intent = new Intent(MainActivity.this, SuccessActivity.class); // 넘어가는것

                    intent.putExtra("user_nickname", user_nickname);
                    intent.putExtra("user_email", user_email);
                    intent.putExtra("user_img", user_img);

                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {

        }

    }
}
