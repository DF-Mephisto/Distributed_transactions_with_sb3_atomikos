package com.example.atomikos.hibinaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Entity
@Table(name = "users")
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class HAUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;

    long age;
}