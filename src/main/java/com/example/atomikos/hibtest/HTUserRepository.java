package com.example.atomikos.hibtest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("htUserRepository")
public interface HTUserRepository extends JpaRepository<HTUser, Long> {
}
