package com.finance.tracker.arfath.controller.auth;

import com.finance.tracker.arfath.model.ERole;
import com.finance.tracker.arfath.model.User;
import com.finance.tracker.arfath.payload.request.SignupRequest;
import com.finance.tracker.arfath.payload.response.MessageResponse;
import com.finance.tracker.arfath.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminCreationController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    /**
     * Creates an admin user. This endpoint should be secured or disabled in production.
     * It's intended for initial setup only.
     */
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdminUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create admin user
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .password(encoder.encode(signUpRequest.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();

        // Assign ADMIN role
        Set<String> roles = new HashSet<>();
        roles.add(ERole.ROLE_ADMIN.name());
        roles.add(ERole.ROLE_USER.name()); // Admin also has user privileges
        user.setRoles(roles);
        
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Admin user created successfully!"));
    }
    
    /**
     * Promotes an existing user to admin role.
     * This endpoint requires admin privileges.
     */
    @PutMapping("/promote/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> promoteToAdmin(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Set<String> roles = user.getRoles();
        roles.add(ERole.ROLE_ADMIN.name());
        user.setRoles(roles);
        
        userRepository.save(user);
        
        return ResponseEntity.ok(new MessageResponse("User promoted to admin successfully!"));
    }
}