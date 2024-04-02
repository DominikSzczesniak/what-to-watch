package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.movies.domain.MoviesFacade;
import pl.szczesniak.dominik.whattowatch.users.domain.model.events.UserDeleted;

@Component
@RequiredArgsConstructor
class MoviesUserDeletedEventListener {

	private final MoviesFacade facade;

	@EventListener(UserDeleted.class)
	public void handleUserDeletedEvent(final UserDeleted event) {
		facade.handleUserDeleted(event.getUserId());
	}

}
