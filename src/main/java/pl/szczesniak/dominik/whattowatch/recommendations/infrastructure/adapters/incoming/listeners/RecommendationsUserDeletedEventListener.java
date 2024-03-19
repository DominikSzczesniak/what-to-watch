package pl.szczesniak.dominik.whattowatch.recommendations.infrastructure.adapters.incoming.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.recommendations.domain.RecommendationFacade;
import pl.szczesniak.dominik.whattowatch.users.domain.model.events.UserDeleted;

@Component
@RequiredArgsConstructor
public class RecommendationsUserDeletedEventListener {

	private final RecommendationFacade facade;

	@EventListener(UserDeleted.class)
	public void handleUserDeletedEvent(UserDeleted event) {
		facade.handleUserDeleted(event.getUserId());
	}

}
