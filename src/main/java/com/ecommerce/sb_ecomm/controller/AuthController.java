package com.ecommerce.sb_ecomm.controller;

import com.ecommerce.sb_ecomm.jwt_security.JwtUtils;
import com.ecommerce.sb_ecomm.jwt_security.model.LoginRequest;
import com.ecommerce.sb_ecomm.jwt_security.model.MessageResponse;
import com.ecommerce.sb_ecomm.jwt_security.model.SignupRequest;
import com.ecommerce.sb_ecomm.jwt_security.model.UserInfoResponse;
import com.ecommerce.sb_ecomm.jwt_security.services.UserDetailsImpl;
import com.ecommerce.sb_ecomm.model.Role;
import com.ecommerce.sb_ecomm.model.Users;
import com.ecommerce.sb_ecomm.model.UsersRole;
import com.ecommerce.sb_ecomm.repository.RoleRepository;
import com.ecommerce.sb_ecomm.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
                jwtToken, userDetails.getUsername(), roles);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse("Error Username is already taken!")
            );
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.
                    badRequest().
                    body(new MessageResponse("Error Username is already taken!"));
        }

        Users user = new Users(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword())
        );

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userDefaultRole = roleRepository.findByRoleName(UsersRole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException(("Error: Role is not defined")));
            roles.add(userDefaultRole);
        } else {
            // admin -> ROLE_ADMIN
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(UsersRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(("Error: Role is not defined")));
                        roles.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(UsersRole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException(("Error: Role is not defined")));
                        roles.add(sellerRole);
                        break;
                    default:
                        Role userDefaultRole = roleRepository.findByRoleName(UsersRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(("Error: Role is not defined")));
                        roles.add(userDefaultRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
