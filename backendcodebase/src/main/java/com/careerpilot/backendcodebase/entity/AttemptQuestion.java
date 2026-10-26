
package com.careerpilot.backendcodebase.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "attempt_questions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_attempt_question",
                        columnNames = {"attempt_id", "question_id"}
                ),
                @UniqueConstraint(
                        name = "uk_attempt_question_order",
                        columnNames = {"attempt_id", "question_order"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AttemptQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attempt_id", nullable = false)
    private MockAttempt mockAttempt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "question_order", nullable = false)
    private Integer questionOrder;
}

