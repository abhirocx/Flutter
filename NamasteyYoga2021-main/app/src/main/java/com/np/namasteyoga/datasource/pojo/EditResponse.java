package com.np.namasteyoga.datasource.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.np.namasteyoga.datasource.response.UserDetail;

import java.io.Serializable;

public class EditResponse implements Serializable {
    @SerializedName("status")
    @Expose
//    private String status;
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private UserDetail data;

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

    public UserDetail getData() {
        return data;
    }

    public void setData(UserDetail data) {
        this.data = data;
    }
}
