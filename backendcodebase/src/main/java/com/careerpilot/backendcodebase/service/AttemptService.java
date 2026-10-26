
package com.careerpilot.backendcodebase.service;

import com.careerpilot.backendcodebase.dtos.*;
import com.careerpilot.backendcodebase.entity.*;
import com.careerpilot.backendcodebase.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
public class AttemptService {

    private final MockAttemptRepository mockAttemptRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final OptionsRepository optionsRepository;
    private final AttemptQuestionRepository attemptQuestionRepository;
    private final QuestionSelectionService questionSelectionService;
    private final CompanyRepository companyRepository;

    public AttemptService(
            MockAttemptRepository mockAttemptRepository,
            UserRepository userRepository,
            QuestionRepository questionRepository,
            OptionsRepository optionsRepository,
            AttemptQuestionRepository attemptQuestionRepository,
            QuestionSelectionService questionSelectionService, CompanyRepository companyRepository) {

        this.mockAttemptRepository = mockAttemptRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.optionsRepository = optionsRepository;
        this.attemptQuestionRepository = attemptQuestionRepository;
        this.questionSelectionService = questionSelectionService;
        this.companyRepository = companyRepository;
    }
// ------------------------------------ start Attempt ------------------------------------------------

    @Transactional
    public StartAttemptResponseDTO startAttempt(StartAttemptDTO dto) {

        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required.");
        }

        if (dto.getNumberOfQuestions() == null
                || dto.getNumberOfQuestions() <= 0) {
            throw new IllegalArgumentException(
                    "Number of questions must be greater than zero."
            );
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException(
                        "User not found with id: " + dto.getUserId()
                ));

        // 1. Select questions: General mock or Company PYQ.
        Company company = null;
        List<Question> selectedQuestions;

