package com.np.namasteyoga.datasource.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreateEventRequest implements Serializable {

    @SerializedName("event_id")
    @Expose
    private Integer event_id;
    @SerializedName("event_name")
    @Expose
    private String eventName;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("contact_person")
    @Expose
    private String contactPerson;
    @SerializedName("contact_no")
    @Expose
    private String contactNo;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("sitting_capacity")
    @Expose
    private Integer sittingCapacity;
    @SerializedName("zip")
    @Expose
    private String zip;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lng")
    @Expose
    private double lng;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;

    @SerializedName("isHighlight")
    @Expose
    private Integer isHighlight;

    @SerializedName("joining_instruction")
    @Expose
    private String joining_instruction;

    @SerializedName("short_description")
    @Expose
    private String short_description;


    @SerializedName("nearest")
    @Expose
    private int nearest;

    @SerializedName("nearest_distance")
    @Expose
    private String nearest_distance;

    @SerializedName("meeting_link")
    @Expose
    private String meeting_link;


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getSittingCapacity() {
        return sittingCapacity;
    }

    public void setSittingCapacity(Integer sittingCapacity) {
        this.sittingCapacity = sittingCapacity;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getEvent_id() {
        return event_id;
    }

    public void setEvent_id(Integer event_id) {
        this.event_id = event_id;
    }

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

    public String getJoining_instruction() {
        return joining_instruction;
    }

    public void setJoining_instruction(String joining_instruction) {
        this.joining_instruction = joining_instruction;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public Integer getIsHighlight() {
        return isHighlight;
    }

    public void setIsHighlight(Integer isHighlight) {
        this.isHighlight = isHighlight;
    }

    public String getMeeting_link() {
        return meeting_link;
    }

    public void setMeeting_link(String meeting_link) {
        this.meeting_link = meeting_link;
    }
}

