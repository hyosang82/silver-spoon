package c.m.mobile8.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import c.m.mobile8.R;
import c.m.mobile8.models.MemoContent;
import c.m.mobile8.utils.ImageDecoder;

/**
 * Created by Hyosang on 2016-05-28.
 */
public class MemoImageViewHolder extends ViewHolderBase {
    public static interface IImageMemoListener {
        public void onImageClicked(String location);
        public void onImageLongClicked(View v);
    }

    public ImageView mImageView;
    private IImageMemoListener mListener = null;

    public MemoImageViewHolder(View mainView, View itemView) {
        super(mainView);

        mImageView = (ImageView) itemView.findViewById(R.id.memo_image);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if(tag != null && tag instanceof String) {
                    String location = (String) tag;

                    if(mListener != null) {
                        mListener.onImageClicked(location);
                    }
                }
            }
        });
        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("Holder", "longclick");
                //
                return true;
            }
        });
    }

    public void setListener(IImageMemoListener listener) {
        mListener = listener;
    }

    @Override
    public void setData(MemoContent content) {
        ImageDecoder.getInstance().decode(content.getContent(), mImageView);
        mImageView.setTag(content.getContent());
    }
}
