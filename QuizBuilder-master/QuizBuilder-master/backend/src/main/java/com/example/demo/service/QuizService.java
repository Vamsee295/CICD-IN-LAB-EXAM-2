package com.example.demo.service;

import com.example.demo.dto.CreateQuizRequest;
import com.example.demo.dto.QuizResponse;
import com.example.demo.dto.QuizSubmission;
import com.example.demo.dto.ScoreResponse;
import com.example.demo.model.Question;
import com.example.demo.model.Quiz;
import com.example.demo.model.QuizResult;
import com.example.demo.repository.QuizRepository;
import com.example.demo.repository.QuizResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizResultRepository resultRepository;

    private static final Logger log = LoggerFactory.getLogger(QuizService.class);

    public QuizService(QuizRepository quizRepository, QuizResultRepository resultRepository) {
        this.quizRepository = quizRepository;
        this.resultRepository = resultRepository;
    }

    // ✅ Create Quiz
    @Transactional
    public Quiz createQuiz(CreateQuizRequest req) {
        Quiz quiz = new Quiz();
        quiz.setDomain(req.getDomain());
        quiz.setTitle(req.getTitle());
        quiz.setTimeLimit(req.getTimeLimit());
        quiz.setCreatedBy(req.getCreatedBy());

        List<Question> qs = new ArrayList<>();
        for (CreateQuizRequest.QuestionDto qdto : req.getQuestions()) {
            Question q = new Question();
            q.setText(qdto.getText());
            q.setOptions(qdto.getOptions());
            q.setAnswer(qdto.getAnswer());
            q.setQuiz(quiz);
            qs.add(q);
        }
        quiz.setQuestions(qs);
        return quizRepository.save(quiz);
    }

    // ✅ Get all quizzes
    public List<Quiz> getAll() {
        return quizRepository.findAll();
    }

    // ✅ Get by ID
    public Quiz getById(Long id) {
        return quizRepository.findById(id).orElse(null);
    }

    // ✅ Get by domain
    public List<Quiz> getByDomain(String domain) {
        return quizRepository.findByDomainIgnoreCase(domain);
    }

    // ✅ Update quiz
    @Transactional
    public Quiz updateQuiz(Long id, CreateQuizRequest req) {
        Quiz existing = quizRepository.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setDomain(req.getDomain());
        existing.setTitle(req.getTitle());
        existing.setTimeLimit(req.getTimeLimit());

        // replace questions = simple approach
        existing.getQuestions().clear();
        List<Question> qs = new ArrayList<>();
        for (CreateQuizRequest.QuestionDto qdto : req.getQuestions()) {
            Question q = new Question();
            q.setText(qdto.getText());
            q.setOptions(qdto.getOptions());
            q.setAnswer(qdto.getAnswer());
            q.setQuiz(existing);
            qs.add(q);
        }
        existing.getQuestions().addAll(qs);
        return quizRepository.save(existing);
    }

    // ✅ Delete quiz
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    // ✅ Score quiz & Save result
    public ScoreResponse score(Long id, QuizSubmission submission) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if (quiz == null) return new ScoreResponse(0, 0);

        int score = 0;

        for (Question q : quiz.getQuestions()) {
            String submitted = submission.getAnswers().get(q.getId()); // ✅ Correctly fetch with Long key
            String correct = q.getAnswer();

            log.info("QID: {} | Submitted: {} | Correct: {}", q.getId(), submitted, correct);

            if (submitted != null && correct != null &&
                    correct.trim().equalsIgnoreCase(submitted.trim())) {
                score++;
                log.info("✅ Correct");
            } else {
                log.info("❌ Wrong");
            }
        }

        log.info("Final Score = {} / {}", score, quiz.getQuestions().size());

        // Save result
        QuizResult result = new QuizResult();
        result.setUsername(submission.getUsername());
        result.setQuiz(quiz);
        result.setScore(score);
        result.setTotal(quiz.getQuestions().size());
        resultRepository.save(result);

        return new ScoreResponse(score, quiz.getQuestions().size());
    }

    // ✅ Convert entity → DTO
    public QuizResponse toResponse(Quiz quiz) {
        QuizResponse dto = new QuizResponse();
        dto.setId(quiz.getId());
        dto.setDomain(quiz.getDomain());
        dto.setTitle(quiz.getTitle());
        dto.setTimeLimit(quiz.getTimeLimit());
        dto.setCreatedBy(quiz.getCreatedBy());

        List<QuizResponse.QuestionResponse> qDtos = quiz.getQuestions().stream().map(q -> {
            QuizResponse.QuestionResponse qr = new QuizResponse.QuestionResponse();
            qr.setId(q.getId());
            qr.setText(q.getText());
            qr.setOptions(q.getOptions());
            qr.setAnswer(q.getAnswer());
            return qr;
        }).collect(Collectors.toList());

        dto.setQuestions(qDtos);
        return dto;
    }

    // ✅ Get quizzes by creator
    public List<QuizResponse> getMyQuizzes(String username) {
        return quizRepository.findByCreatedBy(username)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    
}
