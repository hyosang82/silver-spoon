package c.m.mobile8.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import c.m.mobile8.R;
import c.m.mobile8.models.MemoContent;

/**
 * Created by Hyosang on 2016-05-28.
 */
public class MemoImageViewHolder extends ViewHolderBase{
    public ImageView mImageView;
    public MemoImageViewHolder(View itemView) {
        super(itemView);

        mImageView = (ImageView) itemView.findViewById(R.id.memo_image);
    }

    @Override
    public void setData(MemoContent content) {
        mImageView.setImageURI(Uri.parse(content.getContent()));
    }
}
