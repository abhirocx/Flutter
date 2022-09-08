package com.np.namasteyoga.datasource.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class City implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("city_name")
    @Expose
    private String city;

    @SerializedName("country_name")
    @Expose
    private String country_name;

    @SerializedName("state_name")
    @Expose
    private String state_name;

    @SerializedName("country_id")
    @Expose
    private String country_id;

    @SerializedName("city_id")
    @Expose
    private String city_id;

    @SerializedName("state_id")
    @Expose
    private String state_id;

    @SerializedName("lat")
    @Expose
    private String lat;

    @SerializedName("lng")
    @Expose
    private String lng;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return city;
    }

    public void setName(String name) {
        this.city = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
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

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", country_name='" + country_name + '\'' +
                ", state_name='" + state_name + '\'' +
                ", country_id='" + country_id + '\'' +
                ", city_id='" + city_id + '\'' +
                ", state_id='" + state_id + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                '}';
    }
}
