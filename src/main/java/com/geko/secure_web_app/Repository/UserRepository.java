package com.geko.secure_web_app.Repository;

import com.geko.secure_web_app.Entity.Role;
import com.geko.secure_web_app.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String email);
    List<User> findByRole(Role role);
}
