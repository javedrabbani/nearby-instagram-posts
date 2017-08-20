package io.instag.nearbyposts.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by javed on 20/08/2017.
 */

public class LocationAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> locationList;

    public LocationAdapter(Context context,
                           int textViewResourceId,
                           List<String> values) {

        super(context, textViewResourceId, values);

        this.context = context;
        this.locationList = values;
    }

    public int getCount(){
        return locationList.size();
    }

    public String getItem(int position){
        return locationList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setText(locationList.get(position));

        return view;
    }

    //View of Spinner on dropdown Popping

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView view = new TextView(context);

        view.setTextColor(Color.BLACK);
        view.setText(locationList.get(position));
        view.setHeight(120);

        return view;
    }
}