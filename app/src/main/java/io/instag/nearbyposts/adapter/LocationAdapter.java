package io.instag.nearbyposts.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.instag.nearbyposts.R;
import io.instag.nearbyposts.model.data.Location;
import io.instag.nearbyposts.model.data.LocationData;

/**
 * Created by javed on 20/08/2017.
 */

public class LocationAdapter extends ArrayAdapter<LocationData> {

    private Context mContext;
    private List<LocationData> mLocationDataList = new ArrayList<>();
    private LayoutInflater mInflater;
    private int mLayoutResourceId;

    public LocationAdapter(Context context,
                           int layoutResourceId,
                           List<LocationData> locationDataList) {

        super(context, layoutResourceId, locationDataList);

        this.mContext = context;
        this.mLocationDataList = locationDataList;

        this.mInflater = LayoutInflater.from(mContext);

        this.mLayoutResourceId = layoutResourceId;
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
        TextView view;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutResourceId, parent, false);

            view = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(view);
        } else {
            view = (TextView) convertView.getTag();
        }

        if (view != null) {
            view.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            view.setGravity(Gravity.CENTER);
            view.setText(mLocationDataList.get(position).getName());
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView view;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutResourceId, parent, false);

            view = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(view);
        } else {
            view = (TextView) convertView.getTag();
        }

        if (view != null) {
            view.setGravity(Gravity.CENTER);
            view.setText(mLocationDataList.get(position).getName());
        }

        return view;
    }
}