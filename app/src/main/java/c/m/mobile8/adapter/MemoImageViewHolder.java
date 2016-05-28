package c.m.mobile8.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import c.m.mobile8.R;
import c.m.mobile8.models.MemoContent;
import c.m.mobile8.utils.ImageDecoder;

/**
 * Created by Hyosang on 2016-05-28.
 */
public class MemoImageViewHolder extends ViewHolderBase{
    public ImageView mImageView;
    public MemoImageViewHolder(View mainView, View itemView) {
        super(mainView);

        mImageView = (ImageView) itemView.findViewById(R.id.memo_image);
    }

    @Override
    public void setData(MemoContent content) {
        ImageDecoder.getInstance().decode(content.getContent(), mImageView);
    }
}
