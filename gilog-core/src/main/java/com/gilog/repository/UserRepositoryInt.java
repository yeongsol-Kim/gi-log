package com.gilog.repository;

import com.gilog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryInt extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
