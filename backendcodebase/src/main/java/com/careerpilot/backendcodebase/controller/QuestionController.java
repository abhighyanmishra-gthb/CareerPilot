package com.careerpilot.backendcodebase.controller;

import com.careerpilot.backendcodebase.dtos.QuestionDTO;
import com.careerpilot.backendcodebase.dtos.QuestionResponseDTO;
import com.careerpilot.backendcodebase.entity.DifficultyLevel;
import com.careerpilot.backendcodebase.entity.QuestionCategory;
import com.careerpilot.backendcodebase.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

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
    @GetMapping("/get-questionbycategoryanddifficulty")
    public ResponseEntity<List<QuestionResponseDTO>> findByCategoryAndDifficulty(@RequestParam QuestionCategory category, @RequestParam DifficultyLevel difficulty){
        return ResponseEntity.status(HttpStatus.OK).body(questionService.getByCategoryAndDifficulty(category,difficulty));
    }
    @GetMapping("/get-questionbycategory")
    public ResponseEntity<List<QuestionResponseDTO>> findByCategory(@RequestParam QuestionCategory category){
        return ResponseEntity.status(HttpStatus.OK).body(questionService.getByCategory(category));
    }
    @GetMapping("/get-questionbydifficulty")
    public ResponseEntity<List<QuestionResponseDTO>> findByDifficulty( @RequestParam DifficultyLevel difficulty){
        return ResponseEntity.status(HttpStatus.OK).body(questionService.getByDifficulty(difficulty));
    }
    @GetMapping("/getbycompaniesspecific/{companyId}")
    public  ResponseEntity<Page<QuestionResponseDTO>> getByCompanies(@PathVariable Long companyId, @RequestParam(required = false) DifficultyLevel difficulty,
                                                                     @RequestParam(required = false) QuestionCategory category,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "5") int pageSize){
        return ResponseEntity.status(HttpStatus.OK).body(questionService.getByCompany(companyId,difficulty,category, PageRequest.of(page,pageSize)));
    }

}
