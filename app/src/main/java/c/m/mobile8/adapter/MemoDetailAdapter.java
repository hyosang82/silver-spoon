package c.m.mobile8.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import c.m.mobile8.R;
import c.m.mobile8.adapter.MemoTextViewHolder;
import c.m.mobile8.models.Memo;
import c.m.mobile8.models.MemoContent;

/**
 * Created by Hyosang on 2016-05-28.
 */
public class MemoDetailAdapter extends RecyclerView.Adapter<ViewHolderBase> {
    private static final int VIEW_TYPE_MEMO_TEXT = 0x01;
    private static final int VIEW_TYPE_MEMO_IMAGE = 0x02;

    private List<MemoContent> mList = new ArrayList<MemoContent>();
    private IMemoViewListener mListener = null;

    public static interface IMemoViewListener {
        public void onDelete(int position);
        public void onImageClicked(String location);
        public void onImageLongClicked(View v);
    }

    @Override
    public ViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View mainView = inflater.inflate(R.layout.memo_item_base, parent, false);
        FrameLayout contArea = (FrameLayout) mainView.findViewById(R.id.fl_content);
        View contView = null;

        int layout = R.layout.memo_item_text;
        Class<? extends ViewHolderBase> vhClass = null;

        switch(viewType) {
            case VIEW_TYPE_MEMO_TEXT:
                layout = R.layout.memo_item_text;
                vhClass = MemoTextViewHolder.class;
                break;

            case VIEW_TYPE_MEMO_IMAGE:
                layout = R.layout.memo_item_image;
                vhClass = MemoImageViewHolder.class;
                break;
        }

        contView = inflater.inflate(layout, (ViewGroup) contArea, true);

        ImageView btnDelete = (ImageView) mainView.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    Object tag = v.getTag();
                    if(tag instanceof Integer) {
                        mListener.onDelete((int)tag);
                    }
                }
            }
        });

        try {
            ViewHolderBase vh = (ViewHolderBase) vhClass.getConstructor(View.class, View.class).newInstance(mainView, contView);
            vh.mBtnDelete = btnDelete;
            if(vh instanceof MemoImageViewHolder) {
                ((MemoImageViewHolder) vh).setListener(mImageListener);
            }
            return vh;
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolderBase holder, int position) {
        MemoContent item = mList.get(position);

        if(item != null) {
            holder.mBtnDelete.setTag(position);
            holder.setData(item);
        }
    }

    public void setListener(IMemoViewListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        MemoContent item = mList.get(position);

        if(item != null) {
            switch(item.getContentType()) {
                case CONTENT_TYPE_IMAGE: return VIEW_TYPE_MEMO_IMAGE;
                case CONTENT_TYPE_TEXT: return VIEW_TYPE_MEMO_TEXT;
            }
        }

        return VIEW_TYPE_MEMO_TEXT;
    }

    public int addItem(MemoContent item) {
        if(mList.add(item)) {
            return mList.size() - 1;
        }

        return -1;
    }

    public void removeItem(int position) {
        mList.remove(position);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(Memo memo) {
        List<MemoContent> conts = memo.getMemoContents();

        for(MemoContent m : conts) {
            mList.add(m);
        }
    }

    public List<MemoContent> getAllList() {
        return mList;
    }

    public boolean isEmpty() {
        if(mList.size() == 1) {
            if(mList.get(0).getContent().length() == 0) {
                return true;
            }
        }else if(mList.size() == 0) {
            return true;
        }else {
            return false;
        }

        return false;
    }

    private MemoImageViewHolder.IImageMemoListener mImageListener = new MemoImageViewHolder.IImageMemoListener() {
        @Override
        public void onImageClicked(String location) {
            if(mListener != null) {
                mListener.onImageClicked(location);
            }
        }
        @Override
        public void onImageLongClicked(View v) {
            if(mListener != null) {
                Log.e("DetailAdapter", "longclick");
            }
        }
    };
}
