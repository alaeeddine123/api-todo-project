package com.CHRESTAPI.todolist.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


@Getter
@AllArgsConstructor
public enum BusinessErrorCodes {

    NO_CODE(0, NOT_IMPLEMENTED,"No Code " ),
    INCCORECT_CURRENT_PASSWORD(300,BAD_REQUEST,"CURRENT PASSWORD IS INCORRECT"),
    NEW_PASSWORD_DOES_NOT_MATCH(301,BAD_REQUEST,"New password does not match"),
    ACCOUNT_LOCKED(302,FORBIDDEN,"User account is Locked"),
    ACCOUNT_DISABLED(303,FORBIDDEN,"User account is disabled "),
    BAD_CREDENTIALS(304 , FORBIDDEN , "Login / Password is incorrect");


    private final int code;
    private final HttpStatus httpStatus;
    private final String description;


    }
