package backend.Marine.units.service;

import backend.Marine.units.dto.UserDTO;
import backend.Marine.units.entity.User;

public interface UserService {

    void create(User user);

    void setEnabled(int id, boolean enabled);

    void update(int id, UserDTO user);
}
