package c.m.mobile8.adapter;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import c.m.mobile8.R;
import c.m.mobile8.models.MemoContent;

/**
 * Created by Hyosang on 2016-05-28.
 */
public class MemoTextViewHolder extends ViewHolderBase{
    public EditText mEditText;
    public MemoTextViewHolder(View itemView) {
        super(itemView);

        mEditText = (EditText) itemView.findViewById(R.id.memo_text);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("TEST", "FocusChanged : " + hasFocus);
                storeText();
            }
        });
    }

    private void storeText() {
        Object tag = mEditText.getTag();
        if(tag != null) {
            if(tag instanceof MemoContent) {
                MemoContent memo = (MemoContent) tag;
                memo.setContent(mEditText.getText().toString());

                Log.d("TEST", "Stored :: " + memo.getContent());
            }
        }
    }

    @Override
    public void setData(MemoContent content) {
        mEditText.setTag(content);
        mEditText.setText(content.getContent());
    }
}
