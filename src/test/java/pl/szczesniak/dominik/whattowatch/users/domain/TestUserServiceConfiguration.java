package pl.szczesniak.dominik.whattowatch.users.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.UserRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.UserService;

public class TestUserServiceConfiguration {

    static UserService userService(final UserRepository userRepository) {
        return new UserService(userRepository);
    }
}


