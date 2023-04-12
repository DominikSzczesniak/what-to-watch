package pl.szczesniak.dominik.whattowatch.users.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.Optional;

public interface UserRepository {

	void create(User user);
	UserId nextUserId();
	boolean exists(UserId userId);
	Optional<User> findBy(UserId userId);
	Optional<User> findBy(String username);

}
