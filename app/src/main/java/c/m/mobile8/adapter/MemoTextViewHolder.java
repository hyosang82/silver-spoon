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
    public static boolean bUpdated = false;

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
                String txt = mEditText.getText().toString();

                MemoContent memo = (MemoContent) tag;

                if(!txt.equals(memo.getContent())) {
                    memo.setContent(txt);
                    bUpdated = true;
                }
            }
        }
    }

    @Override
    public void setData(MemoContent content) {
        mEditText.setTag(content);
        mEditText.setText(content.getContent());
    }
}
