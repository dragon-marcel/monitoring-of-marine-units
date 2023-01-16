package backend.Marine.units.jwt;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import backend.Marine.units.entity.User;

@Service
public class JWTService {
	@Value("${secret.key}")
	private String KEY;

	public String createToken(Authentication authentication) {
		User user = (User) authentication.getPrincipal();

		Algorithm algorithm = Algorithm.HMAC256(KEY);
		return JWT.create().withSubject(user.getUsername()).withIssuer("MARCEL")
				.withClaim("roles",
						user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algorithm);

	}

}
