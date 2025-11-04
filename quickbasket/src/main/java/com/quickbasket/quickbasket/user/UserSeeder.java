package com.quickbasket.quickbasket.user;

import com.quickbasket.quickbasket.role.Role;
import com.quickbasket.quickbasket.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    @Override
    public void run(String... args) throws Exception {

        if (userRepository.findByEmail("admin@quickbasket.com").isPresent()) {
            return;
        }

        // ✅ Ensure ADMIN role exists
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Admin Role Not Found"));

        // ✅ Create admin user
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setStatus(200);
        user.setEmail("admin@quickbasket.com");
        user.setPassword(passwordEncoder.encode("admin")); // ✅ Encrypt password
        user.set_email_verified(true);
        user.setRoles(Set.of(adminRole)); // ✅ Assign ADMIN Role

        userRepository.save(user);
    }
}
