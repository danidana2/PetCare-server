package com.example.somserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "user_id", nullable = false, length = 10)
    private String userId; //users table -> user_id : pk, not null, varchar(10)

    @Column(name = "password", nullable = false, length = 100)
    private String password; //users table -> password : not null, varchar(100)

    @Column(name = "nickname", nullable = false, length = 10)
    private String nickname; //users table -> nickname : not null, varchar(10)

    @Column(name = "role", nullable = false, length = 10)
    private String role; //users table -> role : not null, varchar(10)
}
