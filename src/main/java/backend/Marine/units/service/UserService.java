package backend.Marine.units.service;

import backend.Marine.units.entity.User;
import dto.UserDTO;

public interface UserService {

	public void create(User user);

	public void setEnabled(int id, boolean enabled);

	public void update(int id, UserDTO user);
}
