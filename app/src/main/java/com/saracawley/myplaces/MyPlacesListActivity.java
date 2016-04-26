package com.saracawley.myplaces;

import android.support.v4.app.Fragment;

public class MyPlacesListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MyPlaceListFragment();
    }

}
