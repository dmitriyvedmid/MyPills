package com.dmitriyvedmid.mypills.CalendarActivity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dmitriyvedmid.mypills.R;

/**
 * Created by dmitr on 6/6/2017.
 */

public class CalendarActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        if (savedInstanceState == null) {
            CalendarFragment mFragment = new CalendarFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.calendar_holder, mFragment);
            ft.commit();
        }
    }
}
