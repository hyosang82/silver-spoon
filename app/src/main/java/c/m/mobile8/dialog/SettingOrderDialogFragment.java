package c.m.mobile8.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import c.m.mobile8.R;
import c.m.mobile8.adapter.SettingOrderResultListAdapter;
import c.m.mobile8.adapter.SettingThemeResultListAdapter;
import c.m.mobile8.utils.SharedPreferenceManager;
import c.m.mobile8.utils.ThemeUtil;


@SuppressLint("ValidFragment")
public class SettingOrderDialogFragment extends DialogFragment {
    public interface OnOrderItemClickListener {
        public void onClickOrder(int orderType);
    }

    private ViewHolder mViewHolder;
    private SettingOrderResultListAdapter mOrderListAdapter;
    private OnOrderItemClickListener onOrderItemClickListener;

    public SettingOrderDialogFragment(OnOrderItemClickListener orderItemClickListener) {
        onOrderItemClickListener = orderItemClickListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.dialog_set_order, container, false);
        mViewHolder = new ViewHolder(rootView);
        initViews(rootView);
        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_set_order);
        return dialog;
    }

    private void initViews(View rootView) {
        getDialog().setTitle("정렬 순서 설정");
        ArrayList<Integer> orderTypeList = new ArrayList<Integer>();
        orderTypeList.add(0);
        orderTypeList.add(1);
        orderTypeList.add(2);

        if (mOrderListAdapter == null) {
            int currentOrderType = SharedPreferenceManager.getPreferencesOrder(getActivity());
            mOrderListAdapter = new SettingOrderResultListAdapter(getActivity(), orderTypeList, currentOrderType);
        }
        mViewHolder.listview_order.setAdapter(mOrderListAdapter);
    }

    class ViewHolder {
        public ViewHolder(View rootView) {
            listview_order = (ListView) rootView.findViewById(R.id.listview_order);
            listview_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final int orderType = (Integer)parent.getAdapter()
                            .getItem(position);
                    if (onOrderItemClickListener != null) {
                        onOrderItemClickListener.onClickOrder(orderType);
                    }
                    dismiss();
                }
            });
            button_cancel = (Button) rootView.findViewById(R.id.button_cancel);
            button_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        ListView listview_order;
        Button button_cancel;
    }
}
