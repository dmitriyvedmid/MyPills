package com.dmitriyvedmid.mypills.DrugsIntent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmitriyvedmid.mypills.AddDrug.AddDrugActivity;
import com.dmitriyvedmid.mypills.AddDrug.AddDrugFragment;
import com.dmitriyvedmid.mypills.CalendarActivity.CalendarActivity;
import com.dmitriyvedmid.mypills.DrugItem;
import com.dmitriyvedmid.mypills.R;

import java.util.List;

import static com.dmitriyvedmid.mypills.MainActivity.sDrugItems;


/**
 * Created by dmitr on 4/22/2017.
 */

public class DrugsListFragment extends Fragment {
    public static final int REQUEST_TASK = 1;
    public static final String TAG = "My";
    private RecyclerView mDrugRecyclerView;

    private DrugAdapter mAdapter;
    TextView noDataTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstantState) {
        View view = inflater.inflate(R.layout.fragment_drug_list, container, false);
        mDrugRecyclerView = (RecyclerView) view.findViewById(R.id.task_recycler_view);
        mDrugRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        noDataTextView = (TextView) view.findViewById(R.id.empty_list_item);
        noDataTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.empty_list_item)
                    createDrugItem();
            }
        });
        updateUI();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void createDrugItem() {
        DrugItem drugItem = new DrugItem();
        sDrugItems.add(drugItem);
        AddDrugFragment.add = true;
        Log.d(TAG, "createDrugItem: sDrugitems size:"+sDrugItems.size());
        Intent intent = AddDrugActivity.newIntent(getActivity(), drugItem.getUUID());
        startActivity(intent);
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new DrugAdapter(sDrugItems);
            mDrugRecyclerView.setAdapter(mAdapter);
        } else mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.calendar_menu_item:
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //////////////////////////////////////////////////
    private class DrugHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mDrugNameTextView;
        private TextView mDrugTakingIntervalTextView;
        private TextView mListItemNextAlarmTextView;

        private DrugItem mDrug;

        public DrugHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mDrugNameTextView = (TextView) itemView.findViewById(R.id.drug_list_item_drug_name);
            mDrugTakingIntervalTextView = (TextView) itemView.findViewById(R.id.drug_list_item_taking_interval);
            mListItemNextAlarmTextView = (TextView) itemView.findViewById(R.id.list_item_next_alarm_tv);

        }

        public void bindTask(DrugItem drug) {
            try {
                mDrug = drug;
                mDrugNameTextView.setText(mDrug.getDrugName());
                switch (mDrug.getTakingPeriodType()) {
                    case "Раз на N годин":
                        mDrugTakingIntervalTextView.setText("Раз на " + mDrug.getTakingPeriod() + " годин");
                        break;
                    case "Раз на N днів":
                        mDrugTakingIntervalTextView.setText("Раз на " + mDrug.getTakingPeriod() + " днів");
                        break;
                    case "Раз на N тижнів":
                        mDrugTakingIntervalTextView.setText("Раз на " + mDrug.getTakingPeriod() + " тижнів");
                        break;
                }
                mListItemNextAlarmTextView.setText(mDrug.getNextTakingTimeString());
            } catch (Exception e) {
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = AddDrugActivity.newIntent(getActivity(), mDrug.getUUID());
            startActivityForResult(intent, REQUEST_TASK);
        }
    }

    //////////////////////////////////////////////////
    private class DrugAdapter extends RecyclerView.Adapter<DrugHolder> {
        private List<DrugItem> mDrugs;

        public DrugAdapter(List<DrugItem> drugs) {
            mDrugs = drugs;
        }

        @Override
        public DrugHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view;
            view = layoutInflater.inflate(R.layout.list_item_drug, parent, false);
            return new DrugHolder(view);
        }

        @Override
        public void onBindViewHolder(DrugHolder holder, int position) {
            if (sDrugItems != null) {
                DrugItem drug = mDrugs.get(position);
                holder.bindTask(drug);
            }
        }

        @Override
        public int getItemCount() {
            return mDrugs.size();
        }
    }
}