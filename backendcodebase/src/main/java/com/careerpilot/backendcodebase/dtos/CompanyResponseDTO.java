package com.careerpilot.backendcodebase.dtos;

import com.careerpilot.backendcodebase.entity.Question;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
@Data
public class CompanyResponseDTO {


    private Long companyId;

    private String name;
    private String logoUrl;

}
