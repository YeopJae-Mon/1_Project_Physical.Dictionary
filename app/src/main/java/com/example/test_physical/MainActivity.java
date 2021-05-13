package com.example.test_physical;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static int INSERT_REQUEST = 1002; // 삽입요청
    private static int EDIT_RESULT = 2001; // 편집반환
    private static int INSERT_RESULT = 2002; // 삽입반환

    private DBHelper mDBHelper;
    private ListView mListView; // 리스트뷰
    private SearchView mSearchView; // 검색뷰

    private ArrayAdapter<MemoRecord> mListAdapter;
    private ArrayList<MemoRecord> mMemoList = null; // 메모 목록

    public SearchView.OnQueryTextListener queryTextListner = (new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String text) {

            if (text != null && text.length() > 0) {
                mMemoList = mDBHelper.getMemoList(text);
                if (mMemoList != null)
                    displayListView(MainActivity.this, mMemoList);
                else{ mMemoList = mDBHelper.getMemoList();
                    displayListView(MainActivity.this, mMemoList);
                    Toast.makeText(MainActivity.this, "검색된 데이타가 없습니다.", Toast.LENGTH_LONG).show();}
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {

            if (newText != null && newText.length() <= 0) {

                // 약간의 딜레이를(0.3초 정도면 충분) 주고 키보드를 없애야 한다. ㅋㅋ
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        mSearchView.clearFocus(); // 검색뷰 키보드 사라지기
                        mSearchView.setIconified(true); // 아이콘화 시키기 (검색뷰 닫기 효과);
                    }
                }, 300); // 0.3초 delay

                display();
            }

            return false;
        }
    });
    /**
     * 리스트뷰 클릭 이벤트
     */
    private AdapterView.OnItemClickListener mListClickListener = (new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (mMemoList == null) return;

            Intent intent = new Intent(MainActivity.this, MemoIn.class);
            intent.putExtra("DB_ID", mMemoList.get(position).id); // 데이터베이스 ID 넘김
            startActivity(intent); // 인텐트 호출
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.lvMemo); // 리스트 뷰
        mListView.setOnItemClickListener(mListClickListener); // 리스트 클릭 리스너

        mDBHelper = new DBHelper(this);

        // 시스템에 DB가 없을 경우
        if (mDBHelper.checkDatabase() == false) {
            mDBHelper.copyDatabaseFromAsset(); // Asset의 DB를 시스템에 복사
        }
        display();
    }

    private void display() {

        mMemoList = mDBHelper.getMemoList(); // 전체목록검색
        if (mMemoList != null) {
            displayListView(this, mMemoList);
            // mListAdapter.notifyDataSetChanged(); // 리스트 변경 통보
        }
    }

    private void displayListView(final Activity context, ArrayList<MemoRecord> list) {

        class ViewHolder {
            TextView txtTitle;
            TextView txtMemo;
        }

        mListAdapter = new ArrayAdapter<MemoRecord>(this, R.layout.memo_custom, list) {

            ViewHolder holder = null; // 뷰홀더 객체

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                LayoutInflater inflater = context.getLayoutInflater();

                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.memo_custom, parent, false);

                    holder.txtTitle = convertView.findViewById(R.id.txtTitle);
                    holder.txtMemo = convertView.findViewById(R.id.txtText);
                    convertView.setTag(holder); // view 객체의 태그로 ViewHolder 지정
                } else {
                    holder = (ViewHolder) convertView.getTag(); // 태그를 가져옴
                }

                if (holder != null) {
                    holder.txtTitle.setText(mMemoList.get(position).title); // 제목
                    holder.txtMemo.setText(mMemoList.get(position).text); // 메모내용
                }
                return convertView;
            }
        };

        mListView.setAdapter(mListAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == EDIT_RESULT || resultCode == INSERT_RESULT) {
            display(); // 화면 갱신
        }
    }

    // 옵션 메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.memo_options, menu); // 액션바 추가
        mSearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        mSearchView.setOnQueryTextListener(queryTextListner); // 검색 뷰 이벤트 리스너
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.app_bar_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}