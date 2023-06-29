package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.outgoing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;

@Repository
public interface SpringDataJpaUserRepository extends JpaRepository<User, UserId> {

	Optional<User> findUserByUsername_Value(String username);

}
