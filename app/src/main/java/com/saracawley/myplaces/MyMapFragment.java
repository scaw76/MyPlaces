package com.saracawley.myplaces;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.UUID;

/**
 * Created by sara on 4/28/2016.
 */
public class MyMapFragment extends SupportMapFragment{
    private static final String TAG = "MyMapFragment";
    private static final String ARG_MAP_PLACE_ID = "place_id";
    private GoogleMap mMap;
    private Place mPlace;
    private Location mCurrentLocation;
    private Location mPlaceLocation;

    private GoogleApiClient mClient;

    public static MyMapFragment newInstance(UUID placeID) {
        //return new MyMapFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MAP_PLACE_ID, placeID);

        MyMapFragment fragment = new MyMapFragment();
        fragment.setArguments(args);
        return fragment;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        getActivity().invalidateOptionsMenu();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                updateUI();
            }
        });
        UUID place_id = (UUID) getArguments().getSerializable(ARG_MAP_PLACE_ID);
        mPlace = PlaceManger.get(getActivity()).getPlace(place_id);
        Log.d(TAG, "get my Place lat lon "+ mPlace.getLat() +" "+ mPlace.getLon());
    }
    @Override
    public void onStart() {
        super.onStart();
        getActivity().invalidateOptionsMenu();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_place, menu);

        MenuItem searchItem = menu.findItem(R.id.action_locate);
        searchItem.setEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_locate:
                Log.i(TAG,"pushed: action locate ");
                findMe();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateUI(){
       // mPlaceLocation.setLatitude(mPlace.getLat());
        //mPlaceLocation.setLongitude(mPlace.getLon());
        if(mMap == null || mCurrentLocation == null ){//||mPlace.getLat()==0|| mPlace.getLon()==0){ //|| mPlaceLocation == null){
            return;
        }
        LatLng itemPoint = new LatLng(Double.parseDouble(mPlace.getLat()), Double.parseDouble(mPlace.getLon()));

        LatLng myPoint = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        MarkerOptions itemMarker = new MarkerOptions()
                .position(itemPoint);
        MarkerOptions myMarker = new MarkerOptions()
                .position(myPoint);

        mMap.clear();
        mMap.addMarker(itemMarker);
        mMap.addMarker(myMarker);

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(itemPoint)
                .include(myPoint)
                .build();
        int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds,margin);
        mMap.animateCamera(update);
    }

    private void findMe() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },0);
            return;
        }
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mClient, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        //Toast.makeText(getActivity(),"Got a fix:" + location, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),"Got a fix:" + location, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Got a fix:" + location);
                        //mCurrentLocation.set(location);
                        new SearchTask().execute(location);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findMe();
            } else {
                Toast.makeText(getActivity(), "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SearchTask extends AsyncTask<Location, Void, Void> {
        private Location mLocation;

        @Override
        protected Void doInBackground(Location... params) {
            mLocation = params[0];

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mCurrentLocation = mLocation;
            updateUI();
        }
    }

}
