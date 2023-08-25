package backend.Marine.units.controller;

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

	private final JWTService jwtService;

	public AuthController(AuthenticationManager authorizationManager, JWTService jwtService,
			UserRepository userRepository) {
		this.authorizationManager = authorizationManager;
		this.jwtService = jwtService;
	}

	@PostMapping("/api/auth")
	@ApiOperation(value = "Login to the system and get an authentication token")
	public ResponseEntity<?> authenticateAndGenerateToken(@RequestBody AuthRequest authRequest) {
		try {
			Authentication authenticate = authorizationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
			User user = (User) authenticate.getPrincipal();
			String token = jwtService.createToken(authenticate);
			AuthResponse authResponse = new AuthResponse(user.getUsername(), token);
			return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
		} catch (UsernameNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

}
