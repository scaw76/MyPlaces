package com.saracawley.myplaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sara on 4/26/2016.
 */
public class MyPlaceListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private PlaceAdapter mPlaceAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_list,container,false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.chore_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_place_list,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_item_new_place:
                Place p = new Place();
                PlaceManger.get(getActivity()).addPlace(p);
                Intent i = MyPlaceActivity.newIntent(getActivity(),p.getID());
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void updateUI(){
        PlaceManger placeManger = PlaceManger.get(getActivity());
        List<Place> places = placeManger.getPlaces();
        if(mPlaceAdapter == null){
            mPlaceAdapter = new PlaceAdapter(places);
            mRecyclerView.setAdapter(mPlaceAdapter);
        }else{
            mPlaceAdapter.notifyDataSetChanged();
        }

    }
    private class PlaceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mNameTextView;
        private Place mPlace;

        public PlaceHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_name);
        }
        public void bindPlace(Place p){
            mPlace = p;
            mNameTextView.setText(mPlace.getName());
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(getActivity(), mPlace.getName() +" clicked!", Toast.LENGTH_SHORT).show();
            Intent intent = MyPlaceActivity.newIntent(getActivity(),mPlace.getID());
            startActivity(intent);
        }
    }
    private class PlaceAdapter extends RecyclerView.Adapter<PlaceHolder>{
        private List<Place> mPlaces;
        public PlaceAdapter(List<Place> places){
            mPlaces = places;
        }
        @Override
        public PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_item_place,parent,false);
            return new PlaceHolder(v);
        }

        @Override
        public void onBindViewHolder(PlaceHolder holder, int position) {
            Place p = mPlaces.get(position);
            holder.bindPlace(p);
        }

        @Override
        public int getItemCount() {
            return mPlaces.size();
        }
    }

}
