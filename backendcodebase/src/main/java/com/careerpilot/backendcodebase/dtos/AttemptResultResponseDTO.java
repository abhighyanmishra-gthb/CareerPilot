package com.careerpilot.backendcodebase.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttemptResultResponseDTO {
    private Long attemptId;
    private String status;
    private Integer totalQuestions;
    private Integer totalQuestionsAttempted;
    private Integer correctAnswers;
    private Double scorePercentage;
    private String feedbackMessage;
}
