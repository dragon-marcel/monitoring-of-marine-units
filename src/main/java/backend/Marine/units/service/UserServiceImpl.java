package backend.Marine.units.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import backend.Marine.units.entity.User;
import backend.Marine.units.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	//@Autowired
	//private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;



	@Override
	public void createNewUser(String userName, String password) {
		User user = new User(userName, password);

		userRepository.save(user);

	}

}
