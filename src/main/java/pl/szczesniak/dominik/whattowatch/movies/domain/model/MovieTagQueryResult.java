package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.Value;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@Value
public class MovieTagQueryResult {

	MovieTagId tagId;

	MovieTagLabel label;

	UserId userId;

}
