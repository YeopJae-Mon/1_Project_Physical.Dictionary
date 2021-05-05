package com.example.test_physical;

import android.text.format.DateFormat;

import java.util.Date;

public class Util {

    /**
     * 타임스탬프를 년-월-일(요일) 시-분으로 변환
     * @param timpstamp 타임스탬프
     * @return 년-월-일(요일) 시-분 문자열
     */
    public static String convertToDateTimeStr(long timpstamp) {
        Date d = new Date(timpstamp);
        return DateFormat.format("yyyy-MM-dd (E) HH:mm", d).toString(); // 년-월-일(요일) 시-분
    }

    /**
     * 타임스탬프를 시-분으로 변환
     */
    public static String convertToimeStr(long timpstamp) {
        Date d = new Date(timpstamp);
        return DateFormat.format("HH:mm", d).toString(); // 시-분
    }
}
