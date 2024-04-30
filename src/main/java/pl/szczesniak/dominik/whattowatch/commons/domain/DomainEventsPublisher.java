package pl.szczesniak.dominik.whattowatch.commons.domain;


import java.util.List;

public interface DomainEventsPublisher {

	void publish(DomainEvent event);

	void publish(List<DomainEvent> events);

}
