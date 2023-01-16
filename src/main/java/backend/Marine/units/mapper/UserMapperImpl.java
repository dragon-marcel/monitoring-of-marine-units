package backend.Marine.units.mapper;

import java.util.ArrayList;
import java.util.List;

import backend.Marine.units.entity.User;
import dto.UserDTO;

public class UserMapperImpl implements UserMapper {

	@Override
	public UserDTO toUserDTO(User user) {
		System.out.println(user.toString());
		if (user == null) {
			return null;
		}

		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setUsername(user.getUsername());
		userDTO.setEmail(user.getEmail());
		userDTO.setEnable(user.isEnabled());
		userDTO.setRole(user.getRole().name());
		userDTO.setAvatar(user.getAvatar());

		return userDTO;
	}

	@Override
	public List<UserDTO> toUserDTOs(List<User> users) {
		if (users == null) {
			return null;
		}

		List<UserDTO> list = new ArrayList<UserDTO>();
		for (User user : users) {
			list.add(toUserDTO(user));
		}

		return list;
	}

}
