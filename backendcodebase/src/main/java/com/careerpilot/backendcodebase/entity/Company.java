package com.careerpilot.backendcodebase.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "company",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_company_name",
                        columnNames = "name"
                )
        }
)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @Column(nullable = false, length = 100)
    private String name;
}