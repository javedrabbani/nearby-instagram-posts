package io.instag.nearbyposts.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.instag.nearbyposts.model.data.Location;
import io.instag.nearbyposts.model.data.LocationData;

/**
 * Created by javed on 20/08/2017.
 */

public class LocationAdapter extends ArrayAdapter<LocationData> {

    private Context mContext;
    private List<LocationData> mLocationDataList = new ArrayList<>();

    public LocationAdapter(Context context,
                           int textViewResourceId,
                           List<LocationData> locationDataList) {

        super(context, textViewResourceId, locationDataList);

        this.mContext = context;
        this.mLocationDataList = locationDataList;
    }

    public int getCount(){
        return mLocationDataList.size();
    }

    public LocationData getItem(int position){
        return mLocationDataList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(mContext);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setText(mLocationDataList.get(position).getName());

        return view;
    }

    //View of Spinner on dropdown Popping

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView view = new TextView(mContext);

        view.setTextColor(Color.BLACK);
        view.setText(mLocationDataList.get(position).getName());
        view.setHeight(120);

        return view;
    }
}