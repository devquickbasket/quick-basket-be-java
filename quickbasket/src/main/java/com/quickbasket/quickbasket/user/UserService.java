package com.quickbasket.quickbasket.user;

import com.quickbasket.quickbasket.customs.Utils.JwtUtil;
import com.quickbasket.quickbasket.customs.response.LoginResponse;
import com.quickbasket.quickbasket.role.Role;
import com.quickbasket.quickbasket.role.RoleRepository;
import com.quickbasket.quickbasket.user.requests.LoginRequest;
import com.quickbasket.quickbasket.user.requests.UserRegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;


@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    public User register(UserRegistrationRequest request){
        try{

            if (!request.getPassword().equals(request.getConfirmPassword())) {
                throw new IllegalArgumentException("Passwords do not match");
            }

            if (userRepository.findByEmail(request.getEmail()).isPresent()){
                throw new IllegalArgumentException("Email already registered on the system");
            }

            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));


            Role defaultRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("role not found"));
            user.setRoles(Set.of(defaultRole));

            return userRepository.save(user);

        }catch (Exception exception){
            log.error("Unable to create user {}",exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }

    }

    public User activateUser(String email){
        User user =userRepository.findByEmail(email).get();

        if(user==null){
            throw new IllegalArgumentException("User not found");
        }

        user.set_email_verified(true);

        user.setStatus(200);

        userRepository.save(user);

        return user;
    }

    public LoginResponse login(LoginRequest loginRequest){
        try{
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Invalid credentials");
            }

            if (!user.getStatus().equals(200)) {
                throw new IllegalStateException("Account is not activated");
            }

            String token = jwtUtil.generateToken(user.getEmail(),user.getFirstName(),user.getStatus(),user.getId());
            LoginResponse loginResponse = new LoginResponse(token,user.getEmail(),user.getStatus(),user.getId(),user.getFirstName());
            user.setLastLogin(LocalDateTime.now());

            return loginResponse;

        }catch (Exception exception){
            log.error("Unable to login user {}",exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }
}
