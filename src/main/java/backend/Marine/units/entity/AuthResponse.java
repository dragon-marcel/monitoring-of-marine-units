package backend.Marine.units.entity;

import lombok.Data;

@Data
public class AuthResponse {

	private String userName;
	private String token;

	public AuthResponse(String userName, String token) {
		this.userName = userName;
		this.token = token;
	}
}
