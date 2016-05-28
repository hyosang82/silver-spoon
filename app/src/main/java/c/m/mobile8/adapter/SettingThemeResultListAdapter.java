package c.m.mobile8.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import c.m.mobile8.R;
import c.m.mobile8.utils.ThemeUtil;


public class SettingThemeResultListAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList<ThemeUtil.MIDAS_THEME> mThemeList;
    private ThemeUtil.MIDAS_THEME mSelectedTheme;

    public SettingThemeResultListAdapter(Activity activity, ArrayList<ThemeUtil.MIDAS_THEME> themeList, ThemeUtil.MIDAS_THEME selectedTheme) {
        mActivity = activity;
        mThemeList = themeList;
        mSelectedTheme = selectedTheme;
    }

    public int getCount() {
        if (mThemeList == null)
            return 0;
        else
            return mThemeList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mThemeList == null) {
            return null;
        }
        return mThemeList.get(position);
    }


    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        SettingThemeItemViewHolder holder = null;
        if (convertView == null) {
            view = mActivity.getLayoutInflater().inflate(R.layout.list_item_setting_theme, parent, false);
            holder = new SettingThemeItemViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (SettingThemeItemViewHolder) view.getTag();
        }
        //setting views
        ThemeUtil.MIDAS_THEME busTheme = mThemeList.get(position);
        holder.relativelayout_main.setBackgroundColor(ThemeUtil.getMainColor(mActivity, busTheme));
        holder.theme_name.setText(busTheme.toString().replace("THEME_", "").replace("_", " "));
        if (mSelectedTheme == busTheme) {
            holder.theme_heart.setVisibility(View.VISIBLE);
        } else {
            holder.theme_heart.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    private static class SettingThemeItemViewHolder {
        public SettingThemeItemViewHolder(View convertView) {
            relativelayout_main = (RelativeLayout) convertView.findViewById(R.id.relativelayout_main);
            theme_name = (TextView) convertView.findViewById(R.id.theme_name);
            theme_heart = (ImageView) convertView.findViewById(R.id.theme_heart);
        }

        RelativeLayout relativelayout_main;
        TextView theme_name;
        ImageView theme_heart;
    }


    @Override
    public int getViewTypeCount() {
        return 1;
    }

}