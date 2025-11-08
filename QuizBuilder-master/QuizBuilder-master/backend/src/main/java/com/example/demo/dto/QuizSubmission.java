package com.example.demo.dto;

import java.util.Map;

public class QuizSubmission {
    private String username;              // participant
    private Map<Long, String> answers;    // questionId → selectedAnswer

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Map<Long, String> getAnswers() { return answers; }
    public void setAnswers(Map<Long, String> answers) { this.answers = answers; }
}
