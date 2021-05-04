package com.example.test_physical;

public class MemoRecord {
    public Integer id;	 // id
    public String title; // 제목
    public String text; // 텍스트
    public Long timeStamp; // 타임스탬프

    public MemoRecord(Integer id, String title, String text, Long timestamp) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.timeStamp = timestamp;
    }
}
