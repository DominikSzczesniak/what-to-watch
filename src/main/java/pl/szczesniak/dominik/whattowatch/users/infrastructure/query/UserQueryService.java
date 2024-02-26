package pl.szczesniak.dominik.whattowatch.users.infrastructure.query;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.Optional;

public interface UserQueryService {

	boolean isUsernameTaken(Username username);

	boolean exists(UserId userId);

	Optional<UserQueryResult> findUserQueryResult(Username username);

}
