package com.CHRESTAPI.todolist.targets.services.impl;

import com.CHRESTAPI.todolist.targets.TargetCompany;
import com.CHRESTAPI.todolist.targets.repositories.TargetCompanyRepository;
import com.CHRESTAPI.todolist.targets.services.TargetCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TargetCompanyServiceImpl implements TargetCompanyService {


    private final TargetCompanyRepository targetCompanyRepository;
    @Override
    public void createTargetCompany(TargetCompany targetCompany) {
        this.targetCompanyRepository.save(targetCompany);
    }

    @Override
    public void deleteTargetCompany(Long targetCompanyId) {
        this.targetCompanyRepository.deleteById(targetCompanyId);
    }

    @Override
    public void editTargetCompany(Long targetCompanyId) {
    }

    @Override
    public Optional<TargetCompany> getTargetCompanyById(Long targetCompanyId) {
        return this.targetCompanyRepository.findById(targetCompanyId);
    }
}
