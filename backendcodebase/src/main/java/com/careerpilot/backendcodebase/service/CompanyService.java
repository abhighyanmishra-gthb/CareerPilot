package com.careerpilot.backendcodebase.service;

import com.careerpilot.backendcodebase.dtos.CompanyResponseDTO;
import com.careerpilot.backendcodebase.entity.Company;
import com.careerpilot.backendcodebase.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class CompanyService {
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public   Company addCompany(Company company){
       return companyRepository.save(company);
    }

    public Page<CompanyResponseDTO> getCompany(Pageable pageable) {
        Page<Company> allCompany= companyRepository.findAll(pageable);
        return allCompany.map(this::mapToDTO);
    }

    private CompanyResponseDTO mapToDTO(Company company) {
        CompanyResponseDTO dto = new CompanyResponseDTO();
        dto.setName(company.getName());
        dto.setCompanyId(company.getCompanyId());
        dto.setLogoUrl(company.getLogoUrl());
        return dto;
    }
}
