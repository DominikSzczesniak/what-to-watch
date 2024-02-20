package pl.szczesniak.dominik.whattowatch.users.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.Optional;

public interface UserRepository {

	void create(User user);

	boolean exists(UserId userId);

	Optional<User> findBy(Username username);

}