        if (dto.getCompanyId() == null) {

            // Existing general mock logic.
            selectedQuestions =
                    questionSelectionService.selectQuestions(
                            dto.getNumberOfQuestions()
                    );

        } else {

            // Company-specific PYQ logic.
            company = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Company not found with id: "
                                    + dto.getCompanyId()
                    ));

            selectedQuestions =
                    questionSelectionService.selectCompanyQuestions(
                            company.getCompanyId(),
                            dto.getNumberOfQuestions()
                    );

            if (selectedQuestions.size() < dto.getNumberOfQuestions()) {
                throw new IllegalStateException(
                        "Not enough PYQs available for "
                                + company.getName()
                                + ". Available: "
                                + selectedQuestions.size()
                                + ", requested: "
                                + dto.getNumberOfQuestions()
                );
            }
        }

        if (selectedQuestions == null || selectedQuestions.isEmpty()) {
            throw new IllegalStateException(
                    "No questions are available for this attempt."
            );
        }

        // 2. Create and save the attempt.
        MockAttempt attempt = MockAttempt.builder()
                .user(user)
                .status("IN_PROGRESS")
                .testType(company != null
                        ? "COMPANY_PYQ"
                        : (dto.getTestType() != null
                        ? dto.getTestType() : "GENERAL"))
                .sourceName(company != null
                        ? company.getName()
                        : (dto.getSourceName() != null
                        ? dto.getSourceName() : "PRACTICE"))
                .startTime(LocalDateTime.now())
                .totalQuestions(selectedQuestions.size())
                .totalQuestionsAttempted(0)
                .correctAnswers(0)
                .build();

        MockAttempt savedAttempt = mockAttemptRepository.save(attempt);

        // 3. Save each question with its order.
        int order = 1;

        for (Question question : selectedQuestions) {
            AttemptQuestion assignedQuestion = new AttemptQuestion();

            assignedQuestion.setMockAttempt(savedAttempt);
            assignedQuestion.setQuestion(question);
            assignedQuestion.setQuestionOrder(order++);

            attemptQuestionRepository.save(assignedQuestion);
        }

        // 4. Return the attempt and its questions.
        List<AttemptQuestionResponseDTO> questions =
                mapToAttemptQuestionDTOs(selectedQuestions, 1);

        return StartAttemptResponseDTO.builder()
                .attemptId(savedAttempt.getId())
                .totalQuestions(selectedQuestions.size())
                .questions(questions)
                .build();
    }







    // -------------------------- fetching question method-------------------------------------
    // FETCH QUESTIONS FOR AN EXISTING ATTEMPT
    @Transactional(readOnly = true)
    public List<AttemptQuestionResponseDTO> getAttemptQuestions(
            Long attemptId) {

        MockAttempt attempt = mockAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException(
                        "Attempt not found with id: " + attemptId
                ));

        if (!"IN_PROGRESS".equals(attempt.getStatus())) {
            throw new IllegalStateException(
                    "Attempt is not in progress."
            );
        }

        List<AttemptQuestion> assignedQuestions =
                attemptQuestionRepository
                        .findByMockAttempt_IdOrderByQuestionOrderAsc(
                                attemptId
                        );

        return assignedQuestions.stream()
                .map(assigned -> {
                    Question question = assigned.getQuestion();

                    return AttemptQuestionResponseDTO.builder()
                            .questionId(question.getQuestionId())
                            .questionOrder(assigned.getQuestionOrder())
                            .question(question.getQuestion())
                            .options(mapOptions(question))
                            .build();
                })
                .toList();
    }

    // MAP QUESTIONS TO RESPONSE DTOs
    private List<AttemptQuestionResponseDTO> mapToAttemptQuestionDTOs(
            List<Question> questions,
            int startingOrder) {

        int[] order = {startingOrder};

        return questions.stream()
                .map(question ->
                        AttemptQuestionResponseDTO.builder()
                                .questionId(question.getQuestionId())
                                .questionOrder(order[0]++)
                                .question(question.getQuestion())
                                .options(mapOptions(question))
                                .build()
                )
                .toList();
    }

    // MAP OPTIONS WITHOUT EXPOSING CORRECT ANSWERS
    private List<optionResponseDTO> mapOptions(Question question) {

        return question.getOptions().stream()
                .map(option -> new optionResponseDTO(
                        option.getId(),
                        option.getOption()
                ))
                .toList();
    }

    //----------------------------- SUBMIT ATTEMPT------------------------------------------------------------
    @Transactional
    public AttemptResultResponseDTO submitAttempt(
            SubmitAttemptDTO submissionDTO) {

        MockAttempt attempt = mockAttemptRepository
                .findById(submissionDTO.getAttemptId())
                .orElseThrow(() -> new RuntimeException(
                        "Attempt not found with id: "
                                + submissionDTO.getAttemptId()
                ));

        if (!"IN_PROGRESS".equals(attempt.getStatus())) {
            throw new IllegalStateException(
                    "Attempt is not in progress or has already been submitted."
            );
        }

        List<AttemptQuestion> assignedQuestions =
                attemptQuestionRepository
                        .findByMockAttempt_IdOrderByQuestionOrderAsc(
                                attempt.getId()
                        );

        if (assignedQuestions.isEmpty()) {
            throw new IllegalStateException(
                    "No questions are assigned to this attempt."
            );
        }

        Map<Long, Question> assignedQuestionMap = new HashMap<>();

        for (AttemptQuestion assigned : assignedQuestions) {
            Question question = assigned.getQuestion();
            assignedQuestionMap.put(
                    question.getQuestionId(), question
            );
        }

        if (submissionDTO.getAnswers() == null) {
            throw new IllegalArgumentException(
                    "Answers must not be null."
            );
        }

        int totalQuestions = assignedQuestions.size();
        int totalQuestionsAttempted = 0;
        int correctCount = 0;

        Set<Long> submittedQuestionIds = new HashSet<>();

        for (SubmitAttemptDTO.AnswerSubmission answerDto
                : submissionDTO.getAnswers()) {

            Long questionId = answerDto.getQuestionId();

            // Ensure the question belongs to this attempt.
            Question question = assignedQuestionMap.get(questionId);

            if (question == null) {
                throw new IllegalArgumentException(
                        "Question " + questionId
                                + " is not assigned to this attempt."
                );
            }

            // Prevent duplicate answers for a question.
            if (!submittedQuestionIds.add(questionId)) {
                throw new IllegalArgumentException(
                        "Duplicate answer for question: " + questionId
                );
            }

            Options selectedOption = null;
            boolean isCorrect = false;

            if (answerDto.getSelectedOptionId() != null) {

                selectedOption = optionsRepository
                        .findById(answerDto.getSelectedOptionId())
                        .orElseThrow(() -> new RuntimeException(
                                "Option not found with id: "
                                        + answerDto.getSelectedOptionId()
                        ));

                // Ensure the selected option belongs to this question.
                if (selectedOption.getQuestion() == null
                        || !Objects.equals(
                        selectedOption.getQuestion().getQuestionId(),
                        questionId)) {

                    throw new IllegalArgumentException(
                            "Selected option does not belong to question: "
                                    + questionId
                    );
                }

                totalQuestionsAttempted++;

                isCorrect = selectedOption.isCorrect();

                if (isCorrect) {
                    correctCount++;
                }
            }

            UserAnswer userAnswer = UserAnswer.builder()
                    .mockAttempt(attempt)
                    .question(question)
                    .selectedOption(selectedOption)
                    .isCorrect(isCorrect)
                    .timeTakenSeconds(
                            answerDto.getTimeTakenSeconds() != null
                                    ? answerDto.getTimeTakenSeconds() : 0
                    )
                    .build();

            attempt.getUserAnswers().add(userAnswer);
        }

        // Score is calculated against all assigned questions.
        double scorePercentage =
                ((double) correctCount / totalQuestions) * 100;

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
                .feedbackMessage(
                        scorePercentage >= 60.0
                                ? "Great performance!"
                                : "Keep practicing!"
                )
                .build();
    }
}
