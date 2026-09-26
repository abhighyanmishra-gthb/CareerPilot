package com.careerpilot.backendcodebase.entity;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable=false)
    private String userName;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false)
    private String email;
    @Column(nullable=false)
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String stream;
    private List<String> roles;
}
