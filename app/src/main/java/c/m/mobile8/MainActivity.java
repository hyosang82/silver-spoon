package c.m.mobile8;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import c.m.mobile8.fragments.ConfigFragment;
import c.m.mobile8.fragments.MemoCalendarFragment;
import c.m.mobile8.fragments.MemoListFragment;
import c.m.mobile8.models.Memo;
import c.m.mobile8.models.MemoContent;
import c.m.mobile8.models.enums.ContentType;
import c.m.mobile8.utils.DBManager;
import c.m.mobile8.utils.ThemeUtil;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private final int STATE_LIST = 0;
    private final int STATE_CALENDAR = 1;
    private int mCurrentState = STATE_LIST;

    private boolean mIsConfigFragment = false;

    //Back Key
    private static final int BACKKEY_TIMEOUT = 2;
    private static final int MILLIS_IN_SEC = 1000;
    private boolean mIsBackKeyPressed = false;
    private long mCurrTimeInMillis = 0;
    private static final int MSG_TIMER_EXPIRED = 1;
    private CoordinatorLayout mCoordinatorLayout;
    public List<Memo> mMemoList;

    private MemoCalendarFragment mMemoCalendarFragment = new MemoCalendarFragment();
    private MemoListFragment mMemoListFragment = new MemoListFragment();
    private ConfigFragment mConfigFragment = new ConfigFragment();

    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        initActionBar();
        mMemoList = DBManager.getInstance(this).getMemoList();
        //init memo list fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.activity_main_fragments, mMemoListFragment).commit();
        //dbtestCode();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //new memo
                    Log.e(TAG, "add new memo");
                    enterDetailView(-1);
                }
            });
        }
        setActionBarColor(ThemeUtil.getTheme(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void enterDetailView(int memoId) {
        Intent i = new Intent(this, ViewActivity.class);
        i.putExtra(ViewActivity.EXTRA_MEMO_ID, memoId);
        startActivity(i);
    }


    public void initActionBar() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        mMenu = menu;
        inflater.inflate(R.menu.menu_main, mMenu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_list_calendar_changer:
                if(mCurrentState == STATE_LIST) {
                    //TODO : switch fragment to calendar
                    switchFragmentListToCalendar();
                } else {
                    //TODO : switch fragment to list
                    switchFragmentCalendarToList();
                }
                break;
            case R.id.action_list_config:
                if(mIsConfigFragment) {
                    switchFragmentConfigToDefault();
                } else {
                    switchFragmentDefaultToConfig();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void switchFragmentListToCalendar() {
        //TODO : switch fragment to calendar
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.activity_main_fragments, mMemoCalendarFragment).commit();
        mMenu.getItem(0).setIcon(getResources().getDrawable(android.R.drawable.ic_menu_agenda));
        mCurrentState = STATE_CALENDAR;

    }
    public void switchFragmentCalendarToList() {
        //TODO : switch fragment to list
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.activity_main_fragments, mMemoListFragment).commit();
        mMenu.getItem(0).setIcon(getResources().getDrawable(android.R.drawable.ic_menu_my_calendar));
        mCurrentState = STATE_LIST;

    }
    public void switchFragmentDefaultToConfig() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.activity_main_fragments, mConfigFragment).commit();
        mMenu.getItem(1).setIcon(getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
        mMenu.getItem(0).setVisible(false);
        mIsConfigFragment = true;
    }
    public void switchFragmentConfigToDefault() {
        switch(mCurrentState) {
            case STATE_LIST:
                switchFragmentCalendarToList();
                break;
            case STATE_CALENDAR:
                switchFragmentListToCalendar();
                break;
        }
        mMenu.getItem(1).setIcon(getResources().getDrawable(android.R.drawable.ic_menu_preferences));
        mMenu.getItem(0).setVisible(true);
        mIsConfigFragment = false;
    }
    public void dbtestCode() {
        Date date = new Date();
        Memo memo = new Memo(-1, date.getTime(), date.getTime());
        memo.addMemoContent(new MemoContent(0, -1, "test1", ContentType.CONTENT_TYPE_TEXT));
        memo.addMemoContent(new MemoContent(1, -1, "test2", ContentType.CONTENT_TYPE_TEXT));
        DBManager.getInstance(getApplicationContext()).insertMemo(memo);
        List<Memo> memoList = DBManager.getInstance(getApplicationContext()).getMemoList();
        Set<Integer> set = memoList.get(0).getMemoContents().keySet();
        Iterator<Integer> iter = set.iterator();
        while (iter.hasNext()) {
            MemoContent memoContent = memoList.get(0).getMemoContents().get(iter.next());
            Log.i(TAG, "sequence : " + memoContent.getSequence() + ", memo_id : " + memoContent.getMemo_id() + ", content : " + memoContent.getContent() + ", contentType : " + memoContent.getContentType().name());
        }
        DBManager.getInstance(this).deleteMemo(memo.getId());
    }

    //implements Back key pressed
    @Override
    public void onBackPressed() {
        if(mIsConfigFragment) {
            switchFragmentConfigToDefault();
        } else if(mCurrentState == STATE_CALENDAR) {
            switchFragmentCalendarToList();
        } else {
            exitApp();
        }
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

    public void setActionBarColor(ThemeUtil.MIDAS_THEME midasTheme) {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ThemeUtil.getMainColor(this, midasTheme)));
        getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(ThemeUtil.getMainColor(this, midasTheme)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ThemeUtil.getMainColor(this, midasTheme));
            getWindow().setStatusBarColor(ThemeUtil.getSystemColor(this, midasTheme));
        }
    }

}
