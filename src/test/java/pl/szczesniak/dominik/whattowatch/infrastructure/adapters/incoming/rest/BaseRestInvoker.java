package pl.szczesniak.dominik.whattowatch.infrastructure.adapters.incoming.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import pl.szczesniak.dominik.whattowatch.security.LoggedUserProvider.LoggedUser;

@Component
@RequiredArgsConstructor
public class BaseRestInvoker {

	public final TestRestTemplate restTemplate;

	public void addSessionIdandUserIdHeaders(final HttpHeaders headers, final LoggedUser loggedUser) {
		headers.put(HttpHeaders.COOKIE, loggedUser.getSessionId());
		headers.set("userId", String.valueOf(loggedUser.getUserId()));
	}

}
