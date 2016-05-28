package c.m.mobile8;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import c.m.mobile8.adapter.MemoDetailAdapter;
import c.m.mobile8.adapter.MemoTextViewHolder;
import c.m.mobile8.dialog.DialogBase;
import c.m.mobile8.dialog.NewMemoItemDialog;
import c.m.mobile8.models.Memo;
import c.m.mobile8.models.MemoContent;
import c.m.mobile8.models.enums.ContentType;
import c.m.mobile8.utils.DBManager;
import c.m.mobile8.utils.ImageDecoder;
import c.m.mobile8.utils.ThemeUtil;

public class ViewActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_PHOTO = 0x01;

    private static final int MSG_FOCUS_EDITTEXT = 0x01;

    public static final String EXTRA_MEMO_ID = "extra_memo_id";

    private RecyclerView mMemoView;
    private MemoDetailAdapter mAdapter;
    private boolean mbUpdated = false;
    private int mMemoId = -1;
    private SaveTask mSaveTask = null;
    private FloatingActionButton mBtnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        //init
        ImageDecoder.getInstance().setContext(this);
        MemoTextViewHolder.bUpdated = false;

        CoordinatorLayout mainLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mMemoView = (RecyclerView) findViewById(R.id.memo_view);
        mMemoView.setHasFixedSize(true);
        mMemoView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MemoDetailAdapter();
        mAdapter.setListener(mMemoListener);
        mMemoView.setAdapter(mAdapter);

        getSupportActionBar().setTitle("View/Edit Memo");

        findViewById(R.id.btn_cancel).setOnClickListener(mButtonListener);
        findViewById(R.id.btn_save).setOnClickListener(mButtonListener);

        mBtnAdd = (FloatingActionButton) findViewById(R.id.btn_memo_item_add);

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewMemoItemDialog dlg = new NewMemoItemDialog(ViewActivity.this);
                dlg.setListener(mNewMemoItemDialogListener);
                dlg.show();
            }
        });

        mBtnAdd.setBackgroundTintList(ColorStateList.valueOf(ThemeUtil.getSystemColor(this, ThemeUtil.getTheme(this))));
        setActionBarColor(ThemeUtil.getTheme(this));

        mMemoId = getIntent().getIntExtra(EXTRA_MEMO_ID, -1);
        if(mMemoId < 0) {
            //new memo
            mAdapter.addItem(new MemoContent(-1, -1, "", ContentType.CONTENT_TYPE_TEXT));

            requestEditTextFocus();
        }else {
            //load
            Memo memo = DBManager.getInstance(this).getMemoById(mMemoId);
            mAdapter.setData(memo);
            mAdapter.notifyDataSetChanged();

            String title = getMemoTitle();
            if(title != null) {
                getSupportActionBar().setTitle(title);
            }
        }
    }

    private String getMemoTitle() {
        for(MemoContent memo : mAdapter.getAllList()) {
            if(memo.getContentType() == ContentType.CONTENT_TYPE_TEXT) {
                int idx = 10;
                String cont = memo.getContent();
                if(cont.length() > 10) {
                    idx = cont.indexOf(' ', idx);
                    if(idx > 20) idx = 10;
                    else if(idx == -1) idx = 10;

                    return cont.substring(0, idx);
                }else {
                    return cont;
                }
            }
        }

        return null;
    }


    private void requestEditTextFocus() {
        mHandler.sendEmptyMessageDelayed(MSG_FOCUS_EDITTEXT, 500);
    }

    private void setFocusLastEditText() {
        View lastItem = mMemoView.getChildAt(mMemoView.getChildCount() - 1);
        EditText et = findEditText(lastItem);

        if(et != null) {
            et.requestFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private EditText findEditText(View v) {
        if(v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            int cnt = vg.getChildCount();
            for(int i=cnt-1;i>=0;i--) {
                return findEditText(vg.getChildAt(i));
            }
        }else {
            if(v instanceof EditText) {
                return (EditText) v;
            }
        }

        return null;
    }

    public void setActionBarColor(ThemeUtil.MIDAS_THEME midasTheme) {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ThemeUtil.getMainColor(this, midasTheme)));
        getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(ThemeUtil.getMainColor(this, midasTheme)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ThemeUtil.getMainColor(this, midasTheme));
            getWindow().setStatusBarColor(ThemeUtil.getSystemColor(this, midasTheme));
        }
        mBtnAdd.setBackgroundTintList(ColorStateList.valueOf(ThemeUtil.getSystemColor(this, ThemeUtil.getTheme(this))));
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

                    requestEditTextFocus();
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

    private void startSave() {
        if(mSaveTask != null) {
            Snackbar.make(mMemoView, "저장중입니다", Snackbar.LENGTH_SHORT).show();
        }else {
            mSaveTask = new SaveTask();
            mSaveTask.execute();
        }
    }


    private void saveAndExit() {
        Memo memo = new Memo();

        for(MemoContent m : mAdapter.getAllList()) {
            memo.addMemoContent(m);
        }

        if(mMemoId < 0) {
            //created
            memo.setCreatedDate(System.currentTimeMillis());
        }else {
            memo.setId(mMemoId);
        }
        memo.setUpdateDate(System.currentTimeMillis());

        DBManager db = DBManager.getInstance(this);

        if(mMemoId < 0) {
            db.insertMemo(memo);
        }else {
            db.updateMemo(memo);
        }

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
                    String txt = ((EditText) v).getText().toString();

                    MemoContent memo = (MemoContent) tag;

                    if(!txt.equals(memo.getContent())) {
                        memo.setContent(txt);
                        mbUpdated = true;
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        updateDataRecursive(mMemoView);
        mbUpdated |= MemoTextViewHolder.bUpdated;

        if(mbUpdated) {
            //cancel
            (new AlertDialog.Builder(ViewActivity.this)).setMessage("작성한 내용이 삭제됩니다.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
        }else {
            super.onBackPressed();
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_FOCUS_EDITTEXT) {
                setFocusLastEditText();
            }
        }
    };

    private View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btn_cancel) {
                onBackPressed();
            }else if(v.getId() == R.id.btn_save) {
                //save and exit
                updateDataRecursive(mMemoView);

                if(mAdapter.isEmpty()) {
                    Snackbar snack = Snackbar.make(v, "메모가 작성되지 않았습니다.", Snackbar.LENGTH_SHORT);
                    snack.show();
                }else {
                    startSave();
                }
            }
        }
    };

    private MemoDetailAdapter.IMemoViewListener mMemoListener = new MemoDetailAdapter.IMemoViewListener() {
        @Override
        public void onDelete(int position) {
            Log.d("TEST", "DELETE : " + position);

            final int delPosition = position;

            (new AlertDialog.Builder(ViewActivity.this)).setMessage("메모 항목을 삭제합니다.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAdapter.removeItem(delPosition);
                            mAdapter.notifyItemRemoved(delPosition);
                        }
                    }).create().show();

        }
    };

    private class SaveTask extends AsyncTask<Void, Void, Boolean> {
        private HashMap<MemoContent, String> mNewPath;

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mNewPath = new HashMap<MemoContent, String>();

                for(MemoContent memo : mAdapter.getAllList()) {
                    if(memo.getContentType() == ContentType.CONTENT_TYPE_IMAGE) {
                        String path = memo.getContent();
                        if(path.startsWith("content://")) {
                            //gallery image. copy to local.
                            int idx;
                            String filename = path.substring(path.lastIndexOf('/') + 1);
                            idx = filename.lastIndexOf('.');
                            String ext = "";

                            if(idx != -1) {
                                ext = filename.substring(idx + 1);
                                filename = filename.substring(0, idx);
                            }

                            File f;
                            String toPath;
                            idx = 0;

                            do {
                                toPath = getExternalFilesDir(null).getAbsolutePath() + "/" + filename + "_" + idx;
                                if(ext.length() > 0) {
                                    toPath +=  "." + ext;
                                }

                                idx++;
                                f = new File(toPath);
                            }while(f.exists());

                            mNewPath.put(memo, toPath);

                            InputStream is = getContentResolver().openInputStream(Uri.parse(path));
                            FileOutputStream fos = new FileOutputStream(toPath);
                            byte [] buf = new byte[1024 * 1024];
                            int nRead;

                            while((nRead = is.read(buf)) > 0) {
                                fos.write(buf, 0, nRead);
                            }
                            fos.close();
                            is.close();
                        }
                    }
                }

                return true;
            }catch(FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean) {
                for(MemoContent memo : mAdapter.getAllList()) {
                    String newPath = mNewPath.get(memo);
                    if(newPath != null) {
                        memo.setContent(newPath);
                    }
                }

                saveAndExit();
            }else {
                Snackbar.make(mMemoView, "이미지 파일 저장중 오류 발생함", Snackbar.LENGTH_SHORT).show();
            }

            mSaveTask = null;
        }
    }
}
