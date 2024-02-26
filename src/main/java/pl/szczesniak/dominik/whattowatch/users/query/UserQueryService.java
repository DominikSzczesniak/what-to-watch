package pl.szczesniak.dominik.whattowatch.users.query;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.query.model.UserQueryResult;

import java.util.Optional;

public interface UserQueryService {

	boolean isUsernameTaken(Username username);

	boolean exists(UserId userId);

	Optional<UserQueryResult> findUserQueryResult(Username username);

}
