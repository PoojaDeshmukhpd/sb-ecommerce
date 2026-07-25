package com.ecommerce.sb_ecomm.security;


import com.ecommerce.sb_ecomm.jwt_security.AuthEntryPointJwt;
import com.ecommerce.sb_ecomm.jwt_security.AuthTokenFilter;
import com.ecommerce.sb_ecomm.jwt_security.services.UserDetailsServiceImpl;
import com.ecommerce.sb_ecomm.model.Role;
import com.ecommerce.sb_ecomm.model.Users;
import com.ecommerce.sb_ecomm.model.UsersRole;
import com.ecommerce.sb_ecomm.repository.RoleRepository;
import com.ecommerce.sb_ecomm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity for future
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationTokenFilterBean() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProviderBean() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoderBean());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
//                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/carts/**").permitAll()
                                .requestMatchers("/api/cart/**").permitAll()
//                                .requestMatchers("/api/admin/**").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .anyRequest().authenticated());

        http.authenticationProvider(authenticationProviderBean());
        http.addFilterBefore(authenticationTokenFilterBean(),
                UsernamePasswordAuthenticationFilter.class);
        http.headers(headers -> headers.frameOptions(
                HeadersConfigurer.FrameOptionsConfig::sameOrigin
        ));
        return http.build();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            Role userRole = roleRepository.findByRoleName(UsersRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(UsersRole.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });

            Role sellerRole = roleRepository.findByRoleName(UsersRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role newSellerRole = new Role(UsersRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });

            Role adminRole = roleRepository.findByRoleName(UsersRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                            Role newAdminRole = new Role(UsersRole.ROLE_ADMIN);
                            return roleRepository.save(newAdminRole);
                    });

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(sellerRole);
            Set<Role> adminRoles = Set.of(userRole, sellerRole,  adminRole);

            // Create user of not already present
            if(!userRepository.existsByUsername("user1")) {
                Users user1 = new Users("user1","user1@gmail.com", passwordEncoder.encode("userPass1"));
                userRepository.save(user1);
            }

            if(!userRepository.existsByUsername("seller1")) {
                Users user1 = new Users("seller1","seller1@gmail.com", passwordEncoder.encode("sellerPass"));
                userRepository.save(user1);
            }

            if(!userRepository.existsByUsername("admin")) {
                Users user1 = new Users("admin","admin@gmail.com", passwordEncoder.encode("adminPass"));
                userRepository.save(user1);
            }

            // Update Roles for existing Users
            userRepository.findByUsername("user1").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUsername("seller1").ifPresent(seller -> {
                seller.setRoles(sellerRoles);
                userRepository.save(seller);
            });

            userRepository.findByUsername("admin").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });
        };
    }


    // bypass spring security filter at global level
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resorces/**",
                "/swagger-ui.html",
                "webjars/**"
        ));
    }
}
