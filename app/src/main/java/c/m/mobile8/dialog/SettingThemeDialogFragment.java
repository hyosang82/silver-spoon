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
import c.m.mobile8.adapter.SettingThemeResultListAdapter;
import c.m.mobile8.utils.ThemeUtil;


@SuppressLint("ValidFragment")
public class SettingThemeDialogFragment extends DialogFragment {
    public interface OnThemeItemClickListener {
        public void onClickTheme(ThemeUtil.MIDAS_THEME busTheme);
    }

    private ViewHolder mViewHolder;
    private SettingThemeResultListAdapter mThemeListAdapter;
    private OnThemeItemClickListener onThemeItemClickListener;
    private boolean isActivityMode = false;
    private ThemeUtil.MIDAS_THEME activityBasedTheme;

    public SettingThemeDialogFragment(OnThemeItemClickListener themeItemClickListener) {
        onThemeItemClickListener = themeItemClickListener;
    }
    public SettingThemeDialogFragment(OnThemeItemClickListener themeItemClickListener, boolean isActivityMode, ThemeUtil.MIDAS_THEME activityBasedTheme) {
        onThemeItemClickListener = themeItemClickListener;
        this.isActivityMode = isActivityMode;
        this.activityBasedTheme = activityBasedTheme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.dialog_set_theme, container, false);
        mViewHolder = new ViewHolder(rootView);
        initViews(rootView);
        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_set_theme);
        return dialog;
    }

    private void initViews(View rootView) {
        getDialog().setTitle("테마 설정");
        ArrayList<ThemeUtil.MIDAS_THEME> busThemeList = new ArrayList<ThemeUtil.MIDAS_THEME>();
        for (ThemeUtil.MIDAS_THEME busTheme : ThemeUtil.MIDAS_THEME.values()) {
            busThemeList.add(busTheme);
        }
        if(isActivityMode) {
            mThemeListAdapter = new SettingThemeResultListAdapter(getActivity(), busThemeList, activityBasedTheme );
        }
        if (mThemeListAdapter == null) {
            mThemeListAdapter = new SettingThemeResultListAdapter(getActivity(), busThemeList, ThemeUtil.getTheme(getActivity()));
        }
        mViewHolder.listview_theme.setAdapter(mThemeListAdapter);
    }

    class ViewHolder {
        public ViewHolder(View rootView) {
            listview_theme = (ListView) rootView.findViewById(R.id.listview_theme);
            listview_theme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final ThemeUtil.MIDAS_THEME busTheme = (ThemeUtil.MIDAS_THEME) parent.getAdapter()
                            .getItem(position);
                    if (onThemeItemClickListener != null) {
                        onThemeItemClickListener.onClickTheme(busTheme);
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

        ListView listview_theme;
        Button button_cancel;
    }
}
