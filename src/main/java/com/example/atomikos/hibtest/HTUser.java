package com.example.atomikos.hibtest;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class HTUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;

    long age;
}