package com.saracawley.myplaces;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by sara on 4/28/2016.
 */
public class MyMapActivity extends SingleFragmentActivity {
    private static final String EXTRA_PLACE_ID = "com.saracawley.android.myplace.map.place_id";

    //private GoogleApiClient mClient;

    public static Intent newIntent(Context packageContext, UUID placeID){
        Intent intent = new Intent(packageContext, MyMapActivity.class);
        intent.putExtra(EXTRA_PLACE_ID, placeID);
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        UUID placeId = (UUID) getIntent().getSerializableExtra(EXTRA_PLACE_ID);
        return MyMapFragment.newInstance(placeId);
    }

}
