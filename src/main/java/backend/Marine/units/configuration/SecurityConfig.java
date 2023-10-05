package backend.Marine.units.configuration;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import backend.Marine.units.jwt.AuthFailureHandler;
import backend.Marine.units.jwt.AuthSuccessHandler;
import backend.Marine.units.jwt.JWTAuthenticationFilter;
import backend.Marine.units.jwt.JWTTokenFilter;

@Configuration
public class SecurityConfig {

	private final AuthenticationManager authenticationManager;
	private final AuthSuccessHandler authSuccessHandler;
	private final AuthFailureHandler authFailureHandler;

	private final JWTTokenFilter jwtTokenFilter;
	private static final String[] AUTH_WHITELIST = { "/ws-ships/**", "/city/**", "/ship/**", "/avatar/**",
			"/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**" };

	public SecurityConfig(JWTTokenFilter jwtTokenFilter, AuthenticationManager authenticationManager,
			AuthSuccessHandler authSuccessHandler, AuthFailureHandler authFailureHandler) {
		this.authenticationManager = authenticationManager;
		this.authSuccessHandler = authSuccessHandler;
		this.authFailureHandler = authFailureHandler;
		this.jwtTokenFilter = jwtTokenFilter;
	}

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.applyPermitDefaultValues();
		configuration.addAllowedMethod(HttpMethod.PUT);
		configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000"));
		configuration.setAllowCredentials(true);

		http.csrf().disable();
		http.cors().configurationSource(request -> configuration);
		http.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated().and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.addFilter(authenticationFilter());

		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();

	}

	@Bean
	public JWTAuthenticationFilter authenticationFilter() {
		JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter();
		authenticationFilter.setAuthenticationSuccessHandler(authSuccessHandler);
		authenticationFilter.setAuthenticationFailureHandler(authFailureHandler);
		authenticationFilter.setAuthenticationManager(authenticationManager);
		return authenticationFilter;
	}

}
