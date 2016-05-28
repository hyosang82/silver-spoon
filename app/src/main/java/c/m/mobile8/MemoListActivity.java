package c.m.mobile8;


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MemoListActivity extends AppCompatActivity {
    //Back Key
    private static final int BACKKEY_TIMEOUT = 2;
    private static final int MILLIS_IN_SEC = 1000;
    private boolean mIsBackKeyPressed = false;
    private long mCurrTimeInMillis = 0;
    private static final int MSG_TIMER_EXPIRED = 1;
    private CoordinatorLayout mCoordinatorLayout;

    ListView listViewMemoList;
    MemoListViewAdapter memoListViewAdapter;
    List<String> memoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);

        memoItem = new ArrayList<>();
        memoItem.add("memo1");
        memoItem.add("memo2");
        memoItem.add("memo3");
        memoItem.add("memo4");

        listViewMemoList = (ListView)findViewById(R.id.listViewMemoList);
        memoListViewAdapter = new MemoListViewAdapter(getApplicationContext());
        listViewMemoList.setAdapter(memoListViewAdapter);

        listViewMemoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //
            }
        });

    }

    private class MemoListViewAdapter extends BaseAdapter {
        private Context context;

        public MemoListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {return memoItem.size();}
        @Override
        public Object getItem(int position) {return position;}
        @Override
        public long getItemId(int position) {return position;}

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.memo_list_item, null);
                holder.textViewMemoTitle = (TextView) convertView.findViewById(R.id.textViewMemoTitle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            String str = memoItem.get(position);
            holder.textViewMemoTitle.setText(str);
            holder.textViewMemoTitle.setTextColor(Color.BLACK);

            return convertView;
        }
    }
    private class ViewHolder {
        public TextView textViewMemoTitle;
    }


    //implements Back key pressed
    @Override
    public void onBackPressed() {
        exitApp();
    }

    public void exitApp() {
        if (mIsBackKeyPressed == false) {
            mIsBackKeyPressed = true;

            mCurrTimeInMillis = Calendar.getInstance().getTimeInMillis();
            Snackbar snackbar = Snackbar.make(mCoordinatorLayout, "\"Back key\"를 한번 더 누르시면 종료됩니다.", Snackbar.LENGTH_SHORT);
            snackbar.show();
//            showWarningToast("\"Back key\"를 한번 더 누르시면 종료됩니다.");
            startTimer();
        } else {
            mIsBackKeyPressed = false;

            if (Calendar.getInstance().getTimeInMillis() <= (mCurrTimeInMillis + (BACKKEY_TIMEOUT * MILLIS_IN_SEC))) {
                // super.onBackPressed(); // or call finish..
                moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }
    private void startTimer() {
        mTimerHandler.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED,
                BACKKEY_TIMEOUT * MILLIS_IN_SEC);
    }

    private Handler mTimerHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_TIMER_EXPIRED: {
                    mIsBackKeyPressed = false;
                }
                break;
            }
        }
    };
}
