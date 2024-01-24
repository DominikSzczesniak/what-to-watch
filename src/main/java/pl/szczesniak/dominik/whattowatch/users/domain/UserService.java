package pl.szczesniak.dominik.whattowatch.users.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectAlreadyExistsException;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.RoleName;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserPassword;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;
import pl.szczesniak.dominik.whattowatch.users.domain.model.commands.CreateUser;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidCredentialsException;

@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository repository;

	private final UserRoleRepository roleRepository;

	private final PasswordEncoder passwordEncoder;

	public UserId createUser(final CreateUser command) {
		final String encodedPassword = passwordEncoder.encode(command.getUserPassword().getValue());
		final User user = new User(command.getUsername(), new UserPassword(encodedPassword));
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
				.filter(user -> passwordEncoder.matches(userPassword.getValue(), user.getUserPassword().getValue()))
				.orElseThrow(() -> new InvalidCredentialsException("Invalid credentials, could not log in.")).getUserId();
	}

	public boolean isUsernameTaken(final Username username) {
		return repository.findBy(username).isPresent();
	}

	public void addRole(final UserId userId, final UserRole role) {
		final User user = repository.findBy(userId).orElseThrow(() -> new ObjectDoesNotExistException("asd"));
		UserRole by = roleRepository.findBy(role.getRoleName()).orElse(role);
		checkUserAlreadyHasRole(by, user);
		user.addRole(by, user);
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

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		return repository.findBy(new Username(username)).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username: " + username));
	}
}
