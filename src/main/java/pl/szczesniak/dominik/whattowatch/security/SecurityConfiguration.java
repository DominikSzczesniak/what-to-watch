package pl.szczesniak.dominik.whattowatch.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import pl.szczesniak.dominik.whattowatch.users.domain.UserFacade;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.Username;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/users").permitAll()
						.anyRequest().authenticated()
				)
				.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		return http.build();
	}

	@Bean
	public AuthenticationManager authManager(final UserDetailsService userDetailsService, final PasswordEncoder passwordEncoder) {
		final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService(final UserFacade userService) {
		return username -> {
			final UserQueryResult userQueryResult = userService.getUserBy(new Username(username));
			return createUserDetails(userQueryResult);
		};
	}

	private UserDetails createUserDetails(final UserQueryResult user) {
		final List<SimpleGrantedAuthority> grantedAuthorities = user.getRoles().stream()
				.map(userRole -> new SimpleGrantedAuthority(userRole.toString()))
				.toList();
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getUserPassword(),
				true,
				true,
				true,
				true,
				grantedAuthorities
		);
	}

}
