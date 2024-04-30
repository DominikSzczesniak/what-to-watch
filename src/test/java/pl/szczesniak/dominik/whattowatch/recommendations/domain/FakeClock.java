package pl.szczesniak.dominik.whattowatch.recommendations.domain;

import lombok.Setter;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

import static java.util.Optional.ofNullable;

class FakeClock extends Clock {

	@Setter
	static Instant fixedTime = null;

	public void simulateWeeksIntoFuture(long weeks) {
		fixedTime = Optional.ofNullable(fixedTime).orElseGet(Instant::now).plusSeconds(weeks * 7 * 24 * 60 * 60);
	}

	@Override
	public ZoneId getZone() {
		return ZoneId.systemDefault();
	}

	@Override
	public Clock withZone(final ZoneId zone) {
		return this;
	}

	@Override
	public Instant instant() {
		return ofNullable(fixedTime).orElseGet(Instant::now);
	}

}
