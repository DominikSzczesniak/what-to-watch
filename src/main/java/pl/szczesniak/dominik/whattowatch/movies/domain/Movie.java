package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@ToString
@EqualsAndHashCode
public class Movie {

    @Getter
    private final MovieId movieId;
    @Getter
    private final UserId userId;
    @Getter
    private final String title;

    public Movie(final MovieId movieId, final String title, UserId userId) {
        this.movieId = movieId;
        this.title = title;
        this.userId = userId;
    }

}
