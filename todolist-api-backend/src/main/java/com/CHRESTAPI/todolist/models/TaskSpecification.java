package com.CHRESTAPI.todolist.models;

import com.CHRESTAPI.todolist.entities.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<Task> withAssigneeId ( Long assigneeId){
        return   (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("assignee").get("id"),assigneeId);

    }

    public static Specification<Task> withStatus ( String status){
        return  (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("status") , status) ;
    }
}
