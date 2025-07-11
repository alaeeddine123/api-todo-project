package com.CHRESTAPI.todolist.targets;

import com.CHRESTAPI.todolist.targets.dtos.CreateTargetCompanyDto;
import com.CHRESTAPI.todolist.targets.dtos.TargetCompanyResponseDto;

import java.util.List;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface TargetCompanyMapper {

    TargetCompany toEntity(CreateTargetCompanyDto createDto);

    TargetCompanyResponseDto toResponseDto(TargetCompany entity);

    List<TargetCompanyResponseDto> toResponseDtoList(List<TargetCompany> entities);
}
