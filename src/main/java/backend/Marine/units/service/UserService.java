package backend.Marine.units.service;

import backend.Marine.units.dto.UserDTO;
import backend.Marine.units.entity.User;

public interface UserService {

	public void create(User user);

	public void setEnabled(int id, boolean enabled);

	public void update(int id, UserDTO user);
}
