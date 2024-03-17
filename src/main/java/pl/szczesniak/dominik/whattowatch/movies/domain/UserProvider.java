package pl.szczesniak.dominik.whattowatch.movies.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

interface UserProvider {

	boolean exists(UserId userId);

}