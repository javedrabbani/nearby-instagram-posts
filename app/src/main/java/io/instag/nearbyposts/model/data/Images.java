package io.instag.nearbyposts.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.instag.nearbyposts.model.data.images.LowResolution;
import io.instag.nearbyposts.model.data.images.StandardResolution;
import io.instag.nearbyposts.model.data.images.Thumbnail;

/**
 * Created by javed on 19/08/2017.
 */

public class Images {

    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;
    @SerializedName("low_resolution")
    @Expose
    private LowResolution lowResolution;
    @SerializedName("standard_resolution")
    @Expose
    private StandardResolution standardResolution;

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public LowResolution getLowResolution() {
        return lowResolution;
    }

    public void setLowResolution(LowResolution lowResolution) {
        this.lowResolution = lowResolution;
    }

    public StandardResolution getStandardResolution() {
        return standardResolution;
    }

    public void setStandardResolution(StandardResolution standardResolution) {
        this.standardResolution = standardResolution;
    }

}
