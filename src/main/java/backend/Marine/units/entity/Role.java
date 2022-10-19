package backend.Marine.units.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
	ADMIN(Code.ADMIN), USER(Code.USER);

	private Role(String authority) {
		this.authority = authority;
	}

	private final String authority;

	@Override
	public String getAuthority() {
		return authority;
	}

	public class Code {
		public static final String ADMIN = "ROLE_ADMIN";
		public static final String USER = "ROLE_USER";
	}
}
