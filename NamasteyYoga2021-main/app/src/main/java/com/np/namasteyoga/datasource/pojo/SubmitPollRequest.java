package com.np.namasteyoga.datasource.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmitPollRequest {

    @SerializedName("data")
    @Expose
    private PollData data;

    public PollData getData() {
        return data;
    }

    public void setData(PollData data) {
        this.data = data;
    }


}
