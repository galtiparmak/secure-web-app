package com.geko.secure_web_app.Repository;

import com.geko.secure_web_app.Entity.PasswordResetToken;
import com.geko.secure_web_app.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);
}
