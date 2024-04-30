package pl.szczesniak.dominik.whattowatch.users.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.commands.CreateUser;
import pl.szczesniak.dominik.whattowatch.users.query.UserQueryService;
import pl.szczesniak.dominik.whattowatch.users.query.model.UserQueryResult;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class UserFacade {

	private final UserService userService;

	private final UserQueryService userQueryService;

	public UserId createUser(final CreateUser command) {
		return userService.createUser(command);
	}

	public UserId login(final Username username, final UserPassword userPassword) {
		return userService.login(username, userPassword);
	}

	public boolean isUsernameTaken(final Username username) {
		return userQueryService.isUsernameTaken(username);
	}

	public boolean exists(final UserId userId) {
		return userQueryService.exists(userId);
	}

	public UserQueryResult getUserBy(final Username username) {
		return userQueryService.findUserQueryResult(username).orElseThrow(
				() -> new ObjectDoesNotExistException("User with username: " + username.getValue() + " does not exist"));
	}

	public void deleteUser(final UserId userId) {
		userService.deleteUser(userId);
	}

}
