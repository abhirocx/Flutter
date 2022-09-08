package com.np.namasteyoga.datasource.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizData {
    @SerializedName("quiz_id")
    @Expose
    private Integer quizId;

    @SerializedName("quiz_responses_time")
    @Expose
    private Integer quiz_responses_time;

    @SerializedName("question_data")
    @Expose
    private List<PollQuestionDatum> questionData = null;

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public Integer getQuiz_responses_time() {
        return quiz_responses_time;
    }

    public void setQuiz_responses_time(Integer quiz_responses_time) {
        this.quiz_responses_time = quiz_responses_time;
    }

    public List<PollQuestionDatum> getQuestionData() {
        return questionData;
    }

    public void setQuestionData(List<PollQuestionDatum> questionData) {
        this.questionData = questionData;
    }

}


