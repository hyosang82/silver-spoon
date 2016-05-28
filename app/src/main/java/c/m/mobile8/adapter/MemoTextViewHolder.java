package c.m.mobile8.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import c.m.mobile8.R;

/**
 * Created by Hyosang on 2016-05-28.
 */
public class MemoTextViewHolder extends RecyclerView.ViewHolder{
    public TextView mTextView;
    public MemoTextViewHolder(View itemView) {
        super(itemView);

        mTextView = (TextView) itemView.findViewById(R.id.memo_text);
    }
}
