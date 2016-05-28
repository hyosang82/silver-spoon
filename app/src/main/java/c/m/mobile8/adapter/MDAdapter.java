package c.m.mobile8.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import c.m.mobile8.utils.ImageDecoder;
import c.m.mobile8.utils.ThemeUtil;

/**
 * Created by JunLee on 5/29/16.
 */
public class MDAdapter extends BaseAdapter {
    private final String TAG = "MDAdapter";
    private Context context;
    private Memo memo;

    public MDAdapter(Context context, Memo memo) {
        this.context = context;
        this.memo = memo;
    }

    @Override
    public int getCount() {
        if(memo == null || memo.getMemoContents() == null)
            return 0;
        return memo.getMemoContents().size();
    }
    @Override
    public Object getItem(int position) {
        if(memo == null || memo.getMemoContents() == null) {
            return null;
        } else {
            return memo.getMemoContents().get(position);
        }
    }
    @Override
    public long getItemId(int position) {return position;}

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(memo == null || memo.getMemoContents() == null) {
            return ContentType.CONTENT_TYPE_TEXT.ordinal();
        } else {
            return memo.getMemoContents().get(position).getContentType().ordinal();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextViewHolder textHolder = null;
        ImageViewHolder imageHolder = null;
        int viewType = getItemViewType(position);
        if(convertView == null) {
            if(viewType == ContentType.CONTENT_TYPE_TEXT.ordinal()) {
                textHolder = new TextViewHolder();
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.memo_item_text, null);
                textHolder.memo_text = (EditText) convertView.findViewById(R.id.memo_text);
                convertView.setTag(textHolder);
            } else if(viewType == ContentType.CONTENT_TYPE_IMAGE.ordinal()) {
                imageHolder = new ImageViewHolder();
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.memo_item_image, null);
                imageHolder.memo_image = (ImageView) convertView.findViewById(R.id.memo_image);
                convertView.setTag(imageHolder);
            }
        } else {
            if(viewType == ContentType.CONTENT_TYPE_TEXT.ordinal()) {
                textHolder = (TextViewHolder)convertView.getTag();
            } else if(viewType == ContentType.CONTENT_TYPE_IMAGE.ordinal()) {
                imageHolder = (ImageViewHolder)convertView.getTag();
            }
        }
        if(viewType == ContentType.CONTENT_TYPE_TEXT.ordinal()) {
            //TODO : text list인 경우
            MemoContent memoContent = (MemoContent)getItem(position);
            textHolder.memo_text.setText(memoContent.getContent());
            textHolder.memo_text.setTag(position);

        } else if(viewType == ContentType.CONTENT_TYPE_IMAGE.ordinal()) {
            //TODO : image list인 경우
            String contentImagePath = ((MemoContent)getItem(position)).getContent();
            ImageDecoder.getInstance().decode(contentImagePath, imageHolder.memo_image);
        }
        return convertView;
    }

    private class TextViewHolder {
        public EditText memo_text;
    }
    private class ImageViewHolder {
        public ImageView memo_image;
    }

    public void setMemo(Memo _memo) {
        memo = _memo;
    }
}
