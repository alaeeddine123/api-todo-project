package com.CHRESTAPI.todolist.entities;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskId;

    private String title;

    private String description;

    private LocalDateTime due_date;

    private String status;

    private boolean archived;

    private boolean shareable;


    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAT;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedAt;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Integer createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifedBy;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;


}
