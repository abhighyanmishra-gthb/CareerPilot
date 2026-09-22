package com.careerpilot.backendcodebase.controller;

import com.careerpilot.backendcodebase.dtos.QuestionDTO;
import com.careerpilot.backendcodebase.dtos.QuestionResponseDTO;
import com.careerpilot.backendcodebase.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/question")
public class QuestionController {

    private final QuestionService questionService;
    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }
    @GetMapping("/allquestion")
    public String allWorkingFine(){
        return "all works fine";
    }
    @PostMapping("/create-question")
    public ResponseEntity<QuestionResponseDTO> createQuestion(@RequestBody QuestionDTO questionDTO){
       QuestionResponseDTO response= questionService.addQuestion(questionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/get-all-questions")
    public ResponseEntity<List<QuestionResponseDTO>> getAllQuestions(){
        return ResponseEntity.status(HttpStatus.OK).body(questionService.getAllQuestions());
    }

}
