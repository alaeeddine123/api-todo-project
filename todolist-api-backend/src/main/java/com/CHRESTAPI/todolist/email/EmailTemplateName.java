package com.CHRESTAPI.todolist.email;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EmailTemplateName {


    ACTIVATE_ACCOUNT("activate_account");
    private final String name;
}
