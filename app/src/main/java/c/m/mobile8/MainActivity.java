package c.m.mobile8;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
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

import c.m.mobile8.adapter.MemoListViewAdapter;
import c.m.mobile8.fragments.ConfigFragment;
import c.m.mobile8.fragments.MemoCalendarFragment;
import c.m.mobile8.fragments.MemoListFragment;
import c.m.mobile8.models.Memo;
import c.m.mobile8.models.MemoContent;
import c.m.mobile8.models.enums.ContentType;
import c.m.mobile8.utils.DBManager;
import c.m.mobile8.utils.SharedPreferenceManager;
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
    private ActionBar actionBar;
    private FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        initActionBar();
        refreshData();
        //init memo list fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.activity_main_fragments, mMemoListFragment).commit();
        //getSupportActionBar().setTitle("메모 목록");
        actionBar = getSupportActionBar();
        actionBar.setTitle("메모 목록");
        //dbtestCode();

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        if (mFab != null) {
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //new memo
                    enterDetailView(-1);
                }
            });
        }
        mFab.setBackgroundTintList(ColorStateList.valueOf(ThemeUtil.getSystemColor(this, ThemeUtil.getTheme(this))));
        mFab.setImageResource(R.drawable.icon_fab_plus);
        setActionBarColor(ThemeUtil.getTheme(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadData();
    }
    public void reloadData() {
        refreshData();
        switch(mCurrentState) {
            case STATE_LIST:
                mMemoListFragment.reloadData();
                break;
            case STATE_CALENDAR:
                mMemoCalendarFragment.reloadData();
                break;
        }
    }

    public void refreshData() {
        int orderType = SharedPreferenceManager.getPreferencesOrder(this);
        mMemoList = DBManager.getInstance(this).getMemoList(orderType);
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
        if(actionBar != null) actionBar.setTitle("날짜별 메모");
        mMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.icon_list));
        mCurrentState = STATE_CALENDAR;

    }
    public void switchFragmentCalendarToList() {
        //TODO : switch fragment to list
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.activity_main_fragments, mMemoListFragment).commit();
        if(actionBar != null) actionBar.setTitle("메모 목록");
        mMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.icon_calendar));
        mCurrentState = STATE_LIST;

    }
    public void switchFragmentDefaultToConfig() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.activity_main_fragments, mConfigFragment).commit();
        if(actionBar != null) actionBar.setTitle("설정");
        mMenu.getItem(1).setIcon(getResources().getDrawable(R.drawable.icon_x));
        mMenu.getItem(0).setVisible(false);
        mIsConfigFragment = true;
        mFab.hide();
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
        mMenu.getItem(1).setIcon(getResources().getDrawable(R.drawable.icon_setting));
        mMenu.getItem(0).setVisible(true);
        mIsConfigFragment = false;
        mFab.show();
    }

    //implements Back key pressed
    @Override
    public void onBackPressed() {
        if(mIsConfigFragment) {
            switchFragmentConfigToDefault();
        } else if(mCurrentState == STATE_CALENDAR) {
            switchFragmentCalendarToList();
        } else if(mMemoListFragment.getSearchtMode()) {
            mMemoListFragment.setSearchMode(false);
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
        mFab.setBackgroundTintList(ColorStateList.valueOf(ThemeUtil.getSystemColor(this, ThemeUtil.getTheme(this))));
    }

    public void setOrderType(int orderType) {
        refreshData();
    }

}
