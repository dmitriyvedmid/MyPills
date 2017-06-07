package com.dmitriyvedmid.mypills.DrugsIntent;

import android.support.v4.app.Fragment;

/**
 * Created by dmitr on 4/22/2017.
 */

public class DrugsListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new DrugsListFragment();
    }
}
