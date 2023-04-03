package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class User {

    @Getter
    private final String userName;
    @Getter
    private final UserId userId;

    public User(final String userName, final UserId userId) {
        this.userName = userName;
        this.userId = userId;
    }
}
