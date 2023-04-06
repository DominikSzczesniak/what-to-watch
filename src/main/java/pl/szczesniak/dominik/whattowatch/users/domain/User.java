package pl.szczesniak.dominik.whattowatch.users.domain;

import lombok.Getter;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvlaidUserIdException;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.InvalidUsernameException;

@ToString
//@RequiredArgsConstructor
//@NonNull
public class User {

    @Getter
    private final String userName;
    @Getter
    private final UserId userId;

    public User(final String userName, final UserId userId) {
        if (userName == null) {
            throw new InvalidUsernameException("Username cannot be null");
        }
        if (userId == null) {
            throw new InvlaidUserIdException("UserId cannot be null");
        }
        this.userName = userName;
        this.userId = userId;
    }
}
