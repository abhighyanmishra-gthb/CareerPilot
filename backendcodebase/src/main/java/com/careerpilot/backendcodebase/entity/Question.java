package com.careerpilot.backendcodebase.entity;

import com.careerpilot.backendcodebase.dtos.optionDTO;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long questionId;
    private String question;
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Options> options=new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String explanation;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=10)
    private DifficultyLevel difficulty;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=20)
    private QuestionCategory category;

    public void addOptions(@Valid Options option) {
        this.options.add(option);
        option.setQuestion(this);
    }
}
