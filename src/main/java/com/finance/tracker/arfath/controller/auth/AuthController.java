package com.finance.tracker.arfath.controller.auth;

import com.finance.tracker.arfath.model.ERole;
import com.finance.tracker.arfath.model.User;
import com.finance.tracker.arfath.payload.request.LoginRequest;
import com.finance.tracker.arfath.payload.request.SignupRequest;
import com.finance.tracker.arfath.payload.response.JwtResponse;
import com.finance.tracker.arfath.payload.response.MessageResponse;
import com.finance.tracker.arfath.repository.UserRepository;
import com.finance.tracker.arfath.security.jwt.JwtUtils;
import com.finance.tracker.arfath.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Get user details including first and last name
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        
        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList())));
    }

    @Autowired
    private PasswordValidator passwordValidator;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("UsernameTaken"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("EmailTaken"));
        }
        
        // Additional password validation
        if (!passwordValidator.isValid(signUpRequest.getPassword())) {
            List<String> errors = passwordValidator.getValidationErrors(signUpRequest.getPassword());
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: " + String.join(", ", errors)));
        }

        // Create new user's account
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .password(encoder.encode(signUpRequest.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();

        // Process roles
        Set<String> strRoles = signUpRequest.getRoles();
        Set<String> roles = new HashSet<>();

        // Default to USER role if no roles specified
        if (strRoles == null || strRoles.isEmpty()) {
            roles.add(ERole.ROLE_USER.name());
        } else {
            // Process specified roles
            strRoles.forEach(role -> {
                if ("ADMIN".equalsIgnoreCase(role)) {
                    roles.add(ERole.ROLE_ADMIN.name());
                } else {
                    roles.add(ERole.ROLE_USER.name());
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}