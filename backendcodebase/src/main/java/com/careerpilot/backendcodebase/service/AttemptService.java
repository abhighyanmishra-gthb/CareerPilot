package com.careerpilot.backendcodebase.service;

import com.careerpilot.backendcodebase.dtos.*;
import com.careerpilot.backendcodebase.entity.*;
import com.careerpilot.backendcodebase.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AttemptService {

    @Autowired
    private MockAttemptRepository mockAttemptRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionsRepository optionsRepository;

    @Transactional
    public Long startAttempt(StartAttemptDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        MockAttempt attempt = MockAttempt.builder() //creating the attempt object with all the required fields..
                .user(user)
                .status("IN_PROGRESS")
                .testType(dto.getTestType() != null ? dto.getTestType() : "GENERAL")
                .sourceName(dto.getSourceName() != null ? dto.getSourceName() : "PRACTICE")
                .startTime(LocalDateTime.now())
                .build();

        MockAttempt savedAttempt = mockAttemptRepository.save(attempt);// saved the attempt in MockAttempt db..
        return savedAttempt.getId();// returning id of the user..
    }

    @Transactional
    public AttemptResultResponseDTO submitAttempt(SubmitAttemptDTO submissionDTO) {
        MockAttempt attempt = mockAttemptRepository.findById(submissionDTO.getAttemptId())
                .orElseThrow(() -> new RuntimeException("Attempt not found with id: " + submissionDTO.getAttemptId()));

        if ("COMPLETED".equals(attempt.getStatus())) {
            throw new IllegalStateException("This attempt has already been submitted.");
        }

        int correctCount = 0;
        int totalQuestions = submissionDTO.getAnswers().size();
        int totalQuestionsAttempted=0;
        for (SubmitAttemptDTO.AnswerSubmission answerDto : submissionDTO.getAnswers()) {
            Question question = questionRepository.findById(answerDto.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found with id: " + answerDto.getQuestionId()));

            Options selectedOption = null;
            boolean isCorrect = false;

            if (answerDto.getSelectedOptionId() != null) {
                totalQuestionsAttempted++;
                selectedOption = optionsRepository.findById(answerDto.getSelectedOptionId())
                        .orElseThrow(() -> new RuntimeException("Option not found with id: " + answerDto.getSelectedOptionId()));

                if (selectedOption.isCorrect()) {
                    isCorrect = true;
                    correctCount++;
                }
            }

            UserAnswer userAnswer = UserAnswer.builder()
                    .mockAttempt(attempt)
                    .question(question)
                    .selectedOption(selectedOption)
                    .isCorrect(isCorrect)
                    .timeTakenSeconds(answerDto.getTimeTakenSeconds() != null ? answerDto.getTimeTakenSeconds() : 0)
                    .build();

            attempt.getUserAnswers().add(userAnswer);
        }

        double scorePercentage = totalQuestions > 0 ? ((double) correctCount / totalQuestions) * 100 : 0.0;

        attempt.setEndTime(LocalDateTime.now());
        attempt.setStatus("COMPLETED");
        attempt.setTotalQuestions(totalQuestions);
        attempt.setTotalQuestionsAttempted(totalQuestionsAttempted);
        attempt.setCorrectAnswers(correctCount);
        attempt.setScorePercentage(scorePercentage);

        mockAttemptRepository.save(attempt);

        return AttemptResultResponseDTO.builder()
                .attemptId(attempt.getId())
                .status("COMPLETED")
                .totalQuestions(totalQuestions)
                .totalQuestionsAttempted(totalQuestionsAttempted)
                .correctAnswers(correctCount)
                .scorePercentage(scorePercentage)
                .feedbackMessage(scorePercentage >= 60.0 ? "Great performance!" : "Keep practicing!")
                .build();
    }
}