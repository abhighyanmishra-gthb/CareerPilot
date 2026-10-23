package com.careerpilot.backendcodebase.controller;

import com.careerpilot.backendcodebase.dtos.*;
import com.careerpilot.backendcodebase.service.AttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attempt")
public class AttemptController {

    @Autowired
    private AttemptService attemptService;

    @PostMapping("/start")
    public ResponseEntity<Long> startAttempt(@RequestBody StartAttemptDTO startAttemptDTO) {
        Long attemptId = attemptService.startAttempt(startAttemptDTO);
        return ResponseEntity.ok(attemptId);
    }

    @PostMapping("/submit")
    public ResponseEntity<AttemptResultResponseDTO> submitAttempt(@RequestBody SubmitAttemptDTO submitAttemptDTO) {
        AttemptResultResponseDTO result = attemptService.submitAttempt(submitAttemptDTO);
        return ResponseEntity.ok(result);
    }
}