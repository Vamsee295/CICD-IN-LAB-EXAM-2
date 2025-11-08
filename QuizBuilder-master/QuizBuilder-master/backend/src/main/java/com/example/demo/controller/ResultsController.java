package com.example.demo.controller;

import com.example.demo.dto.QuizResultDTO;
import com.example.demo.model.QuizResult;
import com.example.demo.repository.QuizResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/quizzes")
public class ResultsController {

    @Autowired
    private QuizResultRepository quizResultRepository;

    @GetMapping("/quiz-results/users/{username}")
    public List<QuizResultDTO> getResultsByUsername(@PathVariable String username) {
        return quizResultRepository.findResultsByUsername(username);
    }

    @GetMapping("/quiz-results/{quizId}")
    public List<QuizResultDTO> getResultsByQuiz(@PathVariable Long quizId) {
        return quizResultRepository.findResultsByQuizId(quizId);
    }
 // ✅ Get all results (Admin view)
    @GetMapping("/quiz-results")
    public List<QuizResultDTO> getAllResults() {
        return quizResultRepository.findAllResultsWithQuiz();
    }



}
