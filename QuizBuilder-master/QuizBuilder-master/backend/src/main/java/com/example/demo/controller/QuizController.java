package com.example.demo.controller;

import com.example.demo.dto.CreateQuizRequest;
import com.example.demo.dto.QuizResponse;
import com.example.demo.dto.QuizSubmission;
import com.example.demo.dto.ScoreResponse;
import com.example.demo.model.Question;
import com.example.demo.model.Quiz;
import com.example.demo.model.QuizResult;
import com.example.demo.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "*")
public class QuizController {

  private final QuizService quizService;

  public QuizController(QuizService quizService) {
    this.quizService = quizService;
  }

  // ✅ Create quiz
  @PostMapping
  public ResponseEntity<QuizResponse> createQuiz(@RequestBody CreateQuizRequest req) {
    Quiz quiz = new Quiz();
    quiz.setDomain(req.getDomain());
    quiz.setTitle(req.getTitle());
    quiz.setTimeLimit(req.getTimeLimit());
    quiz.setCreatedBy(req.getCreatedBy()); // 👈 set creator

    for (CreateQuizRequest.QuestionDto qdto : req.getQuestions()) {
      Question q = new Question();
      q.setText(qdto.getText());
      q.setOptions(qdto.getOptions());
      q.setAnswer(qdto.getAnswer());
      quiz.addQuestion(q);
    }

    Quiz saved = quizService.createQuiz(req);   // ✅ delegate to service
    return ResponseEntity.ok(quizService.toResponse(saved));
  }

  // ✅ Read all quizzes
  @GetMapping
  public List<QuizResponse> getAll() {
    return quizService.getAll().stream()
        .map(quizService::toResponse)
        .toList();
  }

  // ✅ Read one quiz
  @GetMapping("/{id}")
  public QuizResponse getOne(@PathVariable Long id) {
    return quizService.toResponse(quizService.getById(id));
  }

  // ✅ Get quizzes by domain
  @GetMapping("/domain/{domain}")
  public List<Quiz> byDomain(@PathVariable String domain) {
    return quizService.getByDomain(domain);
  }

  // ✅ Update quiz
  @PutMapping("/{id}")
  public Quiz update(@PathVariable Long id, @Valid @RequestBody CreateQuizRequest req) {
    return quizService.updateQuiz(id, req);
  }

  // ✅ Delete quiz
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    quizService.deleteQuiz(id);
  }

  // ✅ Submit answers & get score (also saves result in DB)
//✅ Submit answers & get score (also saves result in DB)
@PostMapping("/{id}/submit")
public ScoreResponse submit(@PathVariable Long id, @RequestBody QuizSubmission submission) {
   System.out.println("📥 Received submission: username=" + submission.getUsername());
   System.out.println("📥 Answers=" + submission.getAnswers());
   return quizService.score(id, submission);
}


  // ✅ Get quizzes created by specific user
  @GetMapping("/myquizzes/{username}")
  public List<QuizResponse> getMyQuizzes(@PathVariable String username) {
    return quizService.getMyQuizzes(username);
  }

//  // ✅ Get results by user
//  @GetMapping("/results/user/{username}")
//  public List<QuizResult> getResultsByUser(@PathVariable String username) {
//    return quizService.getResultsByUser(username);
//  }
//
//  // ✅ Get results by quiz
//  @GetMapping("/results/quiz/{quizId}")
//  public List<QuizResult> getResultsByQuiz(@PathVariable Long quizId) {
//    return quizService.getResultsByQuiz(quizId);
//  }
}
