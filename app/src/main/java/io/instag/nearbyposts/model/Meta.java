package io.instag.nearbyposts.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by javed on 19/08/2017.
 */

public class Meta {

    @SerializedName("code")
    @Expose
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
