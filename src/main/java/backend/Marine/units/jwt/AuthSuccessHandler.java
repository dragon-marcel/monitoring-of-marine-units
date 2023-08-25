package backend.Marine.units.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import backend.Marine.units.entity.User;
import backend.Marine.units.repository.UserRepository;

@Component
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JWTService jWTService;
	private final UserRepository userRepository;
	public AuthSuccessHandler(JWTService jWTService,UserRepository userRepository){
		this.jWTService =jWTService;
		this.userRepository =userRepository;
	}
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String token = jWTService.createToken(authentication);
		UserDetails principal = (UserDetails) authentication.getPrincipal();
		User user = userRepository.findByUsernameIgnoreCase(principal.getUsername()).orElse(null);
		response.addHeader("Authoriazation", "Bearer " + token);
		Map<String, Object> body = new HashMap<>();
		body.put("username", user.getUsername());
		body.put("id", user.getId());
		body.put("token", token);
		body.put("avatar", user.getAvatar());
		body.put("role", user.getRole().name());
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(200);
		response.setContentType("application/json");
	}

}
