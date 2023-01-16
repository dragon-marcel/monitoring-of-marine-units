package backend.Marine.units.jwt;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.Marine.units.entity.User;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	public JWTAuthenticationFilter() {
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/auth", "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}
		if (username != null && password != null && !password.equals("") && !username.equals("")) {
		} else {

			User user = null;
			try {
				user = new ObjectMapper().readValue(request.getInputStream(), User.class);
				username = user.getUsername();

				password = user.getPassword();

			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		username = username.trim();
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
		setDetails(request, authToken);
		return this.getAuthenticationManager().authenticate(authToken);
	}

}
