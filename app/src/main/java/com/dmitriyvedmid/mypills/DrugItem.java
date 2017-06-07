package com.dmitriyvedmid.mypills;

import java.util.Date;
import java.util.UUID;

/**
 * Created by dmitr on 6/5/2017.
 */

public class DrugItem {

    private UUID mUUID;
    private String mDrugName;
    private String mDrugUnits;
    private double mTakingAmount;
    private double mDrugLeft;
    private Date mNextTakingTime = new Date();
    private String mTakingPeriodType;
    private int mTakingPeriod;

    public DrugItem(){
        mUUID = UUID.randomUUID();
    }

    public DrugItem(String drugName, String drugUnits, double takingAmount, double drugLeft,
                    Date nextTakingTime, String takingPeriodType, int takingPeriod) {
        mUUID = UUID.randomUUID();
        mDrugName = drugName;
        mDrugUnits = drugUnits;
        mTakingAmount = takingAmount;
        mDrugLeft = drugLeft;
        mNextTakingTime = nextTakingTime;
        mTakingPeriod = takingPeriod;
        mTakingPeriodType = takingPeriodType;
    }
    public DrugItem(String drugName, String drugUnits, double takingAmount, double drugLeft,
                    int nextTakingTime, String takingPeriodType, int takingPeriod) {
        mUUID = UUID.randomUUID();
        mDrugName = drugName;
        mDrugUnits = drugUnits;
        mTakingAmount = takingAmount;
        mDrugLeft = drugLeft;
        mNextTakingTime.setTime(nextTakingTime);
        mTakingPeriod = takingPeriod;
        mTakingPeriodType = takingPeriodType;
    }


    public String getNextTakingTimeString()
    {
        return (String) android.text.format.DateFormat.format("hh:mm", mNextTakingTime);
    }

    public UUID getUUID() {
        return mUUID;
    }

    public String getDrugName() {
        return mDrugName;
    }

    public void setDrugName(String drugName) {
        mDrugName = drugName;
    }

    public String getTakingPeriodType() {
        return mTakingPeriodType;
    }

    public void setTakingPeriodType(String takingPeriodType) {
        mTakingPeriodType = takingPeriodType;
    }

    public String getDrugUnits() {
        return mDrugUnits;
    }

    public void setDrugUnits(String drugUnits) {
        mDrugUnits = drugUnits;
    }

    public double getTakingAmount() {
        return mTakingAmount;
    }

    public void setTakingAmount(double takingAmount) {
        mTakingAmount = takingAmount;
    }

    public double getDrugLeft() {
        return mDrugLeft;
    }

    public void setDrugLeft(double drugLeft) {
        mDrugLeft = drugLeft;
    }

    public int getTakingPeriod() {
        return mTakingPeriod;
    }

    public void setTakingPeriod(int takingPeriod) {
        mTakingPeriod = takingPeriod;
    }

    public Date getNextTakingTime() {
        return mNextTakingTime;
    }

    public void setNextTakingTime(Date nextTakingTime) {
        mNextTakingTime = nextTakingTime;
    }
}
