package c.m.mobile8.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import c.m.mobile8.R;
import c.m.mobile8.models.Memo;
import c.m.mobile8.models.MemoContent;
import c.m.mobile8.models.enums.ContentType;

/**
 * Created by kdggg on 2016-05-28.
 */
public class MemoListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Memo> memoList;
    private boolean[] isSelected;

    public MemoListViewAdapter(Context context, List<Memo> memoList) {
        this.context = context;
        this.memoList = memoList;
        this.isSelected = new boolean[this.memoList.size()];
    }

    @Override
    public int getCount() {return memoList.size();}
    @Override
    public Object getItem(int position) {return position;}
    @Override
    public long getItemId(int position) {return position;}

    public void setSelected(boolean[] isSelected) {
        this.isSelected = isSelected;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.memo_list_item, null);
            holder.textViewMemoTitle = (TextView)convertView.findViewById(R.id.textViewMemoTitle);
            holder.textViewUpdateDate = (TextView)convertView.findViewById(R.id.textViewUpdateDate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        Memo memo = memoList.get(position);

        SimpleDateFormat formatter = new SimpleDateFormat ( "yy/MM/dd HH:mm", Locale.KOREA );
        Date currentTime = new Date();
        currentTime.setTime(memo.getUpdateDate());
        String updateDate = formatter.format ( currentTime );

        //CONTENT_TYPE_TEXT
        Iterator<MemoContent> iter = memo.getMemoContents().iterator();
        String content = "";
        while (iter.hasNext()) {
            MemoContent memoContent = iter.next();
            if(memoContent.getContentType() == ContentType.CONTENT_TYPE_TEXT) {
                content += memoContent.getContent();

            } else if(memoContent.getContentType() == ContentType.CONTENT_TYPE_IMAGE) {
                content += " (사진) ";
            } else if(memoContent.getContentType() == ContentType.CONTENT_TYPE_AUDIO) {
                content += " (음성) ";
            } else if(memoContent.getContentType() == ContentType.CONTENT_TYPE_VIDIO) {
                content += " (영상) ";
            }
        }

        if(isSelected[position]) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            convertView.setBackground(null);
        }
        holder.textViewUpdateDate.setText(updateDate);
        holder.textViewMemoTitle.setText(content);

        return convertView;
    }
    private class ViewHolder {
        public TextView textViewUpdateDate;
        public TextView textViewMemoTitle;
    }
}
