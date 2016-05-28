package c.m.mobile8;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import c.m.mobile8.adapter.MDAdapter;
import c.m.mobile8.models.Memo;
import c.m.mobile8.models.MemoContent;
import c.m.mobile8.models.enums.ContentType;
import c.m.mobile8.utils.DBManager;
import c.m.mobile8.utils.ImageDecoder;
import c.m.mobile8.utils.ThemeUtil;

public class MemoDetailActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_PHOTO = 0x01;
    public static final String EXTRA_MEMO_ID = "extra_memo_id";
    //Activity
    private ListView memo_detail_listview;
    private FloatingActionButton mFab;
    //Adapter
    private MDAdapter mMDAdapter;
    private Memo mMemo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);
        init();
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void init() {
        initExtra();
        initLayout();
        initListAdapter();
        initActionBar();
    }
    public void initExtra() {
        int memoId = getIntent().getIntExtra(EXTRA_MEMO_ID, -1);
        if(memoId != -1) {
            mMemo = DBManager.getInstance(this).getMemoById(memoId);
        } else {
            mMemo = new Memo();
            mMemo.setId(-1);
        }
    }
    public void initActionBar() {
        setActionBarColor(ThemeUtil.getTheme(this));
    }
    public void initLayout() {
        memo_detail_listview = (ListView)findViewById(R.id.memo_detail_listview);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mFab.setBackgroundTintList(ColorStateList.valueOf(ThemeUtil.getSystemColor(this, ThemeUtil.getTheme(this))));
        mFab.setImageResource(R.drawable.icon_fab_plus);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사진 항목 추가
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO);
            }
        });

    }
    public void initListAdapter() {
        ImageDecoder.getInstance().setContext(this);
        mMDAdapter = new MDAdapter(this, mMemo);
        memo_detail_listview.setAdapter(mMDAdapter);
        mMDAdapter.notifyDataSetChanged();
    }
    public void setActionBarColor(ThemeUtil.MIDAS_THEME midasTheme) {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ThemeUtil.getMainColor(this, midasTheme)));
        getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(ThemeUtil.getMainColor(this, midasTheme)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ThemeUtil.getMainColor(this, midasTheme));
            getWindow().setStatusBarColor(ThemeUtil.getSystemColor(this, midasTheme));
        }
        mFab.setBackgroundTintList(ColorStateList.valueOf(ThemeUtil.getSystemColor(this, ThemeUtil.getTheme(this))));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_CODE_PICK_PHOTO) {
                //메모에 사진 추가
                if(data != null) {
                    String uri =  data.getData().toString();
                    MemoContent item = new MemoContent(-1, -1, uri, ContentType.CONTENT_TYPE_IMAGE);
                    MemoContent itemDefault = new MemoContent(-1, -1, "", ContentType.CONTENT_TYPE_TEXT);
                    mMemo.addMemoContent(item);
                    mMemo.addMemoContent(itemDefault);
                    mMDAdapter.setMemo(mMemo);
                    mMDAdapter.notifyDataSetChanged();
                }
            }
        }
    }

}
