package pl.szczesniak.dominik.whattowatch.commons.infrastructure.adapters.outgoing.publishers;

import pl.szczesniak.dominik.whattowatch.commons.domain.DomainEvent;
import pl.szczesniak.dominik.whattowatch.commons.domain.DomainEventsPublisher;
import pl.szczesniak.dominik.whattowatch.users.domain.model.events.UserDeleted;

import java.util.List;

public class InMemoryEventPublisher implements DomainEventsPublisher {

	private UserDeleted publishedEvent = null;

	@Override
	public void publish(final DomainEvent event) {
		this.publishedEvent = (UserDeleted) event;
	}

	@Override
	public List<DomainEvent> getPublishedEvents() {
		return List.of(publishedEvent);
	}

}
