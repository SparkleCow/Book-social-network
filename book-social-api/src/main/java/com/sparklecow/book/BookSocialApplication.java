package com.sparklecow.book;

import com.sparklecow.book.entities.role.Role;
import com.sparklecow.book.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class BookSocialApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookSocialApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository){
		return args -> {
			Optional<Role> role = roleRepository.findByName("USER");
			if(role.isPresent()){
				return;
			}
			roleRepository.save(Role.builder().name("USER").build());
		};
	}
}
