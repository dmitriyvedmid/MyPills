package com.dmitriyvedmid.mypills;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.dmitriyvedmid.mypills.DrugsIntent.DrugsListActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    public static ArrayList<DrugItem> sDrugItems = new ArrayList<>();
    public static long day = 1000*60*60*24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        sDrugItems.clear();
        fillDrugItems();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), DrugsListActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);
    }

    public void fillDrugItems (){
        Database database = new Database(this);
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query(Database.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int drugNameIndex = cursor.getColumnIndex(Database.DRUG_NAME);
            int drugUnitsIndex = cursor.getColumnIndex(Database.DRUG_UNITS);
            int drugTakingAmount = cursor.getColumnIndex(Database.DRUG_TAKING_AMOUNT);
            int drugLeft = cursor.getColumnIndex(Database.DRUG_LEFT);
            int drugTakingTime = cursor.getColumnIndex(Database.DRUG_NEXT_TAKE_TIME);
            int drugPeriod = cursor.getColumnIndex(Database.DRUG_TAKING_PERIOD);
            int drugPeriodType = cursor.getColumnIndex(Database.DRUG_TAKING_PERIOD_TYPE);
            do {
                sDrugItems.add(new DrugItem(cursor.getString(drugNameIndex), cursor.getString(drugUnitsIndex),
                        cursor.getLong(drugTakingAmount), cursor.getInt(drugLeft),cursor.getInt(drugTakingTime),
                        cursor.getString(drugPeriodType), cursor.getInt(drugPeriod)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
    }
}
