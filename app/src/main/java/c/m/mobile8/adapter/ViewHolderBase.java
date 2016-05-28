package c.m.mobile8.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import c.m.mobile8.models.MemoContent;

/**
 * Created by Hyosang on 2016-05-28.
 */
public abstract class ViewHolderBase extends RecyclerView.ViewHolder {
    public ImageView mBtnDelete;

    public ViewHolderBase(View itemView) {
        super(itemView);
    }


    public abstract void setData(MemoContent content);
}
