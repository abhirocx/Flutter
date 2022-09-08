package com.np.namasteyoga.datasource.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseQuizValidate {
    @SerializedName("status")
    @Expose
    public String status;


    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("data")
    @Expose
    public QuizResultData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public QuizResultData getData() {
        return data;
    }

    public void setData(QuizResultData data) {
        this.data = data;
    }
}
