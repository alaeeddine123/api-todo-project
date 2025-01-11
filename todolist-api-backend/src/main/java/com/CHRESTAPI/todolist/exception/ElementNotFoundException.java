package com.CHRESTAPI.todolist.exception;

public class ElementNotFoundException extends RuntimeException {


    public ElementNotFoundException(String message) {
        super(message);
    }

    public static ElementNotFoundException taskNotFound(Long id) {
        return new ElementNotFoundException("Task not found with ID: " + id);
    }

    public static ElementNotFoundException userNotFound(Long id) {
        return new ElementNotFoundException("user not found with ID: " + id);
    }

    public static ElementNotFoundException taskListNotFound(Long id) {
        return new ElementNotFoundException("TaskList not found with ID: " + id);
    }



}
