package pl.szczesniak.dominik.whattowatch.commons.infrastructure.adapters.outgoing.publishers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.commons.domain.DomainEvent;
import pl.szczesniak.dominik.whattowatch.commons.domain.DomainEventsPublisher;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpringApplicationPublisher implements DomainEventsPublisher {


	private final ApplicationEventPublisher springApplicationPublisher;
	private final List<DomainEvent> publishedEvents;

	SpringApplicationPublisher(final ApplicationEventPublisher springApplicationPublisher) {
		this.springApplicationPublisher = springApplicationPublisher;
		this.publishedEvents = new ArrayList<>();
	}

	@Override
	public void publish(final DomainEvent event) {
		springApplicationPublisher.publishEvent(event);
		publishedEvents.add(event);
	}

	@Override
	public List<DomainEvent> getPublishedEvents() {
		return publishedEvents;
	}

}
