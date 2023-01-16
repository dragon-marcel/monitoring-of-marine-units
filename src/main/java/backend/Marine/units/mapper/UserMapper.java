package backend.Marine.units.mapper;

import java.util.List;

import org.mapstruct.factory.Mappers;

import backend.Marine.units.entity.User;
import dto.UserDTO;

public interface UserMapper {
	public static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	UserDTO toUserDTO(User user);
	List<UserDTO> toUserDTOs(List<User> user);
}
