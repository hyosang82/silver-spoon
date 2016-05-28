package c.m.mobile8.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import c.m.mobile8.MainActivity;
import c.m.mobile8.R;
import c.m.mobile8.dialog.SettingOrderDialogFragment;
import c.m.mobile8.dialog.SettingThemeDialogFragment;
import c.m.mobile8.utils.AppInfo;
import c.m.mobile8.utils.SharedPreferenceManager;
import c.m.mobile8.utils.ThemeUtil;


public class ConfigFragment extends Fragment implements SettingThemeDialogFragment.OnThemeItemClickListener, SettingOrderDialogFragment.OnOrderItemClickListener {
    private final String TAG = "ConfigFragment";
    private ViewHolder mViewHolder;

    public ConfigFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_config, container, false);
        mViewHolder = new ViewHolder(rootView);
        return rootView;
    }
    class ViewHolder {
        public ViewHolder(View rootView) {
            final String appVersionName = AppInfo.getInstance(getActivity()).getVersionName();
            final int appVersionCode = AppInfo.getInstance(getActivity()).getVersionCode();

            current_version = (TextView) rootView.findViewById(R.id.current_version);
            current_version.setText(appVersionName);
            contact_theme_setting = (RelativeLayout) rootView.findViewById(R.id.contact_theme_setting);
            contact_theme_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction tr = fm.beginTransaction();
                    Fragment prev = fm.findFragmentByTag("SettingThemeDialogFragment");
                    if (prev != null) {
                        tr.remove(prev);
                    }
                    SettingThemeDialogFragment dialog = new SettingThemeDialogFragment(ConfigFragment.this);
                    dialog.show(tr, "SettingThemeDialogFragment");
                }
            });
            current_theme = (TextView) rootView.findViewById(R.id.current_theme);
            ThemeUtil.MIDAS_THEME currentTheme = ThemeUtil.getTheme(getActivity());
            current_theme.setText(currentTheme.toString().replace("THEME_", "").replace("_", " "));
            current_theme.setBackgroundColor(ThemeUtil.getMainColor(getActivity(), currentTheme));
            contact_opensource_license = (RelativeLayout) rootView.findViewById(R.id.contact_opensource_license);
            contact_opensource_license.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebView view = (WebView) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_licenses, null);
                    view.loadUrl("file:///android_asset/opensource_licenses.html");
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.action_licenses))
                            .setView(view)
                            .setPositiveButton(android.R.string.ok, null)
                            .create().show();
                }
            });

            contact_opensource_license = (RelativeLayout) rootView.findViewById(R.id.contact_opensource_license);
            contact_order_setting = (RelativeLayout) rootView.findViewById(R.id.contact_order_setting);
            current_order = (TextView) rootView.findViewById(R.id.current_order);
            int orderType = SharedPreferenceManager.getPreferencesOrder(getActivity());
            switch(orderType) {
                case SharedPreferenceManager.ORDER_BY_CREATED:
                    current_order.setText("초기 작성일 기준");
                    break;
                case SharedPreferenceManager.ORDER_BY_UPDATE:
                    current_order.setText("업데이트 기준");
                    break;
                case SharedPreferenceManager.ORDER_BY_THEME:
                    current_order.setText("색상 기준");
                    break;
            }
            contact_order_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction tr = fm.beginTransaction();
                    Fragment prev = fm.findFragmentByTag("SettingOrderDialogFragment");
                    if (prev != null) {
                        tr.remove(prev);
                    }
                    SettingOrderDialogFragment dialog = new SettingOrderDialogFragment(ConfigFragment.this);
                    dialog.show(tr, "SettingOrderDialogFragment");
                }
            });
        }

        TextView current_version;
        RelativeLayout contact_theme_setting;
        TextView current_theme;
        RelativeLayout contact_opensource_license;
        RelativeLayout contact_order_setting;
        TextView current_order;
    }

    @Override
    public void onClickTheme(ThemeUtil.MIDAS_THEME midasTheme) {
        mViewHolder.current_theme.setText(midasTheme.toString().replace("THEME_", "").replace("_", " "));
        mViewHolder.current_theme.setBackgroundColor(ThemeUtil.getMainColor(getActivity(), midasTheme));
        ThemeUtil.setTheme(getActivity(), midasTheme);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setActionBarColor(midasTheme);
    }

    @Override
    public void onClickOrder(int orderType) {
        switch(orderType) {
            case SharedPreferenceManager.ORDER_BY_CREATED:
                mViewHolder.current_order.setText("초기 작성일 기준");
                SharedPreferenceManager.setPreferenceOrder(getActivity(), SharedPreferenceManager.ORDER_BY_CREATED);
                break;
            case SharedPreferenceManager.ORDER_BY_UPDATE:
                mViewHolder.current_order.setText("업데이트 기준");
                SharedPreferenceManager.setPreferenceOrder(getActivity(), SharedPreferenceManager.ORDER_BY_UPDATE);
                break;
            case SharedPreferenceManager.ORDER_BY_THEME:
                mViewHolder.current_order.setText("색상 기준");
                SharedPreferenceManager.setPreferenceOrder(getActivity(), SharedPreferenceManager.ORDER_BY_THEME);
                break;
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setOrderType(orderType);
    }

}
