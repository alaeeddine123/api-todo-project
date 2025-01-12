package com.CHRESTAPI.todolist.entities;

import jakarta.persistence.*;
import lombok.*;
import org.apache.james.mime4j.dom.datetime.DateTime;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Project {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long ProjectId;

    String title;

    String description;


    LocalDateTime dueDate;

    String priority;

    String TeamSize;
}
