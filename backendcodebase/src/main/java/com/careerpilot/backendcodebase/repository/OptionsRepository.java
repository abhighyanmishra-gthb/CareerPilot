package com.careerpilot.backendcodebase.repository;

import com.careerpilot.backendcodebase.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionsRepository extends JpaRepository<Options, Long> {
}