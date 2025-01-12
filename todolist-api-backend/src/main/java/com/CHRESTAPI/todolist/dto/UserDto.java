package com.CHRESTAPI.todolist.dto;

import com.CHRESTAPI.todolist.entities.User;
import com.CHRESTAPI.todolist.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class UserDto {


        private Long id;
        private String username;
        private String password;
        private String email;
        private String firstName;
        private String lastName;
        private Role role;

        public static UserDto fromEntity(User user) {
                if (user == null) return null;
                return  null;
        }

        public  static  User toEntity(UserDto userDto){
                if (userDto == null) return null;

               return  null;
        }

    }
