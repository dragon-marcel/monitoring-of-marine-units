package backend.Marine.units.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import backend.Marine.units.entity.User;
import backend.Marine.units.exception.UserNotFoundException;
import backend.Marine.units.exception.UserUniqueException;
import backend.Marine.units.mail.ContainerObserverShip;
import backend.Marine.units.repository.UserRepository;
import dto.UserDTO;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContainerObserverShip containerObserverShip;

	@EventListener(ApplicationReadyEvent.class)
	public void saveUser() {
		User user = new User("admin", passwordEncoder.encode("admin"), "dragon.marcel@o2.pl", "ADMIN", true, "1");
		userRepository.save(user);
		containerObserverShip.addObserverShip(user);

	}

	@Override
	public void create(User user) {
		if (userRepository.findByUsernameIgnoreCase(user.getUsername()).orElse(null) != null)
			throw new UserUniqueException(user.getUsername());

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);

		if (user.isEnabled())
			containerObserverShip.addObserverShip(user);

	}

	@Override
	public void setEnabled(int id, boolean enabled) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		user.setEnabled(enabled);
		userRepository.save(user);

		if (user.isEnabled())
			containerObserverShip.addObserverShip(user);
		else
			containerObserverShip.removeObserverShip(user);
	}

	@Override
	public void update(int id, UserDTO userDTO) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		User uniqueUser = userRepository.findByUsernameIgnoreCase(userDTO.getUsername()).orElse(null);
		if (uniqueUser != null && uniqueUser.getId() != id)
			throw new UserUniqueException(user.getUsername());
		user.setUsername(userDTO.getUsername());
		user.setEmail(userDTO.getEmail());
		user.setAvatar(userDTO.getAvatar());
		user.setRole(userDTO.getRole());
		user.setEnabled(userDTO.isEnabled());
		userRepository.save(user);

		if (user.isEnabled())
			containerObserverShip.addObserverShip(user);
		else
			containerObserverShip.removeObserverShip(user);

	}

}
