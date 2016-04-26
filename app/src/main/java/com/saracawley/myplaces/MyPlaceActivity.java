package com.saracawley.myplaces;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by sara on 4/26/2016.
 */
public class MyPlaceActivity extends SingleFragmentActivity {
    private static final String EXTRA_PLACE_ID = "com.saracawley.android.myplace.place_id";

    public static Intent newIntent(Context packageContext, UUID placeID){
        Intent intent = new Intent(packageContext, MyPlaceActivity.class);
        intent.putExtra(EXTRA_PLACE_ID, placeID);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        //return new MyPlaceFragment();
        UUID placeId = (UUID) getIntent().getSerializableExtra(EXTRA_PLACE_ID);
        return MyPlaceFragment.newInstance(placeId);
    }

}
