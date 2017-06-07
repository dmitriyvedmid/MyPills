package com.dmitriyvedmid.mypills.AddDrug;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dmitriyvedmid.mypills.AlarmNotificationReciever;
import com.dmitriyvedmid.mypills.Database;
import com.dmitriyvedmid.mypills.DrugItem;
import com.dmitriyvedmid.mypills.DrugsIntent.DrugsListActivity;
import com.dmitriyvedmid.mypills.R;
import com.dmitriyvedmid.mypills.TimePickerFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static android.content.Context.ALARM_SERVICE;
import static com.dmitriyvedmid.mypills.MainActivity.sDrugItems;


public class AddDrugFragment extends Fragment implements TextWatcher {

    String TAG = "My";
    String[] data = {"Таблетки", "Ампули", "Краплі", "Грами", "Міліграми", "Мілілітри", "Одиниці", "Прийоми"};
    String[] data1 = {"Раз на N годин", "Раз на N днів", "Раз на N тижнів"};
    EditText drugName;
    EditText drugAmount;
    EditText drugLeft;
    EditText n;
    Button nextButton;
    TextView mTakingTimeTV;
    String drugUnits;
    String takingPeriodType;
    DrugItem tDrugItem;
    long interval;
    NotificationManager nm;
    String prevName;
    Date tDate = new Date();
    public static final int REQUEST_TIME = 0;
    private static final String ARG_TASK_ID = "drug_id";
    public static boolean add = false;

