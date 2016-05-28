package c.m.mobile8.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

    @Override
    public ViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType) {
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

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        try {
            return vhClass.getConstructor(View.class).newInstance(v);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolderBase holder, int position) {
        MemoContent item = mList.get(position);

        if(item != null) {
            holder.setData(item);
        }
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

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(Memo memo) {
        Map<Integer, MemoContent> conts = new TreeMap<>(memo.getMemoContents());

        for(Map.Entry<Integer, MemoContent> entry : conts.entrySet()) {
            mList.add(entry.getValue());
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
}
