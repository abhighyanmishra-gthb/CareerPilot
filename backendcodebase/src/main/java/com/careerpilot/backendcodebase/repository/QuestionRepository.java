package com.careerpilot.backendcodebase.repository;

import com.careerpilot.backendcodebase.entity.DifficultyLevel;
import com.careerpilot.backendcodebase.entity.Question;
import com.careerpilot.backendcodebase.entity.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question> findByCategoryAndDifficulty(QuestionCategory category, DifficultyLevel difficulty);
    List<Question> findByCategory(QuestionCategory category);

    List<Question> findByDifficulty(DifficultyLevel difficulty);
}
