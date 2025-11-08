package com.example.demo.repository;

import com.example.demo.dto.QuizResultDTO;
import com.example.demo.model.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {

    @Query("SELECT new com.example.demo.dto.QuizResultDTO(r.username, r.score, r.total, r.submittedAt, q.title, q.domain) " +
           "FROM QuizResult r JOIN r.quiz q WHERE r.username = :username")
    List<QuizResultDTO> findResultsByUsername(@Param("username") String username);

    @Query("SELECT new com.example.demo.dto.QuizResultDTO(r.username, r.score, r.total, r.submittedAt, q.title, q.domain) " +
           "FROM QuizResult r JOIN r.quiz q WHERE q.id = :quizId")
    List<QuizResultDTO> findResultsByQuizId(@Param("quizId") Long quizId);
    @Query("SELECT new com.example.demo.dto.QuizResultDTO(r.username, r.score, r.total, r.submittedAt, q.title, q.domain) " +
    	       "FROM QuizResult r JOIN r.quiz q ORDER BY r.submittedAt DESC")
    	List<QuizResultDTO> findAllResultsWithQuiz();

}
