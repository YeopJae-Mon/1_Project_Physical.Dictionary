package com.example.test_physical;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MemoIn extends AppCompatActivity {

    private int mDB_ID; // 이전 액티비티에서 넘어온 DB_ID

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mDB_ID = intent.getIntExtra("DB_ID", 0);

        if(mDB_ID == 1){setContentView(R.layout.train_1);}
        if(mDB_ID == 2){setContentView(R.layout.train_2);}
        if(mDB_ID == 3){setContentView(R.layout.train_3);}
    }

}
