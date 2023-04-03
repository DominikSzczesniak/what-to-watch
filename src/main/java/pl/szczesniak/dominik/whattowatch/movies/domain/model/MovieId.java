package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class MovieId {

    @Getter
    private final int id;

    public MovieId(final int id) {
        this.id = id;
    }
}
