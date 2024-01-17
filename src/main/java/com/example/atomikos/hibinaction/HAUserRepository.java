package com.example.atomikos.hibinaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("haUserRepository")
public interface HAUserRepository extends JpaRepository<HAUser, Long> {
}
