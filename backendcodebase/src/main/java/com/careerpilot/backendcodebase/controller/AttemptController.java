package com.careerpilot.backendcodebase.controller;

import com.careerpilot.backendcodebase.dtos.*;
import com.careerpilot.backendcodebase.service.AttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attempt")
public class AttemptController {


    private final AttemptService attemptService;

    public AttemptController(AttemptService attemptService){
        this.attemptService=attemptService;
    }

    @PostMapping("/start")
    public ResponseEntity<StartAttemptResponseDTO> startAttempt(@RequestBody StartAttemptDTO startAttemptDTO) {

        return ResponseEntity.ok(attemptService.startAttempt(startAttemptDTO));
    }

    @PostMapping("/submit")
    public ResponseEntity<AttemptResultResponseDTO> submitAttempt(@RequestBody SubmitAttemptDTO submitAttemptDTO) {
        AttemptResultResponseDTO result = attemptService.submitAttempt(submitAttemptDTO);
        return ResponseEntity.ok(result);
    }
}