package pl.szczesniak.dominik.whattowatch.users.domain;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectAlreadyExistsException;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.commands.CreateUser;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidCredentialsException;

@RequiredArgsConstructor
public class UserService {

	private final UserRepository repository;

	private final UserRoleRepository roleRepository;

	public UserId createUser(final CreateUser command) {
		final User user = new User(command.getUsername(), command.getUserPassword());
		addDefaultRoleToUser(user);
		repository.create(user);
		return user.getUserId();
	}

	private void addDefaultRoleToUser(final User user) {
		final UserRole role = roleRepository.findBy(new RoleName("USER")).orElseGet(() -> {
			final UserRole createdRole = new UserRole(new RoleName("USER"));
			roleRepository.create(createdRole);
			return createdRole;
		});

		user.addRole(role, user);
	}

	public boolean exists(final UserId userId) {
		return repository.exists(userId);
	}

	public UserId login(final Username username, final UserPassword userPassword) {
		return repository.findBy(username)
				.filter(user -> user.getUserPassword().equals(userPassword))
				.orElseThrow(() -> new InvalidCredentialsException("Invalid credentials, could not log in.")).getUserId();
	}

	public boolean isUsernameTaken(final Username username) {
		return repository.findBy(username).isPresent();
	}

	public void addRole(final UserId userId, final UserRole role) {
		final User user = repository.findBy(userId).orElseThrow(() -> new ObjectDoesNotExistException("asd"));
		checkUserAlreadyHasRole(role, user);
		user.addRole(role, user);
		repository.update(user);
	}

	private static void checkUserAlreadyHasRole(final UserRole role, final User user) {
		user.getRoles().forEach(userRole -> {
			if (userRole.getRoleName().equals(role.getRoleName())) {
				throw new ObjectAlreadyExistsException("User already has this role.");
			}
		});
	}

	User getUserBy(final Username username) {
		return repository.findBy(username).orElseThrow(() ->
				new ObjectDoesNotExistException("User with username " + username.getValue() + " does not exist."));
	}

	User getUserBy(final UserId userId) {
		return repository.findBy(userId).orElseThrow(() ->
				new ObjectDoesNotExistException("User with userId: " + userId.getValue() + " does not exist."));
	}

}
