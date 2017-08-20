package io.instag.nearbyposts.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.instag.nearbyposts.model.data.Data;

import java.util.List;

/**
 * Created by javed on 19/08/2017.
 */

public class NearbyPostsResponseData {

    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}