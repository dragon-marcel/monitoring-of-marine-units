package backend.Marine.units.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import backend.Marine.units.dto.UserDTO;
import backend.Marine.units.entity.User;

public class UserMapperImpl implements UserMapper {

	@Override
	public UserDTO toUserDTO(User user) {
		if (user == null)
			throw new IllegalArgumentException("User cannot be null");

		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setUsername(user.getUsername());
		userDTO.setEmail(user.getEmail());
		userDTO.setEnable(user.isEnabled());
		userDTO.setRole(user.getRole() != null ? user.getRole().name() : null);
		userDTO.setAvatar(user.getAvatar());

		return userDTO;
	}

	@Override
	public List<UserDTO> toUserDTOs(List<User> users) {
		if (users == null)
			return Collections.emptyList();

		List<UserDTO> usersDTO = new ArrayList<UserDTO>();
		users.forEach(user -> usersDTO.add(toUserDTO(user)));

		return usersDTO;
	}

}
