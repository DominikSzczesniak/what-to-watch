package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class MovieId {

    @Getter
    private final int value;
}
