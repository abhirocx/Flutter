package com.np.namasteyoga.datasource.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResumeRequest implements Serializable {

    @SerializedName("email")
    @Expose
    private String email;
    private final static long serialVersionUID = 7883983543912184140L;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
