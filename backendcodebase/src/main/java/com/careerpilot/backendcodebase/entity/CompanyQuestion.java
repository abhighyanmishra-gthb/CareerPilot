
package com.careerpilot.backendcodebase.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "company_question",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_company_question",
                        columnNames = {"company_id", "question_id"}
                )
        }
)
public class CompanyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyQuestionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
