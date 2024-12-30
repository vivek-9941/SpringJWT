package com.security.security.repo;

import com.security.security.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface repository extends JpaRepository<user, Long> {
    user findByuserName(String username);
}
