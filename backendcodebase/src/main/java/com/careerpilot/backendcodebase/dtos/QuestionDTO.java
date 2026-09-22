package com.careerpilot.backendcodebase.dtos;

import com.careerpilot.backendcodebase.entity.DifficultyLevel;
import com.careerpilot.backendcodebase.entity.Options;
import com.careerpilot.backendcodebase.entity.QuestionCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    @NotBlank(message = "please enter a question")
    private String question;
    @Valid
    private List<optionDTO> options=new ArrayList<>();
    private String explanation;
    @NotBlank(message = "please enter a difficulty level")
    private DifficultyLevel difficulty;
    @NotBlank(message = "please enter Category of question")
    private QuestionCategory category;
}
