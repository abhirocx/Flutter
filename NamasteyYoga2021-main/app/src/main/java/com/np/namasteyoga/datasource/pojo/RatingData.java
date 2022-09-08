package com.np.namasteyoga.datasource.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RatingData implements Serializable {
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("out_of")
    @Expose
    private Integer outOf;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Integer getOutOf() {
        return outOf;
    }

    public void setOutOf(Integer outOf) {
        this.outOf = outOf;
    }
}
