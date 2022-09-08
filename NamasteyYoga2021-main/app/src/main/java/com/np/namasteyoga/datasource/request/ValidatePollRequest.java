package com.np.namasteyoga.datasource.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ValidatePollRequest implements Serializable {
    @SerializedName("poll_id")
    @Expose
    private Integer poll_id;

    public Integer getPoll_id() {
        return poll_id;
    }

    public void setPoll_id(Integer poll_id) {
        this.poll_id = poll_id;
    }
}
