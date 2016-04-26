package com.saracawley.myplaces;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by sara on 4/26/2016.
 */
public class MyPlaceFragment extends Fragment {
    private static final String ARG_PLACE_ID = "place_id";
    private Place mPlace;
    private TextView mNameTextView;

    public static MyPlaceFragment newInstance(UUID placeID){
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
        UUID place_id = (UUID) getArguments().getSerializable(ARG_PLACE_ID);
        mPlace = PlaceManger.get(getActivity()).getPlace(place_id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_place,container,false);
        mNameTextView = (TextView)v.findViewById(R.id.name);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_place, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_item_delete_place:
                PlaceManger.get(getActivity()).deletePlace(mPlace);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
