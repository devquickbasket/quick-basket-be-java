package com.quickbasket.quickbasket.user;

import com.quickbasket.quickbasket.customs.Utils.JwtUtil;
import com.quickbasket.quickbasket.customs.response.LoginResponse;
import com.quickbasket.quickbasket.role.Role;
import com.quickbasket.quickbasket.role.RoleRepository;
import com.quickbasket.quickbasket.user.requests.LoginRequest;
import com.quickbasket.quickbasket.user.requests.UserFilterRequest;
import com.quickbasket.quickbasket.user.requests.UserRegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

            log.info("Request payload {}", request);

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

            if (request.getRole().equalsIgnoreCase("ROLE_AGENT")){
                user.set_email_verified(true);
                user.setStatus(200);
            }

            Role defaultRole = roleRepository.findByName(request.getRole())
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

    public Page<UserResponse> getAllUsers(UserFilterRequest request){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        if (request.getRole() != null && request.getSearch() != null) {
            return userRepository
                    .findByRoleAndSearch(request.getRole(), request.getSearch(),pageable)
                    .map(UserResponse::new);
        }

        // If search keyword exists → search + paginate
        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            return userRepository
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            request.getSearch(), request.getSearch(), request.getSearch(), pageable)
                    .map(UserResponse::new);
        }

        if (request.getRole() != null) {
            return userRepository
                    .findByRole(request.getRole(), pageable)
                    .map(UserResponse::new);
        }

        return userRepository.findAll(pageable).map(UserResponse::new);
    }
}
