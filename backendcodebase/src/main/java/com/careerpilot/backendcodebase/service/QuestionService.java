package com.careerpilot.backendcodebase.service;

import com.careerpilot.backendcodebase.dtos.*;
import com.careerpilot.backendcodebase.entity.*;
import com.careerpilot.backendcodebase.repository.CompanyQuestionRepository;
import com.careerpilot.backendcodebase.repository.CompanyRepository;
import com.careerpilot.backendcodebase.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    public List<QuestionResponseDTO> getByCategoryand;
    private final CompanyQuestionRepository companyQuestionRepository;
    private final CompanyRepository companyRepository;


    @Autowired
    public QuestionService(QuestionRepository questionRepository,CompanyQuestionRepository companyQuestionRepository,CompanyRepository companyRepository) {
        this.questionRepository = questionRepository;
        this.companyQuestionRepository=companyQuestionRepository;
        this.companyRepository=companyRepository;

    }

    public QuestionResponseDTO addQuestion(QuestionDTO questionDTO) {
        Question question = MapToEntity(questionDTO);
        Question savedQuestion = questionRepository.save(question);

        if (questionDTO.getCompanyIds() != null
                && !questionDTO.getCompanyIds().isEmpty()) {

            for (Long companyId : questionDTO.getCompanyIds()) {

                Company company = companyRepository
                        .findById(companyId)
                        .orElseThrow(() ->new RuntimeException("Company not found with ID: " + companyId));

                CompanyQuestion companyQuestion = new CompanyQuestion();
                companyQuestion.setCompany(company);
                companyQuestion.setQuestion(savedQuestion);

                companyQuestionRepository.save(companyQuestion);
            }
        }
        return mapToDTO(savedQuestion);

    }

    public List<QuestionResponseDTO> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<QuestionResponseDTO> getByCategoryAndDifficulty(QuestionCategory category, DifficultyLevel difficulty) {
        List<Question> categoryQuestion = questionRepository.findByCategoryAndDifficulty(category, difficulty);
        if (categoryQuestion.isEmpty()) {
            throw new RuntimeException("No question found with this Category  and difficulty Not Found");
        }
        return categoryQuestion.stream()
                .map(this::mapToDTO)
                .toList();
    }
    public List<QuestionResponseDTO> getByCategory(QuestionCategory category) {
        List<Question> categoryQuestion = questionRepository.findByCategory(category );
        if (categoryQuestion.isEmpty()) {
            throw new RuntimeException("No question found with this Category ");
        }
        return categoryQuestion.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<QuestionResponseDTO> getByDifficulty( DifficultyLevel difficulty) {
        List<Question> categoryQuestion = questionRepository.findByDifficulty(  difficulty );
        if (categoryQuestion.isEmpty()) {
            throw new RuntimeException("No question found with this Category ");
        }
        return categoryQuestion.stream()
                .map(this::mapToDTO)
                .toList();
    }
    public Page<QuestionResponseDTO> getByCompany(Long companyId, DifficultyLevel difficulty, QuestionCategory category, Pageable pageable) {
        Page<Question> response=questionRepository.findByCompany(companyId,difficulty,category,pageable);
        return response.map(this::mapToDTO);

    }

    private QuestionResponseDTO mapToDTO(Question savedQuestion) {
        QuestionResponseDTO questionResponseDTO = new QuestionResponseDTO();
        questionResponseDTO.setQuestionId(savedQuestion.getQuestionId());
        questionResponseDTO.setQuestion(savedQuestion.getQuestion());
        questionResponseDTO.setCategory(savedQuestion.getCategory());
        questionResponseDTO.setDifficulty(savedQuestion.getDifficulty());
        List<optionResponseDTO> optionResponseDTO = savedQuestion.getOptions().stream().map(opt -> new optionResponseDTO(opt.getId(), opt.getOption())).toList();
        questionResponseDTO.setOptions(optionResponseDTO);
        questionResponseDTO.setExplanation(savedQuestion.getExplanation());
        Set<CompanyResponseDTO> company= Collections.emptySet();
        if (savedQuestion.getCompany() != null) {
            company = savedQuestion.getCompany().stream()
                    .map(this::mapToCompany)
                    .collect(Collectors.toSet());
        }
        questionResponseDTO.setCompanies(company);
        return questionResponseDTO;
    }
    private CompanyResponseDTO mapToCompany(Company company) {
        CompanyResponseDTO companyResponseDTO = new CompanyResponseDTO();
        companyResponseDTO.setCompanyId(company.getCompanyId());
        companyResponseDTO.setName(company.getName());
        companyResponseDTO.setLogoUrl(company.getLogoUrl());
        return  companyResponseDTO;
    }


    private Question MapToEntity(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setQuestion(questionDTO.getQuestion());
        question.setExplanation(questionDTO.getExplanation());
        question.setDifficulty(questionDTO.getDifficulty());
        question.setCategory(questionDTO.getCategory());
        Set<Company> fetchCompany = new HashSet<>(companyRepository.findAllById(questionDTO.getCompanyIds()));
        question.setCompany(fetchCompany);
        for (optionDTO optiondto : questionDTO.getOptions()) {
            Options option = new Options();
            option.setOption(optiondto.getOption());
            option.setCorrect(optiondto.isCorrect());
            question.addOptions(option);
        }
        return question;
    }



}
