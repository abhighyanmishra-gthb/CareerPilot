package com.careerpilot.backendcodebase.controller;

import com.careerpilot.backendcodebase.entity.Company;
import com.careerpilot.backendcodebase.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @PostMapping("/addcompany")
    public ResponseEntity<Company> addCompany(@RequestBody Company company) {
        Company company1=companyService.addCompany(company);
        return  ResponseEntity.status(HttpStatus.CREATED).body(company1);
    }
}
