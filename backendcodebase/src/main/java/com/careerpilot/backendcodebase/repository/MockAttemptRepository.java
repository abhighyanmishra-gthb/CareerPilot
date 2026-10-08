package com.careerpilot.backendcodebase.repository;

import com.careerpilot.backendcodebase.entity.MockAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MockAttemptRepository extends JpaRepository<MockAttempt, Long> {
    //to fetch all the mock attempt of a user using his userId....
    List<MockAttempt> findByUser_UserId(Long userId);
}
