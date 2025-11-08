package com.example.demo.dto;

import java.util.List;

public class QuizResponse {
  private Long id;
  private String domain;
  private String title;
  private int timeLimit;
  private List<QuestionResponse> questions;

  public static class QuestionResponse {
    private Long id;
    private String text;
    private List<String> options;
    private String answer;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
  }

  // getters/setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getDomain() { return domain; }
  public void setDomain(String domain) { this.domain = domain; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public int getTimeLimit() { return timeLimit; }
  public void setTimeLimit(int timeLimit) { this.timeLimit = timeLimit; }
  public List<QuestionResponse> getQuestions() { return questions; }
  public void setQuestions(List<QuestionResponse> questions) { this.questions = questions; }
  private String createdBy;

  public String getCreatedBy() { return createdBy; }
  public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

}
