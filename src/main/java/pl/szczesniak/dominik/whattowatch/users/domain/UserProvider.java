package pl.szczesniak.dominik.whattowatch.users.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

public interface UserProvider {
	boolean exists(UserId userId);
}
