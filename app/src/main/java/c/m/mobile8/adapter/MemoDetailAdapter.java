package c.m.mobile8.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import c.m.mobile8.R;
import c.m.mobile8.adapter.MemoTextViewHolder;
import c.m.mobile8.models.MemoContent;

/**
 * Created by Hyosang on 2016-05-28.
 */
public class MemoDetailAdapter extends RecyclerView.Adapter<MemoTextViewHolder> {
    private List<MemoContent> mList = new ArrayList<MemoContent>();

    @Override
    public MemoTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_item_text, parent, false);

        MemoTextViewHolder vh = new MemoTextViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MemoTextViewHolder holder, int position) {
        MemoContent item = mList.get(position);

        if(item != null) {
            holder.mTextView.setText(item.getContent());
        }
    }

    public void addItem(MemoContent item) {
        mList.add(item);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
