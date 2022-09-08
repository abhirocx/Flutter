package com.np.namasteyoga.datasource.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Event implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("event_name")
    @Expose
    private String eventName;
    @SerializedName("user_id")
    @Expose
    private String userId;
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
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("sitting_capacity")
    @Expose
    private String sittingCapacity;
    @SerializedName("zip")
    @Expose
    private String zip;
    @SerializedName("lat")
    @Expose
    private Float lat;
    @SerializedName("lng")
    @Expose
    private Float lng;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("state_name")
    @Expose
    private String stateName;

    @SerializedName("city_name")
    @Expose
    private String cityName;

    @SerializedName("short_description")
    @Expose
    private String short_description;

    @SerializedName("joining_instruction")
    @Expose
    private String joining_instruction;

    @SerializedName("rating")
    @Expose
    private float rating;

    @SerializedName("isHighlight")
    @Expose
    private String isHighlight;


    @SerializedName("nearest")
    @Expose
    private int nearest;

    @SerializedName("nearest_distance")
    @Expose
    private String nearest_distance;

    @SerializedName("videoId")
    @Expose
    private String videoId;

    @SerializedName("event_images")
    @Expose
    private List<String> event_images;


    @SerializedName("meeting_link")
    @Expose
    private String  meeting_link;


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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getSittingCapacity() {
        return sittingCapacity;
    }

    public void setSittingCapacity(String sittingCapacity) {
        this.sittingCapacity = sittingCapacity;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getJoining_instruction() {
        return joining_instruction;
    }

    public void setJoining_instruction(String joining_instruction) {
        this.joining_instruction = joining_instruction;
    }

    public String getIsHighlight() {
        return isHighlight;
    }

    public void setIsHighlight(String isHighlight) {
        this.isHighlight = isHighlight;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public List<String> getEvent_images() {
        return event_images;
    }

    public void setEvent_images(List<String> event_images) {
        this.event_images = event_images;
    }

    public String getMeeting_link() {
        return meeting_link;
    }

    public void setMeeting_link(String meeting_link) {
        this.meeting_link = meeting_link;
    }
}
