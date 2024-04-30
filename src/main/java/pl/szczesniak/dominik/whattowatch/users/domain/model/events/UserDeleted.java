package pl.szczesniak.dominik.whattowatch.users.domain.model.events;

import lombok.NonNull;
import lombok.Value;
import pl.szczesniak.dominik.whattowatch.commons.domain.DomainEvent;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@Value
public class UserDeleted implements DomainEvent {

	@NonNull UserId userId;

}
