package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Movie {

    @Getter
    private final MovieId movieId;
    @Getter
    private final String title;
    @Getter
    private final UserId userId;

    public static Movie recreate(final MovieId movieId, final String title, final UserId userId) {
        return new Movie(movieId, title, userId);
    }

}
