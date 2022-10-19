package backend.Marine.units.contoler;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import backend.Marine.units.entity.AuthRequest;
import backend.Marine.units.entity.AuthResponse;
import backend.Marine.units.entity.User;
import backend.Marine.units.repository.UserRepository;
import backend.Marine.units.service.JWTService;

@RestController
public class AuthController {

	private final AuthenticationManager authorizationManager;

	private final JWTService jWTService;

	private UserRepository ur;

	public AuthController(AuthenticationManager authorizationManager, JWTService jWTService, UserRepository ur) {
		this.authorizationManager = authorizationManager;
		this.jWTService = jWTService;
		this.ur = ur;
	}

	@GetMapping("/auth/regist")
	public String gets() {
		Optional<backend.Marine.units.entity.User> u = ur.findByUsername("Marcel");
		return u.toString();
	}

	@PostMapping("/auth/login")
	public ResponseEntity<?> getJWT(@RequestBody AuthRequest authRequest) {
		try {
			Authentication authenticate = authorizationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
			User user = (User) authenticate.getPrincipal();
			String token = jWTService.createToken(authenticate);
			AuthResponse authResponse = new AuthResponse(user.getUsername(), token);
			return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
		} catch (UsernameNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

}
