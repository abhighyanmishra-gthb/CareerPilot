package com.careerpilot.backendcodebase.service;

import com.careerpilot.backendcodebase.entity.Question;
import com.careerpilot.backendcodebase.repository.CompanyQuestionRepository;
import com.careerpilot.backendcodebase.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QuestionSelectionService {
    private  QuestionRepository questionRepository;
    private  CompanyQuestionRepository companyQuestionRepository;


    public QuestionSelectionService(QuestionRepository questionRepository,CompanyQuestionRepository companyQuestionRepository)
    {
        this.questionRepository=questionRepository;
        this.companyQuestionRepository=companyQuestionRepository;
    }
    public List<Question> selectQuestions(int numberOfQuestions)
    {
        return questionRepository.findRandomQuestions(numberOfQuestions);
    }

    // New company-specific PYQ logic
    public List<Question> selectCompanyQuestions(Long companyId,
                                                 int numberOfQuestions) {
        List<Question> questions =
                companyQuestionRepository.findQuestionsByCompanyId(companyId);

        Collections.shuffle(questions);

        return questions.stream()
                .limit(numberOfQuestions)
                .toList();
    }
}
