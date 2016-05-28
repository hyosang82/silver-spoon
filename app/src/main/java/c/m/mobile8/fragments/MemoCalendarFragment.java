package c.m.mobile8.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.Calendar;
import java.util.Date;

import c.m.mobile8.R;
import c.m.mobile8.utils.DBManager;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class MemoCalendarFragment extends Fragment {
    private CalendarView mCalendar;

    public MemoCalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_memo_calendar, container, false);

        mCalendar = (CalendarView) rootView.findViewById(R.id.calendar_view);
        initCalendar();
        return rootView;
    }

    public void initCalendar() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        long tmpStartDate = DBManager.getInstance(getActivity()).getStartDateForCalendar();
        Date today = new Date();

        if(tmpStartDate == 0) {
            startDate.setTime(today);
            endDate.setTime(today);
            startDate.add(Calendar.MONTH, -1);
            endDate.add(Calendar.MONTH, 1);
            mCalendar.setMinDate(startDate.getTimeInMillis());
            mCalendar.setMaxDate(endDate.getTimeInMillis());
        } else {
            startDate.setTimeInMillis(tmpStartDate);
            endDate.setTime(today);
            startDate.add(Calendar.MONTH, -1);
            endDate.add(Calendar.MONTH, 1);
            mCalendar.setMinDate(startDate.getTimeInMillis());
            mCalendar.setMaxDate(endDate.getTimeInMillis());
        }
        //TODO: MainActivity에서 들고있는 Memo List를 달력에 표현
        mCalendar.setDate(startDate.getTimeInMillis());
        mCalendar.setDate(endDate.getTimeInMillis());
//        mCalendar.addEv
        //TODO: click listener 구현

    }

}