    public static AddDrugFragment newInstance(UUID taskId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, taskId);
        AddDrugFragment fragment = new AddDrugFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID drugId = (UUID) getArguments().getSerializable(ARG_TASK_ID);
        for (int i = 0; i != sDrugItems.size(); i++) {
            if (drugId.equals(sDrugItems.get(i).getUUID())) {
                tDrugItem = sDrugItems.get(i);
                try {
                    prevName = sDrugItems.get(i).getDrugName();
                    tDate = sDrugItems.get(i).getNextTakingTime();
                } catch (Exception e) {
                }
            }
        }
        nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_drug, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) view.findViewById(R.id.add_drug_choose_drug_type_spinner);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        try {
            for (int i = 0; i != data.length; i++)
                if (tDrugItem.getDrugUnits().equals(data[i]))
                    spinner.setSelection(i);
        } catch (Exception e) {
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                drugUnits = data[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner1 = (Spinner) view.findViewById(R.id.add_drug_choose_drug_taking_interval_spinner);
        spinner1.setAdapter(adapter1);
        spinner1.setSelection(0);
        try {
            for (int i = 0; i != data1.length; i++)
                if (tDrugItem.getTakingPeriodType().equals(data1[i]))
                    spinner.setSelection(i);
        } catch (Exception e1) {
        }
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                takingPeriodType = data1[position];
                switch (position) {
                    case 0:
                        interval = 1;
                        break;
                    case 1:
                        interval = 24;
                        break;
                    case 2:
                        interval = 7 * 24;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        nextButton = (Button) view.findViewById(R.id.add_drug_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExit();
            }

        });
        mTakingTimeTV = (TextView) view.findViewById(R.id.add_drug_taking_time_edit_text);
        updateTime();
        mTakingTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(tDate);
                dialog.setTargetFragment(AddDrugFragment.this, REQUEST_TIME);
                dialog.show(manager, "DialogDate");
                updateTime();
            }
        });

        drugName = (EditText) view.findViewById(R.id.add_drug_drug_name_edit_text);
        try {
            drugName.setText(tDrugItem.getDrugName());
        } catch (Exception e1) {
        }
        drugName.addTextChangedListener(this);
        drugAmount = (EditText) view.findViewById(R.id.add_drug_drug_amount_edit_text);
        try {
            drugAmount.setText(String.valueOf(tDrugItem.getTakingAmount()));
        } catch (Exception e2) {
        }
        drugAmount.addTextChangedListener(this);
        n = (EditText) view.findViewById(R.id.add_drug_n);
        try {
            n.setText(String.valueOf(tDrugItem.getTakingPeriod()));
        } catch (Exception e3) {
        }
        n.addTextChangedListener(this);
        drugLeft = (EditText) view.findViewById(R.id.add_drug_left_amount);
        try {
            drugLeft.setText(String.valueOf(tDrugItem.getDrugLeft()));
        } catch (Exception e4) {
        }
        drugLeft.addTextChangedListener(this);
        btnCheck();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            tDate = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            updateTime();
        }
    }

    private void updateTime() {
        mTakingTimeTV.setText(android.text.format.DateFormat.format("hh:mm", tDate));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        btnCheck();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_drug, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_current_item_menu:
                for (DrugItem drugItem : sDrugItems) {
                    if (tDrugItem.getUUID().equals(drugItem.getUUID())) {
                        Database dbHelper = new Database(getContext());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("DELETE FROM " + Database.TABLE_NAME + " WHERE " + Database.DRUG_NAME + "='" + drugItem.getDrugName() + "'");
                        sDrugItems.remove(drugItem);
                        Intent intent = new Intent(getActivity(), AlarmNotificationReciever.class);
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);
                        alarmManager.cancel(pendingIntent);
                        pendingIntent.cancel();
                        Intent inten = new Intent(getActivity(), DrugsListActivity.class);
                        startActivity(inten);
                    }
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void btnCheck() {
        if ((drugName.getText().toString().length() > 0) && (drugAmount.getText().toString().length() > 0)
                && (n.getText().toString().length() > 0) && (drugLeft.getText().toString().length() > 0)) {
            nextButton.setVisibility(View.VISIBLE);
        }
    }

    public void onExit() {
        Log.d(TAG, "createDrugItem: sDrugitems size:" + sDrugItems.size());
        for (DrugItem drugItem : sDrugItems) {
            if (tDrugItem.getUUID() == drugItem.getUUID()) {
                tDrugItem.setDrugName(drugName.getText().toString());
                tDrugItem.setNextTakingTime(tDate);
                tDrugItem.setDrugUnits(drugUnits);
                tDrugItem.setDrugLeft(Double.parseDouble(drugLeft.getText().toString()));
                tDrugItem.setTakingAmount(Double.parseDouble(drugAmount.getText().toString()));
                tDrugItem.setTakingPeriodType(takingPeriodType);
                tDrugItem.setTakingPeriod(Integer.parseInt(n.getText().toString()));
                if (add) {
                    Database database = new Database(getContext());
                    SQLiteDatabase db = database.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Database.DRUG_NAME, drugName.getText().toString());
                    contentValues.put(Database.DRUG_NEXT_TAKE_TIME, tDate.getTime());
                    contentValues.put(Database.DRUG_UNITS, drugUnits);
                    contentValues.put(Database.DRUG_LEFT, Double.parseDouble(drugLeft.getText().toString()));
                    contentValues.put(Database.DRUG_TAKING_AMOUNT, Double.parseDouble(drugAmount.getText().toString()));
                    contentValues.put(Database.DRUG_TAKING_PERIOD_TYPE, takingPeriodType);
                    contentValues.put(Database.DRUG_TAKING_PERIOD, Integer.parseInt(n.getText().toString()));
                    db.insert(Database.TABLE_NAME, null, contentValues);
                    database.close();
                    add = false;
                } else {
                    Database database = new Database(getContext());
                    SQLiteDatabase db = database.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Database.DRUG_NAME, drugName.getText().toString());
                    contentValues.put(Database.DRUG_NEXT_TAKE_TIME, tDate.getTime());
                    contentValues.put(Database.DRUG_UNITS, drugUnits);
                    contentValues.put(Database.DRUG_LEFT, Double.parseDouble(drugLeft.getText().toString()));
                    contentValues.put(Database.DRUG_TAKING_AMOUNT, Double.parseDouble(drugAmount.getText().toString()));
                    contentValues.put(Database.DRUG_TAKING_PERIOD_TYPE, takingPeriodType);
                    contentValues.put(Database.DRUG_TAKING_PERIOD, Integer.parseInt(n.getText().toString()));
                    db.update(Database.TABLE_NAME, contentValues, Database.DRUG_NAME + " = ?", new String[]{prevName});
                    database.close();
                }
                Intent notificationIntent = new Intent(getActivity(), AlarmNotificationReciever.class);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, notificationIntent, 0);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(tDate);
                calendar.set(Calendar.SECOND, 0);
                alarmManager.cancel(pendingIntent);
                Log.d(TAG, "onClick: " + calendar.getTime() + " ms:" + calendar.getTimeInMillis());
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), (Integer.parseInt(n.getText().toString()) * interval * AlarmManager.INTERVAL_HOUR), pendingIntent);
            }
            getActivity().finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onExit();
    }
}
