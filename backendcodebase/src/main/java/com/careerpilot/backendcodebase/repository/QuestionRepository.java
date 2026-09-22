package com.careerpilot.backendcodebase.repository;

import com.careerpilot.backendcodebase.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
}
