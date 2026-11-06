package com.careerpilot.backendcodebase.repository;

import com.careerpilot.backendcodebase.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
}
