package com.np.namasteyoga.datasource.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EditRequest implements Serializable {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("role_id")
    @Expose
    private Integer roleId;
    @SerializedName("sitting_capacity")
    @Expose
    private Integer sitting_capacity;
    @SerializedName("zip")
    @Expose
    private String zip;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    @SerializedName("user_type")
    @Expose
    private String user_type;

    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;


    @SerializedName("nearest")
    @Expose
    private int nearest;

    @SerializedName("nearest_distance")
    @Expose
    private String nearest_distance;

    public int getNearest() {
        return nearest;
    }

    public void setNearest(int nearest) {
        this.nearest = nearest;
    }

    public String getNearest_distance() {
        return nearest_distance;
    }

    public void setNearest_distance(String nearest_distance) {
        this.nearest_distance = nearest_distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Integer getSitting_capacity() {
        return sitting_capacity;
    }

    public void setSitting_capacity(Integer sitting_capacity) {
        this.sitting_capacity = sitting_capacity;
    }
}
