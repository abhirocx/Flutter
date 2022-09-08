package com.np.namasteyoga.datasource.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizResultData {
    @SerializedName("marks_obtained")
    @Expose
    public Integer marksObtained;

    @SerializedName("total_marks")
    @Expose
    public Integer totalMarks;

    @SerializedName("response_date")
    @Expose
    public String responseDate;

    public Integer getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(Integer marksObtained) {
        this.marksObtained = marksObtained;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(String responseDate) {
        this.responseDate = responseDate;
    }
}
