package com.example.test_physical;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {
    private String DB_PATH;							// database 경로
    private static String DB_NAME = "mymemo.db"; 	// database 이름
    private SQLiteDatabase mDatabase;
    private Context mContext;

    // DBHelper 생성자
    public DBHelper(Context context) {
        mContext = context;
        DB_PATH = "/data/data/"+ context.getPackageName() + "/databases/"; // DB경로
    }


    /**
     * Memo Database Open/Close
     */
    public void openDatabase() {
        String dbPath = DB_PATH + DB_NAME;
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null,
                SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    }

    public void closeDatabase() {
        if (mDatabase != null)
            mDatabase.close();
    }


    /**
     * Database Query: SELECT
     */

    public ArrayList<MemoRecord> getMemoList()
    {
        openDatabase(); // open database

        String sqlStr = String
                .format(Locale.US, "SELECT * FROM memo;");  // SQL 명령문

        Cursor c = mDatabase.rawQuery(sqlStr, null);
        ArrayList<MemoRecord> list = new ArrayList<MemoRecord>();

        while (c.moveToNext()) {

            Integer id = c.getInt(0);		// id
            String title = c.getString(1);	// 제목
            String text = c.getString(2);	// 텍스트

            list.add(new MemoRecord(id, title, text));
        }

        if(c.getCount() <= 0) return null;

        c.close(); 			// cursor close
        closeDatabase(); 	// database close

        return list;
    }


    // 검색어를 사용하여 쿼리
    public ArrayList<MemoRecord> getMemoList(String searchWord)
    {

        openDatabase(); // open database

        String sqlStr = String.format(Locale.US, "SELECT * FROM memo WHERE title LIKE '%%%s%%'", searchWord);

        Cursor c = mDatabase.rawQuery(sqlStr, null);
        ArrayList<MemoRecord> list = new ArrayList<MemoRecord>();

        while (c.moveToNext()) {

            Integer id = c.getInt(0);		// id
            String title = c.getString(1);	// 제목
            String text = c.getString(2);	// 텍스트

            list.add(new MemoRecord(id, title, text));
        }

        if(c.getCount() <= 0) return null;

        c.close(); 			// cursor close
        closeDatabase(); 	// database close

        return list;
    }


    // ID로 검색
    public MemoRecord getMemoRcord(int db_id)
    {
        openDatabase(); // open database

        String sqlStr = String
                .format(Locale.US, "SELECT * FROM memo WHERE id = %d;", db_id);
        Cursor c = mDatabase.rawQuery(sqlStr, null);

        MemoRecord list = null;

        while (c.moveToNext()) {

            Integer id = c.getInt(0);
            String title = c.getString(1);
            String text = c.getString(2);

            list = new MemoRecord(id, title, text);
        }

        if(c.getCount() <= 0) return null;

        c.close(); 			// cursor close
        closeDatabase(); 	// database close

        return list;
    }


    /**
     * Database INSERT, UPDATE, DELETE
     */

    // 메모 레코드 삽입
    public void insertMemo(String title, String text)
    {
        openDatabase();

        String sqlStr = String
                .format(Locale.US, "INSERT INTO memo VALUES (null, \"%s\", \"%s\", %d);",
                        title, text, System.currentTimeMillis()); // 참고==> \"%s\",  (문자열안에 공백 또는 쌍따옴표 등이 들어있을 수 있기 때문에)
        mDatabase.execSQL(sqlStr);

        closeDatabase(); //
    }


    // DB의 레코드 업데이트
    public void updateMemo(Integer db_id, String title, String text)
    {
        openDatabase();

        String sqlStr = String
                .format(Locale.US, "UPDATE memo SET title = \"%s\", text = \"%s\", timestamp = %d WHERE id = %d;",
                        title, text, System.currentTimeMillis(), db_id);

        mDatabase.execSQL(sqlStr);

        closeDatabase(); //
    }


    // ID로 레코드를 삭제
    public void deleteMemo(int db_id)
    {
        openDatabase(); //

        String sqlStr = String.format(Locale.US,"DELETE FROM memo WHERE id = %d;", db_id); //
        mDatabase.execSQL(sqlStr);

        closeDatabase(); //
    }


    // DB 전체레코드를 삭제
    public void deleteMemoAll()
    {
        openDatabase(); //

        String sqlStr = String.format(Locale.US,"DELETE FROM memo;"); //
        mDatabase.execSQL(sqlStr);

        closeDatabase(); //
    }


    /**
     * 안드로이드 시스템에 DB가 존재하는지 체크
     */
    public Boolean checkDatabase() {
        String path = DB_PATH + DB_NAME; // 안드로이드 DB 경로
        File dbFile = new File(path);
        return dbFile.exists();
    }


    // Asset 폴더에 있는 mymemo.db 파일을 안드로이드 시스템에 복사
    public void copyDatabaseFromAsset() {
        try {
            InputStream myInputStream = mContext.getAssets().open(DB_NAME); // Asset에 있는 DB파일을 open

            String outFilePath = DB_PATH + DB_NAME;

            File file = new File(DB_PATH);  // database directory in android system
            if(file.exists() == false) // 디렉토리가 존재하지 않으면 디렉토리 새로 생성
                file.mkdir(); // make directory

            OutputStream myOutputStream = new FileOutputStream(outFilePath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInputStream.read(buffer)) > 0) {
                myOutputStream.write(buffer, 0, length);
            }

            myOutputStream.flush();
            myOutputStream.close();
            myInputStream.close();
        }
        catch (FileNotFoundException e) {
            throw new Error("File Not Found " + e.toString());
        }
        catch (EOFException e) {
            throw new Error("EOF Exception " + e.toString());
        }
        catch (IOException e) {
            throw new Error("Error copying database");
        }
    }

}