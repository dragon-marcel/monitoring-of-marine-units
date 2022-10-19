package backend.Marine.units.configuration;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import backend.Marine.units.entity.User;
import backend.Marine.units.repository.UserRepository;
import backend.Marine.units.service.JwtTokenFilter;

@Configuration
public class SecurityConfig {
	private final UserRepository userRepo;
	private final JwtTokenFilter jwtTokenFilter;

	public SecurityConfig(JwtTokenFilter jwtTokenFilter, UserRepository userRepo) {

		this.jwtTokenFilter = jwtTokenFilter;
		this.userRepo = userRepo;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void saveUser() {
		userRepo.save(new User("Marcel", getBcryptPasswordEncoder().encode("admin123")));
	}

	@Bean
	public UserDetailsService userDetailsService() {

		return username -> userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with username not found: " + username));

	}

	@Bean
	public PasswordEncoder getBcryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authorizationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {

		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
		http.authorizeRequests().antMatchers("/auth/login", "/auth/regist").permitAll().antMatchers("/hello")
				.hasRole("USER").anyRequest().authenticated();
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();

	}
}
