package com.careerpilot.backendcodebase.service;

import com.careerpilot.backendcodebase.entity.Company;
import com.careerpilot.backendcodebase.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class CompanyService {
    private CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public   Company addCompany(Company company){
       return companyRepository.save(company);
    }

}
