package bg.rborisov.softunigraduation.dao;

import bg.rborisov.softunigraduation.model.PasswordToken;
import bg.rborisov.softunigraduation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {

    Optional<PasswordToken> findPasswordTokenByUser(User user);

    Optional<PasswordToken> findByToken(String token);

    Set<PasswordToken> findByExpireDateBefore(LocalDateTime expireDate);
}