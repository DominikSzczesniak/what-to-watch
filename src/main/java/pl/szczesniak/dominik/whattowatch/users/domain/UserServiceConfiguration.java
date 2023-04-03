package pl.szczesniak.dominik.whattowatch.users.domain;

public class UserServiceConfiguration {

    public UserService userService(final UserRepository userRepository) {
        return new UserService(userRepository);
    }
}
