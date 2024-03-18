package pl.szczesniak.dominik.whattowatch.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.users.domain.UserFacade;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.query.model.UserQueryResult;

@RequiredArgsConstructor
@Component
public class LoggedInUserProvider {

	private final UserFacade userFacade;

	public UserId getLoggedUser(final Username username) {
		final UserQueryResult user = userFacade.getUserBy(username);
		return new UserId(user.getId());
	}

}
