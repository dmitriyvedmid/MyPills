package com.dmitriyvedmid.mypills;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dmitr on 5/31/2017.
 */

public class Database extends SQLiteOpenHelper {

    public static final String DB_NAME = "pills_db";
    public static final String TABLE_NAME = "table_pills";
    public static final String DRUG_NAME = "drug_name";
    public static final String DRUG_UNITS = "drug_units";
    public static final String DRUG_TAKING_AMOUNT = "drug_taking_amount";
    public static final String DRUG_TAKING_PERIOD = "drug_taking_period";
    public static final String DRUG_TAKING_PERIOD_TYPE = "drug_taking_period_type";
    public static final String DRUG_NEXT_TAKE_TIME = "drug_next_take_time";
    public static final String DRUG_LEFT = "drug_left";


    static final int databaseVersion = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "( " + DRUG_NAME + " text," + DRUG_UNITS + " text," +
                DRUG_TAKING_AMOUNT + " real, " + DRUG_LEFT + " real," + DRUG_NEXT_TAKE_TIME + " real,"
                + DRUG_TAKING_PERIOD_TYPE + " text, " + DRUG_TAKING_PERIOD + " integer" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists  " + TABLE_NAME);
        onCreate(db);
    }
}
