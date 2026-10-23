package com.careerpilot.backendcodebase.dtos;

import lombok.Data;

@Data
public class StartAttemptDTO {
    private Long userId;
    private String testType;   // e.g., "COMPANY_MOCK" or "TOPIC_PRACTICE"
    private String sourceName; // e.g., "TCS", "Infosys", "Logical Reasoning"
}
