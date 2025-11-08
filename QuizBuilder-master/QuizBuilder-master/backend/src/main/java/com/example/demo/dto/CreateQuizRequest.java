package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class CreateQuizRequest {

  @NotBlank
  private String domain;

  @NotBlank
  private String title;

  @Min(1)
  private int timeLimit;

  @NotEmpty
  private List<QuestionDto> questions;

  public static class QuestionDto {
    @NotBlank
    private String text;

    @NotEmpty
    private List<String> options; // must be 4 in UI

    @NotBlank
    private String answer; // "A","B","C","D"

    // getters/setters
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
  }

  // getters/setters
  public String getDomain() { return domain; }
  public void setDomain(String domain) { this.domain = domain; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public int getTimeLimit() { return timeLimit; }
  public void setTimeLimit(int timeLimit) { this.timeLimit = timeLimit; }
  public List<QuestionDto> getQuestions() { return questions; }
  public void setQuestions(List<QuestionDto> questions) { this.questions = questions; }
  @NotBlank
  private String createdBy;

  public String getCreatedBy() { return createdBy; }
  public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

}
