package com.careerpilot.backendcodebase.service;
import com.careerpilot.backendcodebase.dtos.QuestionDTO;
import com.careerpilot.backendcodebase.dtos.QuestionResponseDTO;
import com.careerpilot.backendcodebase.dtos.optionDTO;
import com.careerpilot.backendcodebase.dtos.optionResponseDTO;
import com.careerpilot.backendcodebase.entity.Options;
import com.careerpilot.backendcodebase.entity.Question;
import com.careerpilot.backendcodebase.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public QuestionResponseDTO addQuestion(QuestionDTO questionDTO) {
        Question question = MapToEntity(questionDTO);
        Question savedQuestion = questionRepository.save(question);
        return mapToDTO(savedQuestion);

    }
    public List<QuestionResponseDTO> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream()
                .map(this::mapToDTO)
                .toList();
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
        return questionResponseDTO;
    }


    private Question MapToEntity(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setQuestion(questionDTO.getQuestion());
        question.setExplanation(questionDTO.getExplanation());
        question.setDifficulty(questionDTO.getDifficulty());
        question.setCategory(questionDTO.getCategory());
        for (optionDTO optiondto : questionDTO.getOptions()) {
            Options option = new Options();
            option.setOption(optiondto.getOption());
            option.setCorrect(optiondto.isCorrect());
            question.addOptions(option);
        }
        return question;
    }
}
