package pl.szczesniak.dominik.whattowatch.users.infrastructure.adapters.outgoing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczesniak.dominik.whattowatch.users.domain.User;

import java.util.Optional;

@Repository
public interface SpringDataJpaRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUserNameEquals(String username);

}
