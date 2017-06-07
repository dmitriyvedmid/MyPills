package com.dmitriyvedmid.mypills.AddDrug;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dmitriyvedmid.mypills.R;

import java.util.UUID;

/**
 * Created by dmitr on 6/5/2017.
 */

public class AddDrugActivity extends AppCompatActivity {

    private static final String EXTRA_DRUG_ID = "com.dmitriyvedmid.mypills.drug_id";

    public static Intent newIntent(Context packageContext, UUID taskId){
        Intent intent = new Intent(packageContext, AddDrugActivity.class);
        intent.putExtra(EXTRA_DRUG_ID, taskId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug);
        UUID id = (UUID)getIntent().getSerializableExtra(EXTRA_DRUG_ID);
        if (savedInstanceState == null) {
            AddDrugFragment mFragment = AddDrugFragment.newInstance(id);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.add_drug_activity, mFragment);
            ft.commit();
        }

    }
}
