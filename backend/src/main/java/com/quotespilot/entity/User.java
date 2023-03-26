package com.quotespilot.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
    private String hashedPassword;
    private String salt;

    //For simplicity 1 user have only 1 role
    private String role;
    
    @ManyToMany(mappedBy = "users")
    private Set<Quote> quotes = new HashSet<>();
}
