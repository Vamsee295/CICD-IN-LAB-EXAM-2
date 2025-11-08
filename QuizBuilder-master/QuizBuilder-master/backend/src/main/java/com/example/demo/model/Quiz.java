package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "quizzes")
public class Quiz {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String domain;

  @NotBlank
  private String title;
  @NotBlank
  private String createdBy; 

  @Min(1)
  private int timeLimit; // minutes

  @OneToMany(
      mappedBy = "quiz",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER
  )
  @JsonManagedReference 
  private List<Question> questions = new ArrayList<>();

  // helpers
  public void addQuestion(Question q) {
    q.setQuiz(this);
    this.questions.add(q);
  }

  public void removeQuestion(Question q) {
    q.setQuiz(null);
    this.questions.remove(q);
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
  public List<Question> getQuestions() { return questions; }
  public void setQuestions(List<Question> questions) { this.questions = questions; }
 // e.g. username or email of creator

  public String getCreatedBy() { return createdBy; }
  public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

}
