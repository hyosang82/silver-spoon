package c.m.mobile8;


import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.Calendar;

public class MemoListActivity extends AppCompatActivity {
    //Back Key
    private static final int BACKKEY_TIMEOUT = 2;
    private static final int MILLIS_IN_SEC = 1000;
    private boolean mIsBackKeyPressed = false;
    private long mCurrTimeInMillis = 0;
    private static final int MSG_TIMER_EXPIRED = 1;
    private CoordinatorLayout mCoordinatorLayout;

    RecyclerView memoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);


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
