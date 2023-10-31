package pl.szczesniak.dominik.whattowatch.users.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

	void create(User user);

	boolean exists(UserId userId);

	Optional<User> findBy(String username);

	List<UserId> findAllUsers();

}