package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.Getter;

public class User {

    @Getter
    private final UserId id;
    @Getter
    private final String name;

    public User(final String name) {
        this.name = name;
        id = new UserId();
    }
}
