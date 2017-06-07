package com.dmitriyvedmid.mypills.CalendarActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dmitriyvedmid.mypills.DatePickerFragment;
import com.dmitriyvedmid.mypills.PrefManager;
import com.dmitriyvedmid.mypills.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CalendarFragment extends Fragment {

    TextView start;
    String TAG = "My";
    EditText length, cycle;
    PrefManager mPrefManager;
    Date startDate;
    CompactCalendarView compactCalendarView;
    long lengthDate, cycleDate;
    long day = 1000 * 60 * 60 * 24;
    ArrayList<Event> mEvents = new ArrayList<>();
    public static final int REQUEST_DATE = 3;
    public static final String DIALOG_DATE = "DialogDate";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        compactCalendarView = (CompactCalendarView) view.findViewById(R.id.calendar);
        mPrefManager = new PrefManager(getActivity());
        start = (TextView) view.findViewById(R.id.calendar_previous_menstruation_start);
        length = (EditText) view.findViewById(R.id.calendar_previous_menstruation_length);
        cycle = (EditText) view.findViewById(R.id.calendar_cycle_length);
        length.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        length.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    mPrefManager.setPrevLast(Long.parseLong(length.getText().toString()));
                    lengthDate = Long.parseLong(length.getText().toString());
                    updateMenstruation();
                } catch (Exception e) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cycle.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        cycle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    mPrefManager.setPrevInterval(Long.parseLong(cycle.getText().toString()));
                    cycleDate = Long.parseLong(cycle.getText().toString());
                    updateMenstruation();
                } catch (Exception e1) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        initValues();
        updateMenstruation();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                date.setTime(mPrefManager.getPrevStart());
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(date);
                dialog.setTargetFragment(CalendarFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        return view;
    }

    public void initValues() {
        startDate = new Date();
        startDate.setTime(mPrefManager.getPrevStart());
        lengthDate = mPrefManager.getPrevLast();
        cycleDate = mPrefManager.getPrevInterval();
        start.setText(dateToString(startDate));
        length.setText(String.valueOf(lengthDate));
        cycle.setText(String.valueOf(cycleDate));
    }

    public void updateMenstruation() {
        compactCalendarView.removeAllEvents();
        for (int i = 0; i < lengthDate; i++) {
            mEvents.add(new Event(Color.GREEN, startDate.getTime() + i * AlarmManager.INTERVAL_DAY));
            compactCalendarView.addEvent(mEvents.get(i), false);
        }
    }

    public String dateToString(Date date) {
        SimpleDateFormat dfDate_day = new SimpleDateFormat("dd.MM.yyyy");
        return dfDate_day.format(date);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mPrefManager.setPrevStart(date.getTime());
            start.setText(dateToString(date));
            startDate = date;
            compactCalendarView.removeAllEvents();
            for (int i = 0; i < lengthDate; i++) {
                mEvents.add(new Event(Color.GREEN, date.getTime() + i * day));
                compactCalendarView.addEvent(mEvents.get(i), false);
            }
        }
    }

}
