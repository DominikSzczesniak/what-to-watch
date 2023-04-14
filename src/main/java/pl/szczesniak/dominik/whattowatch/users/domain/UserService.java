package pl.szczesniak.dominik.whattowatch.users.domain;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

@RequiredArgsConstructor
public class UserService {

	private final UserRepository repository;

	public UserId createUser(final Username username, final UserPassword userPassword) {
		User user = new User(username, repository.nextUserId(), userPassword);
		repository.create(user);
		return user.getUserId();
	}

	public boolean exists(final UserId userId) {
		return repository.exists(userId);
	}


	public UserId login(final Username username, final UserPassword userPassword) {
		return repository.findBy(username.getValue()).get().getUserId();
	}

}
