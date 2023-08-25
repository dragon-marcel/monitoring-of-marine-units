package backend.Marine.units.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.Marine.units.dto.UserDTO;
import backend.Marine.units.entity.Role;
import backend.Marine.units.entity.User;
import backend.Marine.units.exception.UserNotFoundException;
import backend.Marine.units.mapper.UserMapper;
import backend.Marine.units.repository.UserRepository;
import backend.Marine.units.service.UserService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

	private final UserService userService;
	private final UserRepository userRepository;

	public UserController(UserService userService, UserRepository userRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	@ApiOperation(value = "Create user")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		userService.create(user);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping(value = "/{id}/{enabled}")
	@ApiOperation("Set user's enabled status")
	public ResponseEntity<?> setEnabledUser(@PathVariable int id, @PathVariable boolean enabled) {
		userService.setEnabled(id, enabled);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping(value = "/{id}")
	@ApiOperation(value = "Update user")
	public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO, @PathVariable int id) {
		userService.update(id, userDTO);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping(value = "/{id}")
	@ApiOperation(value = "Get user by ID")
	public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {

		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		UserDTO userDTO = UserMapper.INSTANCE.toUserDTO(user);

		return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
	}

	@GetMapping
	@ApiOperation(value = "Get all users")
	public ResponseEntity<List<UserDTO>> getUsers() {
		List<UserDTO> users = UserMapper.INSTANCE.toUserDTOs(userRepository.findAll());

		return new ResponseEntity<List<UserDTO>>(users, HttpStatus.OK);
	}

	@GetMapping(value = "/roles")
	@ApiOperation("Get user roles")
	public ResponseEntity<List<?>> getUsersRole() {

		return new ResponseEntity<List<?>>(Arrays.asList(Role.values()), HttpStatus.OK);
	}

//	@PatchMapping("/{username}/{mmsi}")
//	@ApiOperation(value = "Add tracked ship")
//	public ResponseEntity<?> trackedShip(@PathVariable int mmsi, @PathVariable String username) {
//		userService.addTrackedShip(username, mmsi);
//
//		return ResponseEntity.status(HttpStatus.OK).build();
//
//	}
}
