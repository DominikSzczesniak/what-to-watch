package pl.szczesniak.dominik.whattowatch.users.domain;


import lombok.Builder;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UsernameSample.createAnyUsername;

public class UserSample {

	@Builder
	private static User build(final Username username, final UserId userId) {
		return new User(
				ofNullable(username).orElse(createAnyUsername()),
				ofNullable(userId).orElse(createAnyUserId())
				);
	}

}