package com.CHRESTAPI.todolist.targets.Controllers;


import com.CHRESTAPI.todolist.targets.TargetCompany;
import com.CHRESTAPI.todolist.targets.services.TargetCompanyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/targets")
@Tag(name = "Target")
public class TargetCompanyController {

    private final TargetCompanyService targetCompanyService ;




}
