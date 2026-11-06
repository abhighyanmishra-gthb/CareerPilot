
package com.careerpilot.backendcodebase.repository;

import com.careerpilot.backendcodebase.entity.CompanyQuestion;
import com.careerpilot.backendcodebase.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyQuestionRepository
        extends JpaRepository<CompanyQuestion, Long> {

    List<CompanyQuestion> findByCompany_CompanyId(Long companyId);


    @Query("""
    SELECT cq.question
    FROM CompanyQuestion cq
    WHERE cq.company.companyId = :companyId
""")
    List<Question> findQuestionsByCompanyId(
            @Param("companyId") Long companyId
    );

}
