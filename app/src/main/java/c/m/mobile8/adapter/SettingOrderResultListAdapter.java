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
import c.m.mobile8.utils.SharedPreferenceManager;
import c.m.mobile8.utils.ThemeUtil;


public class SettingOrderResultListAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList<Integer> mOrderList;
    private int mSelectedOrder;

    public SettingOrderResultListAdapter(Activity activity, ArrayList<Integer> orderList, int selectedOrder) {
        mActivity = activity;
        mOrderList = orderList;
        mSelectedOrder = selectedOrder;
    }

    public int getCount() {
        if (mOrderList == null)
            return 0;
        else
            return mOrderList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mOrderList == null) {
            return null;
        }
        return mOrderList.get(position);
    }


    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        SettingOrderItemViewHolder holder = null;
        if (convertView == null) {
            view = mActivity.getLayoutInflater().inflate(R.layout.list_item_setting_order, parent, false);
            holder = new SettingOrderItemViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (SettingOrderItemViewHolder) view.getTag();
        }
        //setting views
        int order = mOrderList.get(position);
        switch(order) {
            case SharedPreferenceManager.ORDER_BY_CREATED:
                holder.order_name.setText("초기 작성일 기준");
                break;
            case SharedPreferenceManager.ORDER_BY_THEME:
                holder.order_name.setText("색상 기준");
                break;
            case SharedPreferenceManager.ORDER_BY_UPDATE:
                holder.order_name.setText("업데이트 기준");
                break;
        }
        if (mSelectedOrder == order) {
            holder.order_heart.setVisibility(View.VISIBLE);
        } else {
            holder.order_heart.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    private static class SettingOrderItemViewHolder {
        public SettingOrderItemViewHolder(View convertView) {
            relativelayout_main = (RelativeLayout) convertView.findViewById(R.id.relativelayout_main);
            order_name = (TextView) convertView.findViewById(R.id.order_name);
            order_heart = (ImageView) convertView.findViewById(R.id.order_heart);
        }

        RelativeLayout relativelayout_main;
        TextView order_name;
        ImageView order_heart;
    }


    @Override
    public int getViewTypeCount() {
        return 1;
    }

}