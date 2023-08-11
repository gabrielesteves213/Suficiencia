package br.com.suficiencia.domain.repositories;

import br.com.suficiencia.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByLoginIgnoreCase(String login);
    Optional<User> findByLoginIgnoreCase(String login);
}
