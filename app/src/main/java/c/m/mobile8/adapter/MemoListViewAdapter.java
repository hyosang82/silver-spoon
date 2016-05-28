package c.m.mobile8.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import c.m.mobile8.utils.ThemeUtil;

/**
 * Created by kdggg on 2016-05-28.
 */
public class MemoListViewAdapter extends BaseAdapter {
    private final String TAG = "MemoListViewAdapter";
    private Context context;
    private List<Memo> memoList;

    public MemoListViewAdapter(Context context, List<Memo> memoList) {
        this.context = context;
        this.memoList = memoList;
    }

    @Override
    public int getCount() {return memoList.size();}
    @Override
    public Object getItem(int position) {return position;}
    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.memo_list_item, null);
            holder.textViewMemoTitle = (TextView)convertView.findViewById(R.id.textViewMemoTitle);
            holder.textViewUpdateDate = (TextView)convertView.findViewById(R.id.textViewUpdateDate);
            holder.imageViewContainImage = (ImageView) convertView.findViewById(R.id.imageViewContainImage);
            holder.imageViewContainAudio = (ImageView) convertView.findViewById(R.id.imageViewContainAudio);
            holder.imageViewContainVideo = (ImageView) convertView.findViewById(R.id.imageViewContainVideo);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        Memo memo = memoList.get(position);

        SimpleDateFormat formatter = new SimpleDateFormat ("yy/MM/dd HH:mm", Locale.KOREA);
        Date currentTime = new Date();
        currentTime.setTime(memo.getUpdateDate());
        String updateDate = formatter.format ( currentTime );

        //CONTENT_TYPE_TEXT
        Iterator<MemoContent> iter = memo.getMemoContents().iterator();
        String content = "";
        boolean[] isContainFile = new boolean[3];
        while (iter.hasNext()) {
            MemoContent memoContent = iter.next();
            if(memoContent.getContentType() == ContentType.CONTENT_TYPE_TEXT) {
                content += memoContent.getContent();
            } else if(memoContent.getContentType() == ContentType.CONTENT_TYPE_IMAGE) {
                isContainFile[0] = true;
            } else if(memoContent.getContentType() == ContentType.CONTENT_TYPE_AUDIO) {
                isContainFile[1] = true;
            } else if(memoContent.getContentType() == ContentType.CONTENT_TYPE_VIDIO) {
                isContainFile[2] = true;
            }
        }
        holder.textViewUpdateDate.setText(updateDate);
        if(!content.equals("")) holder.textViewMemoTitle.setText(content);
        else holder.textViewMemoTitle.setText("텍스트 없음");

        if (isContainFile[0]) holder.imageViewContainImage.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_menu_camera));
        else holder.imageViewContainImage.setImageBitmap(null);
        if (isContainFile[1]) holder.imageViewContainAudio.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_menu_slideshow));
        else holder.imageViewContainAudio.setImageBitmap(null);
        if (isContainFile[2]) holder.imageViewContainVideo.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_btn_speak_now));
        else holder.imageViewContainVideo.setImageBitmap(null);

        return convertView;
    }
    private class ViewHolder {
        public TextView textViewUpdateDate;
        public TextView textViewMemoTitle;
        public ImageView imageViewContainImage;
        public ImageView imageViewContainAudio;
        public ImageView imageViewContainVideo;
    }

    public void setMemoList(List<Memo> _memoList) {
        memoList = _memoList;
    }
}
