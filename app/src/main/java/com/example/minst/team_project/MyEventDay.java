package com.example.minst.team_project;

import android.os.Parcel;
import android.os.Parcelable;

import com.applandeo.materialcalendarview.EventDay;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by minst on 2018-03-22.
 */

class MyEventDay extends EventDay implements Parcelable {

    private String mNote;
    private Calendar mDay;
    private int Image;

    MyEventDay(Calendar day, String note, int imageResource) { //생성자 (날짜, 이미지소스, 노트내용)
        super(day, imageResource);
        mNote = note;
        mDay = day;
        Image = imageResource;
    }

    public String getNote() {
        return mNote;
    } // 내가쓴거 뽑아내기 (Memo)

    public Calendar getDate() {
        return mDay;
    } // 작성날짜 뽑아내기 (Day)

    public int getImage() {
        return Image;
    }

    private MyEventDay(Parcel in) {
        super((Calendar) in.readSerializable(), in.readInt());
        mNote = in.readString();
    }

    public static final Creator<MyEventDay> CREATOR = new Creator<MyEventDay>() {
        @Override
        public MyEventDay createFromParcel(Parcel in) {
            return new MyEventDay(in);
        }

        @Override
        public MyEventDay[] newArray(int size) {
            return new MyEventDay[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(getCalendar());
        parcel.writeString(mNote);
        parcel.writeInt(getImageResource());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
