package c.m.mobile8.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * Created by Hyosang on 2016-05-28.
 */
public class DialogBase extends Dialog {
    protected IDialogListener mListener = null;

    public DialogBase(Context context) {
        super(context);
    }

    public void setListener(IDialogListener listener) {
        mListener = listener;
    }


    protected View.OnClickListener mDefaultButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mListener != null) {
                mListener.onButtonClicked(DialogBase.this, v);
            }
        }
    };


    public static interface IDialogListener {
        public void onButtonClicked(Dialog me, View btnView);
    };
}
