package com.CHRESTAPI.todolist.targets.services;

import com.CHRESTAPI.todolist.targets.TargetCompany;

import java.util.Optional;

public interface TargetCompanyService {

    void createTargetCompany(TargetCompany targetCompany);

    void deleteTargetCompany( Long targetCompanyId);

    void editTargetCompany( Long targetCompanyId);

    Optional<TargetCompany> getTargetCompanyById(Long targetCompanyId);
}
