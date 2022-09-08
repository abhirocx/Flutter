package com.np.namasteyoga.datasource.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {
    @SerializedName("user")
    @Expose
    private LoggedUser user;
    @SerializedName("token")
    @Expose
    private String token;

    public LoggedUser getUser() {
        return user;
    }

    public void setUser(LoggedUser user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
