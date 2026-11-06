
package com.careerpilot.backendcodebase.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttemptQuestionResponseDTO {

    private Long questionId;
    private Integer questionOrder;
    private String question;
    private List<optionResponseDTO> options;
}

