package com.example.lesson5.backend.domain.repository;

import com.example.lesson5.backend.domain.model.entity.Email;
import com.example.lesson5.backend.domain.model.entity.EmailPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, EmailPK> {

    public Email findByEmail(String email);

}
