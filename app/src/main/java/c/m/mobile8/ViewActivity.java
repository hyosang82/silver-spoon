package c.m.mobile8;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.InputStream;

import c.m.mobile8.adapter.MemoDetailAdapter;
import c.m.mobile8.dialog.DialogBase;
import c.m.mobile8.dialog.NewMemoItemDialog;
import c.m.mobile8.models.Memo;
import c.m.mobile8.models.MemoContent;
import c.m.mobile8.models.enums.ContentType;
import c.m.mobile8.utils.DBManager;

public class ViewActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_PHOTO = 0x01;

    public static final String EXTRA_MEMO_ID = "extra_memo_id";

    private RecyclerView mMemoView;
    private MemoDetailAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        CoordinatorLayout mainLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mMemoView = (RecyclerView) findViewById(R.id.memo_view);
        mMemoView.setHasFixedSize(true);
        mMemoView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MemoDetailAdapter();
        mMemoView.setAdapter(mAdapter);

        getSupportActionBar().setTitle("View/Edit Memo");

        findViewById(R.id.btn_cancel).setOnClickListener(mButtonListener);
        findViewById(R.id.btn_save).setOnClickListener(mButtonListener);

        findViewById(R.id.btn_memo_item_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewMemoItemDialog dlg = new NewMemoItemDialog(ViewActivity.this);
                dlg.setListener(mNewMemoItemDialogListener);
                dlg.show();
            }
        });

        int memoId = getIntent().getIntExtra(EXTRA_MEMO_ID, -1);
        if(memoId < 0) {
            //new memo
            mAdapter.addItem(new MemoContent(-1, -1, "", ContentType.CONTENT_TYPE_TEXT));
        }else {
            //load
        }
    }

    private DialogBase.IDialogListener mNewMemoItemDialogListener = new DialogBase.IDialogListener() {
        @Override
        public void onButtonClicked(Dialog me, View btnView) {
            int id = btnView.getId();
            if(id == R.id.btn_text) {
                //텍스트 항목 추가
                MemoContent item = new MemoContent(-1, -1, "", ContentType.CONTENT_TYPE_TEXT);
                int inserted = mAdapter.addItem(item);
                if(inserted >= 0) {
                    mAdapter.notifyItemInserted(inserted);
                }else {
                    Snackbar snack = Snackbar.make(btnView, "Memo item add failed", Snackbar.LENGTH_SHORT);
                    snack.show();
                }
            }else if(id == R.id.btn_photo) {
                //사진 항목 추가
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO);
            }

            me.dismiss();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_CODE_PICK_PHOTO) {
                //메모에 사진 추가
                if(data != null) {
                    String uri =  data.getData().toString();
                    MemoContent item = new MemoContent(-1, -1, uri, ContentType.CONTENT_TYPE_IMAGE);
                    int inserted = mAdapter.addItem(item);
                    if(inserted >= 0) {
                        mAdapter.notifyItemInserted(inserted);
                    }
                }
            }
        }
    }

    private void saveAndExit() {
        Memo memo = new Memo();

        for(MemoContent m : mAdapter.getAllList()) {
            memo.addMemoContent(m);
        }

        DBManager db = DBManager.getInstance(this);
        db.insertMemo(memo);

        finish();
    }

    private void updateDataRecursive(View v) {
        if(v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            int cnt = vg.getChildCount();
            for(int i=0;i<cnt;i++) {
                updateDataRecursive(vg.getChildAt(i));
            }
        }else {
            if(v instanceof EditText) {
                Object tag = v.getTag();
                if(tag instanceof MemoContent) {
                    MemoContent memo = (MemoContent) tag;
                    memo.setContent(((EditText) v).getText().toString());

                    Log.d("TEST", "UPDATED :::: " + memo.getContent());
                }
            }
        }
    }

    private View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btn_cancel) {
                //cancel
            }else if(v.getId() == R.id.btn_save) {
                //save and exit
                updateDataRecursive(mMemoView);

                if(mAdapter.isEmpty()) {
                    Snackbar snack = Snackbar.make(v, "메모가 작성되지 않았습니다.", Snackbar.LENGTH_SHORT);
                    snack.show();
                }else {
                    saveAndExit();
                }
            }
        }
    };
}
