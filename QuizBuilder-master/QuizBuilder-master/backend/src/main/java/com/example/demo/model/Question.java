package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "questions")
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String text;

  @ElementCollection
  @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
  @Column(name = "option_text")
  @Size(min = 4, max = 4, message = "Must provide exactly 4 options")
  private List<String> options;

  /**
   * "A", "B", "C", or "D"
   */
  @NotBlank
  private String answer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "quiz_id")
  @JsonBackReference 
  private Quiz quiz;

  // getters/setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getText() { return text; }
  public void setText(String text) { this.text = text; }
  public List<String> getOptions() { return options; }
  public void setOptions(List<String> options) { this.options = options; }
  public String getAnswer() { return answer; }
  public void setAnswer(String answer) { this.answer = answer; }
  public Quiz getQuiz() { return quiz; }
  public void setQuiz(Quiz quiz) { this.quiz = quiz; }
}
