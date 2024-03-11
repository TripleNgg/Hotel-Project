package org.tripleng.likesidehotel;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tripleng.likesidehotel.model.Role;
import org.tripleng.likesidehotel.model.Room;
import org.tripleng.likesidehotel.model.User;
import org.tripleng.likesidehotel.repository.RoleRepository;
import org.tripleng.likesidehotel.repository.RoomRepository;
import org.tripleng.likesidehotel.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Collections;

@SpringBootApplication
@RequiredArgsConstructor
public class LikeSideHotelApplication implements CommandLineRunner {

	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final DbxClientV2 dbxClientV2;
	public static void main(String[] args) {

		SpringApplication.run(LikeSideHotelApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		roleRepository.save(new Role("ROLE_USER"));
		roleRepository.save(new Role("ROLE_ADMIN"));
		roleRepository.save(new Role("ROLE_EDITOR"));

		User user =  new User();
		user.setId(1L);
		user.setEmail("admin@gmail.com");
		user.setFirstName("Admin");
		user.setLastName("Admin");
		user.setPassword(passwordEncoder.encode("amdin"));
		user.setEnabled(true);
		Role role = roleRepository.findByName("ROLE_ADMIN").get();
		user.setRoles(Collections.singletonList(role));
		userRepository.save(user);
	}
}
