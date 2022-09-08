package com.np.namasteyoga.datasource.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {

    @SerializedName("poll_id")
    @Expose
    private Integer pollId;
    @SerializedName("poll_name")
    @Expose
    private String pollName;

    @SerializedName("quiz_name")
    @Expose
    private String quiz_name;
    @SerializedName("quiz_id")
    @Expose
    private Integer quiz_id;
    @SerializedName("quiz_time")
    @Expose
    private Integer quiz_time;


    @SerializedName("question_data")
    @Expose
    private List<QuestionDatum> questionData = null;


    public Integer getPollId() {
        return pollId;
    }

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
    }

    public String getPollName() {
        return pollName;
    }

    public void setPollName(String pollName) {
        this.pollName = pollName;
    }

    public List<QuestionDatum> getQuestionData() {
        return questionData;
    }

    public void setQuestionData(List<QuestionDatum> questionData) {
        this.questionData = questionData;
    }

    public String getQuiz_name() {
        return quiz_name;
    }

    public void setQuiz_name(String quiz_name) {
        this.quiz_name = quiz_name;
    }

    public Integer getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(Integer quiz_id) {
        this.quiz_id = quiz_id;
    }

    public Integer getQuiz_time() {
        return quiz_time;
    }

    public void setQuiz_time(Integer quiz_time) {
        this.quiz_time = quiz_time;
    }
}
