package com.careerpilot.backendcodebase.dtos;

import com.careerpilot.backendcodebase.entity.DifficultyLevel;
import com.careerpilot.backendcodebase.entity.QuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDTO {
    private Long questionId;
    private String question;
    private List<optionResponseDTO> options=new ArrayList<>();
    private String explanation;
    private DifficultyLevel difficulty;
    private QuestionCategory category;
}
