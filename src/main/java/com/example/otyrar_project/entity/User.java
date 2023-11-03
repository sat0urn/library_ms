package com.example.otyrar_project.entity;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Collection;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Document(collection = "User")
public class User {
    @Id
    private String id;
    @Getter
    private String name;
    private String surname;
    private Long phone;
    private String password;
    private String email;
    private List<Role> roles;
}
