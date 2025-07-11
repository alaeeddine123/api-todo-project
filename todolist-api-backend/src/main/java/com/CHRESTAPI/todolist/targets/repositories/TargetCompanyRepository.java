package com.CHRESTAPI.todolist.targets.repositories;

import com.CHRESTAPI.todolist.targets.TargetCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TargetCompanyRepository extends JpaRepository<TargetCompany , Long> {

}
