package com.careerpilot.backendcodebase.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class optionDTO {
    @NotBlank(message = "please insert values")
    private String option;
    @NotNull(message = "please provide options")
    private boolean isCorrect;
}
