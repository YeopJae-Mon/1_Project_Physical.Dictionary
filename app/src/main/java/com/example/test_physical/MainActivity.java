package com.example.test_physical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    DBHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView =(ListView)findViewById(R.id.listview);

        helper = new DBHelper(MainActivity.this, "test.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        String sql = "select * from mytable;";
        Cursor c = db.rawQuery(sql, null);
        String[] strs = new String[]{"txt"};
        int[] ints = new int[] {R.id.listview_txt};

        SimpleCursorAdapter adapter = null;
        adapter = new SimpleCursorAdapter(listView.getContext(), R.layout.listview, c, strs, ints,0);

        listView.setAdapter(adapter);

    }

    public class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sql = "DROP TABLE if exists mytable";

            db.execSQL(sql);
            onCreate(db);
        }
    }
}
