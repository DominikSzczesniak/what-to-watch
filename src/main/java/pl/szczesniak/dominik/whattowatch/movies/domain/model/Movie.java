package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@ToString
@EqualsAndHashCode
public class Movie {

    @Getter
    private final MovieId movieId;
    @Getter
    private UserId userId;
    @Getter
    private final String title;

    Movie(final MovieId movieId, final String title, UserId userId) {
        this.movieId = movieId;
        this.title = title;
        assignToUser(userId);
    }

    void assignToUser(final UserId id) {
        userId = id;
    }
}
