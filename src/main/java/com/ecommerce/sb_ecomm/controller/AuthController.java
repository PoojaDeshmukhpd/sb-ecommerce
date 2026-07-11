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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(), roles);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                jwtCookie.toString()).body(response);
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

    @GetMapping("/username")
    public String currentUserName(Authentication authetication) {
        if (authetication != null) {
            return authetication.getName();
        }
        return "Null";
    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getUserDetails(Authentication authetication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authetication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(), roles);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser(Authentication authentication) {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse(
                "You have been signed out!"
        ));
    }
}
