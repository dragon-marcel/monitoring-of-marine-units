package backend.Marine.units.contoler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import backend.Marine.units.entity.AuthRequest;
import backend.Marine.units.entity.AuthResponse;
import backend.Marine.units.entity.User;
import backend.Marine.units.jwt.JWTService;
import backend.Marine.units.repository.UserRepository;
import io.swagger.annotations.ApiOperation;

@RestController
public class AuthController {

	private final AuthenticationManager authorizationManager;

	private final JWTService jWTService;

	public AuthController(AuthenticationManager authorizationManager, JWTService jWTService, UserRepository ur) {
		this.authorizationManager = authorizationManager;
		this.jWTService = jWTService;
	}

	@PostMapping("/api/auth")
	@ApiOperation(value = "Login to system and get token")
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
