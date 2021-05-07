package com.example.test_physical;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MemoView extends AppCompatActivity {
    private static int EDIT_RESULT = 2001; // 편집반환
    private static int INSERT_RESULT = 2002; // 삽입반환

    private DBHelper mDBHelper;
    private MemoRecord mMemoRecord = null; // 메모 목록

    private int mDB_ID; // 이전 액티비티에서 넘어온 DB_ID
    private boolean mIsEditRequest; // 편집요청
    private boolean mIsInsertRequest; // 삽입요청여부

    private EditText mTxtTitle;
    private EditText mTxtText;
    private Button mBtnOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_view);
        mTxtTitle = (EditText) findViewById(R.id.txtTitle);
        mTxtText = (EditText) findViewById(R.id.txtText);
        mBtnOk = (Button) findViewById(R.id.btnOk);

        Intent intent = getIntent();
        mDB_ID = intent.getIntExtra("DB_ID", 0);
        mIsEditRequest = intent.getBooleanExtra("EDIT_REQUEST", false); // 편집요청
        mIsInsertRequest = intent.getBooleanExtra("INSERT_REQUEST", false); // 삽입요청

        mDBHelper = new DBHelper(this);
        mMemoRecord = mDBHelper.getMemoRcord(mDB_ID);
        displayContents();
    }

    private void displayContents() {

        if (mIsEditRequest || mIsInsertRequest)
            mBtnOk.setVisibility(View.VISIBLE); // 보임 상태로 변경

        if (mIsEditRequest) {
            mTxtTitle.setText(mMemoRecord.title); // 제목
            mTxtText.setText(mMemoRecord.text); // 메모내용
        }
    }

    public void onClickOK(View v) {

        //if (mMemoRecord == null) return;

        if (mIsEditRequest) {
            mDBHelper.updateMemo(mDB_ID, mTxtTitle.getText().toString(), mTxtText.getText().toString()); // DB 변경

            Intent intent = new Intent();
            setResult(EDIT_RESULT, intent); // 편집반환
        } else if (mIsInsertRequest) {
            mDBHelper.insertMemo(mTxtTitle.getText().toString(), mTxtText.getText().toString()); // DB에 삽입

            Intent intent = new Intent();
            setResult(INSERT_RESULT, intent); // 삽입반환
        }

        finish(); // 액티비티 닫기
    }
}
