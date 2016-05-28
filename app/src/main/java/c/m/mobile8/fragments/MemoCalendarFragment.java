package c.m.mobile8.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import c.m.mobile8.MainActivity;
import c.m.mobile8.R;
import c.m.mobile8.ViewActivity;
import c.m.mobile8.adapter.MemoListViewAdapter;
import c.m.mobile8.models.Memo;
import c.m.mobile8.utils.DBManager;
import c.m.mobile8.utils.ThemeUtil;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class MemoCalendarFragment extends Fragment {
    private CompactCalendarView mCalendar;
    private TextView mTextView;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private MemoListViewAdapter mMemoListViewAdapter;

    public MemoCalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_memo_calendar, container, false);

        mCalendar = (CompactCalendarView) rootView.findViewById(R.id.calendar_view);
        ListView listView = (ListView)rootView.findViewById(R.id.calendar_listview);

        if(mMemoListViewAdapter == null) {
            mMemoListViewAdapter = new MemoListViewAdapter(getActivity(), new ArrayList<Memo>());
        }
        List<Memo> memoList = ((MainActivity)getActivity()).mMemoList;
        Iterator<Memo> iter = memoList.iterator();
        while(iter.hasNext()) {
            Memo memo = iter.next();
            mCalendar.addEvent(new Event(Color.argb(255, 255, 255, 255), memo.getCreatedDate(), memo), false);
        }
        //today event
        List<Event> eventList = mCalendar.getEvents(new Date());
        List<Memo> memoTmpList = new ArrayList<Memo>();
        if (eventList != null) {
            for (Event event : eventList) {
                memoTmpList.add((Memo) event.getData());
            }

            if(mMemoListViewAdapter != null)
                mMemoListViewAdapter.setMemoList(memoList);
            mMemoListViewAdapter.notifyDataSetChanged();
        }
        listView.setAdapter(mMemoListViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Memo> memoList = ((MainActivity)getActivity()).mMemoList;
                enterDetailView(memoList.get(position).getId());
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder
                        .setTitle("삭제")
                        .setPositiveButton("확인",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                List<Memo> memoList = ((MainActivity)getActivity()).mMemoList;
                                DBManager.getInstance(getActivity().getApplicationContext()).deleteMemo(memoList.get(position).getId());
                                ((MainActivity)getActivity()).reloadData();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
            }
        });
        mMemoListViewAdapter.notifyDataSetChanged();
        mTextView = (TextView) rootView.findViewById(R.id.calendar_month_textview);
        initCalendar();
        return rootView;
    }

    public void initCalendar() {
        mCalendar.setCalendarBackgroundColor(ThemeUtil.getMainColor(getActivity(), ThemeUtil.getTheme(getActivity())));
        mCalendar.setCurrentSelectedDayBackgroundColor(ThemeUtil.getSystemColor(getActivity(), ThemeUtil.getTheme(getActivity())));

        mCalendar.invalidate();
        mTextView.setBackgroundColor(ThemeUtil.getMainColor(getActivity(), ThemeUtil.getTheme(getActivity())));
        mTextView.setText(dateFormatForMonth.format(mCalendar.getFirstDayOfCurrentMonth()));
        mCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> eventList = mCalendar.getEvents(dateClicked);
                List<Memo> memoList = new ArrayList<Memo>();
                if (eventList != null) {
                    for (Event event : eventList) {
                        memoList.add((Memo) event.getData());
                    }

                    if(mMemoListViewAdapter != null)
                        mMemoListViewAdapter.setMemoList(memoList);
                        mMemoListViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                mTextView.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

    }
    public void reloadData() {
        List<Memo> memoList = ((MainActivity)getActivity()).mMemoList;
        Iterator<Memo> iter = memoList.iterator();
        mCalendar.removeAllEvents();

        while(iter.hasNext()) {
            Memo memo = iter.next();
            mCalendar.addEvent(new Event(Color.argb(255, 255, 255, 255), memo.getCreatedDate(), memo), false);
        }
        List<Event> eventList = mCalendar.getEvents(mCalendar.getFirstDayOfCurrentMonth());
        List<Memo> memoTmpList = new ArrayList<Memo>();
        if (eventList != null) {
            for (Event event : eventList) {
                memoTmpList.add((Memo) event.getData());
            }

            if(mMemoListViewAdapter != null)
                mMemoListViewAdapter.setMemoList(memoList);
        }
        mMemoListViewAdapter.notifyDataSetChanged();
    }

    private void enterDetailView(int memoId) {
        Intent i = new Intent(getActivity(), ViewActivity.class);
        i.putExtra(ViewActivity.EXTRA_MEMO_ID, memoId);
        startActivity(i);
    }
}
