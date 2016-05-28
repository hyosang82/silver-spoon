package c.m.mobile8.dialog;

import android.app.Dialog;
import android.content.Context;

import c.m.mobile8.R;

/**
 * Created by Hyosang on 2016-05-28.
 */
public class NewMemoItemDialog extends DialogBase {
    public NewMemoItemDialog(Context context) {
        super(context);

        setTitle("메모 항목 추가");

        setContentView(R.layout.dialog_new_memo_item);

        findViewById(R.id.btn_text).setOnClickListener(mDefaultButtonListener);
        findViewById(R.id.btn_photo).setOnClickListener(mDefaultButtonListener);

    }
}
