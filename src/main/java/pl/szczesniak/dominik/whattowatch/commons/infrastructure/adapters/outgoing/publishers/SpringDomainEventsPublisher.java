package pl.szczesniak.dominik.whattowatch.commons.infrastructure.adapters.outgoing.publishers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.commons.domain.DomainEvent;
import pl.szczesniak.dominik.whattowatch.commons.domain.DomainEventsPublisher;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SpringDomainEventsPublisher implements DomainEventsPublisher {


	private final ApplicationEventPublisher springApplicationPublisher;

	@Override
	public void publish(final DomainEvent event) {
		springApplicationPublisher.publishEvent(event);
	}

	@Override
	public void publish(final List<DomainEvent> events) {
		events.forEach(springApplicationPublisher::publishEvent);
	}

}
