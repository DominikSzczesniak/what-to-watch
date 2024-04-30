package pl.szczesniak.dominik.whattowatch.commons.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.events.UserDeleted;

import java.util.ArrayList;
import java.util.List;

public class InMemoryEventPublisher implements DomainEventsPublisher {

	private final List<UserDeleted> publishedEvent = new ArrayList<>();

	@Override
	public void publish(final DomainEvent event) {
		publishedEvent.add((UserDeleted) event);
	}

	@Override
	public void publish(final List<DomainEvent> events) {
		events.forEach(event -> publishedEvent.add((UserDeleted) event));
	}

	public List<UserDeleted> getPublishedEvents() {
		return publishedEvent;
	}

}
