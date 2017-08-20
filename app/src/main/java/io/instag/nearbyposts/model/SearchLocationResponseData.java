package io.instag.nearbyposts.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.instag.nearbyposts.model.data.LocationData;

/**
 * Created by javed on 20/08/2017.
 */

public class SearchLocationResponseData {

    @SerializedName("data")
    @Expose
    private List<LocationData> locationData = null;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public List<LocationData> getLocationData() {
        return locationData;
    }

    public void setLocationData(List<LocationData> locationData) {
        this.locationData = locationData;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "meta = " + meta.toString() + ", location data = " + locationData.toString();
    }
}





