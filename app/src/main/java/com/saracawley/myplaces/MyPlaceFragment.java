package com.saracawley.myplaces;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by sara on 4/26/2016.
 */
public class MyPlaceFragment extends Fragment {

    private static final String TAG = "MyPlaceFragment";
    private static final String ARG_PLACE_ID = "place_id";
    private static final int REQUEST_PHOTO = 2;
    private Place mPlace;
    private File mPhotoFile;
    private TextView mNameTextView;
    private ImageButton mImageButton;
    private ImageView mPhotoView;

    private GoogleApiClient mClient;


    public static MyPlaceFragment newInstance(UUID placeID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLACE_ID, placeID);

        MyPlaceFragment fragment = new MyPlaceFragment();
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

        UUID place_id = (UUID) getArguments().getSerializable(ARG_PLACE_ID);
        mPlace = PlaceManger.get(getActivity()).getPlace(place_id);
        mPhotoFile = PlaceManger.get(getActivity()).getPhotoFile(mPlace);
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
    public void onPause() {
        super.onPause();

        PlaceManger.get(getActivity()).updatePlace(mPlace);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_place, container, false);

        mNameTextView = (TextView) v.findViewById(R.id.name);
        mImageButton = (ImageButton) v.findViewById(R.id.camera);


        PackageManager packageManager = getActivity().getPackageManager();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mImageButton.setEnabled(canTakePhoto);
        if (canTakePhoto) {
            Log.i("MyPlaceFragment", " can take photo");
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.image);
        updatePhotoView();
        String exif = ReadExif(mPhotoFile.getPath());
        Log.i(TAG, exif);

        mNameTextView.setText(mPlace.getName());
        mNameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPlace.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }

    String ReadExif(String file) {
        String exif = "exif: " + file;

        try {
            ExifInterface exifInterface = new ExifInterface(file);
            exif += "Image Lat" + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            exif += "Image Lon " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        } catch (IOException e) {
            Log.e("MyPlaceFragment", "ReadExif " + e);
        }
        return exif;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_PHOTO) {
            getImageLatLon();
            updatePhotoView();
        }
    }
    void getImageLatLon(){
        try {
            ExifInterface exifInterface = new ExifInterface(mPhotoFile.getPath());
            float[] latLong = new float[2];
            boolean hasLatLong = exifInterface.getLatLong(latLong);
            if (hasLatLong) {
                mPlace.setLat(latLong[0]);
                mPlace.setLon(latLong[1]);
                Log.i(TAG,"Latitude: " + latLong[0]);
                Log.i(TAG,"Longitude: " + latLong[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // set lat and lon in mPlace
        String exif = ReadExif(mPhotoFile.getPath());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_place, menu);

        MenuItem searchItem = menu.findItem(R.id.action_locate);
        searchItem.setEnabled(mClient.isConnected());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_place:
                PlaceManger.get(getActivity()).deletePlace(mPlace);
                getActivity().finish();
                return true;
            case R.id.action_locate:
                findMe();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                        Toast.makeText(getActivity(),"Got a fix:" + location, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Got a fix:" + location);
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

    private void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
