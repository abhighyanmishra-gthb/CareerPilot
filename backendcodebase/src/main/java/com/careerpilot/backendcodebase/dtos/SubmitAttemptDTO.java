package com.careerpilot.backendcodebase.dtos;

import lombok.Data;
import java.util.List;

@Data
public class SubmitAttemptDTO {
    private Long attemptId;

    private List<AnswerSubmission> answers;

    @Data
    public static class AnswerSubmission {
        private Long questionId;
        private Long selectedOptionId;
        private Integer timeTakenSeconds;
    }
}