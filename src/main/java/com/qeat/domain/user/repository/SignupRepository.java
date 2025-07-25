package com.qeat.domain.user.repository;

import com.qeat.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignupRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
