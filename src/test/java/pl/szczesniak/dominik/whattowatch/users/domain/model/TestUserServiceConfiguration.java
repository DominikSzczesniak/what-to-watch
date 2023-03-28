package pl.szczesniak.dominik.whattowatch.users.domain.model;

public class TestUserServiceConfiguration {

    static UserService userService(final UserRepository userRepository) {
        return new UserService(userRepository);
    }
}


