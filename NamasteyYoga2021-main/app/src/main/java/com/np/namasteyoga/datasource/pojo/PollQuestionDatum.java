package com.np.namasteyoga.datasource.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PollQuestionDatum {

    @SerializedName("que_id")
    @Expose
    private Integer queId;
    @SerializedName("ans_id")
    @Expose
    private Integer ansId;

    public Integer getQueId() {
        return queId;
    }

    public void setQueId(Integer queId) {
        this.queId = queId;
    }

    public Integer getAnsId() {
        return ansId;
    }

    public void setAnsId(Integer ansId) {
        this.ansId = ansId;
    }

}
