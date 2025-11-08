package com.example.demo.dto;

import java.time.LocalDateTime;

public class QuizResultDTO {
    private String username;
    private int score;
    private int total;
    private LocalDateTime submittedAt;
    private String quizTitle;
    private String quizDomain;

    public QuizResultDTO(String username, int score, int total,
                         LocalDateTime submittedAt, String quizTitle, String quizDomain) {
        this.username = username;
        this.score = score;
        this.total = total;
        this.submittedAt = submittedAt;
        this.quizTitle = quizTitle;
        this.quizDomain = quizDomain;
    }

    // Getters only
    public String getUsername() { return username; }
    public int getScore() { return score; }
    public int getTotal() { return total; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public String getQuizTitle() { return quizTitle; }
    public String getQuizDomain() { return quizDomain; }
}
