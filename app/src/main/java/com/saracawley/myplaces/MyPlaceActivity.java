package com.saracawley.myplaces;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.UUID;

/**
 * Created by sara on 4/26/2016.
 */
public class MyPlaceActivity extends SingleFragmentActivity {
    private static final String EXTRA_PLACE_ID = "com.saracawley.android.myplace.place_id";

    private static final int REQUEST_ERROR = 0;

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

    @Override
    public void onResume() {
        super.onResume();
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if(errorCode != ConnectionResult.SUCCESS){
            Dialog errorDialog = googleApiAvailability.getErrorDialog(this, errorCode,  REQUEST_ERROR,
                    new DialogInterface.OnCancelListener(){
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }
                    });
            errorDialog.show();
        }
    }
}
