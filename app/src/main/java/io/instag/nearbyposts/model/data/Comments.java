package io.instag.nearbyposts.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by javed on 19/08/2017.
 */

public class Comments {

    @SerializedName("count")
    @Expose
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
