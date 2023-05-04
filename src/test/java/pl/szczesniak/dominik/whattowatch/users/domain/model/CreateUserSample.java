package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.Builder;

import static java.util.Optional.ofNullable;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserPasswordSample.createAnyUserPassword;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UsernameSample.createAnyUsername;

public class CreateUserSample {

	@Builder
	private static CreateUser build(final Username username, final UserPassword userPassword) {
		return new CreateUser(
				ofNullable(username).orElse(createAnyUsername()),
				ofNullable(userPassword).orElse(createAnyUserPassword())
		);
	}

}
