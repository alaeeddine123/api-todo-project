package com.CHRESTAPI.todolist.repositories;

import com.CHRESTAPI.todolist.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User , Long> {

Optional<User> findByEmail(@Param("email") String email);

}
