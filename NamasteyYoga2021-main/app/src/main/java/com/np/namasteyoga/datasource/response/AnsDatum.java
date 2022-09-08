package com.np.namasteyoga.datasource.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnsDatum {

    @SerializedName("ans_id")
    @Expose
    private Integer ansId;
    @SerializedName("ans_text")
    @Expose
    private String ansText;


    public Integer getAnsId() {
        return ansId;
    }

    public void setAnsId(Integer ansId) {
        this.ansId = ansId;
    }

    public String getAnsText() {
        return ansText;
    }

    public void setAnsText(String ansText) {
        this.ansText = ansText;
    }

}
