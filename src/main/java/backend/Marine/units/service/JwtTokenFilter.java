package backend.Marine.units.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
	@Value("${secret.key}")
	private String KEY;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorization = request.getHeader("Authorization");
		if (authorization == null) {
			filterChain.doFilter(request, response);
			return;
		}
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getUsernamePasswordAuthenticationToken(
				authorization);
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		filterChain.doFilter(request, response);
	}

	public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String token) {
		Algorithm algorithm = Algorithm.HMAC256(KEY);
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT jwt = verifier.verify(resolve(token).trim());
		String[] roles = jwt.getClaim("roles").asArray(String.class);
		List<SimpleGrantedAuthority> collect = Stream.of(roles).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		return new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, collect);
	}

	public String resolve(String token) {
		if (token != null && token.startsWith("Bearer")) {
			return token.replace("Bearer", "");
		} else {
			return token;
		}
	}
}
