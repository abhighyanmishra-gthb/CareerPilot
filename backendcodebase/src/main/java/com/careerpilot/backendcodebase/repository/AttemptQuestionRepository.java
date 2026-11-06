
package com.careerpilot.backendcodebase.repository;

import com.careerpilot.backendcodebase.entity.AttemptQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttemptQuestionRepository
        extends JpaRepository<AttemptQuestion, Long> {

    List<AttemptQuestion> findByMockAttempt_IdOrderByQuestionOrderAsc(
            Long attemptId
    );
}
