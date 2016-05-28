package c.m.mobile8.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import c.m.mobile8.R;
import c.m.mobile8.models.MemoContent;

/**
 * Created by Hyosang on 2016-05-28.
 */
public class MemoTextViewHolder extends ViewHolderBase{
    public TextView mTextView;
    public MemoTextViewHolder(View itemView) {
        super(itemView);

        mTextView = (TextView) itemView.findViewById(R.id.memo_text);
    }

    @Override
    public void setData(MemoContent content) {
        mTextView.setText(content.getContent());
    }
}
