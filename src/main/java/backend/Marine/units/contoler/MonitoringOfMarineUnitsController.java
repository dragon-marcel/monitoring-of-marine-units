package backend.Marine.units.contoler;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import backend.Marine.units.entity.AuthRequest;
import backend.Marine.units.jwt.JWTService;

@Controller
public class MonitoringOfMarineUnitsController {
	private final AuthenticationManager authorizationManager;
	private final JWTService jWTService;

	public MonitoringOfMarineUnitsController(AuthenticationManager authorizationManager, JWTService jWTService) {
		this.authorizationManager = authorizationManager;
		this.jWTService = jWTService;
	}

	@GetMapping(value = "/")
	public String getIndex(@ModelAttribute AuthRequest authRequest, Model model) {
		// model.addAttribute("authRequest", authRequest);
		return "index";
	}

//	@PostMapping("/")
//	public String greetingSubmit(@ModelAttribute AuthRequest authRequest, Model model) {
//		Authentication authenticate = authorizationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//		User user = (User) authenticate.getPrincipal();
//		String token = jWTService.createToken(authenticate);
//		AuthResponse authResponse = new AuthResponse(user.getUsername(), token);
//		model.addAttribute("token", authResponse.getToken());
//		return "index";
//	}
}
