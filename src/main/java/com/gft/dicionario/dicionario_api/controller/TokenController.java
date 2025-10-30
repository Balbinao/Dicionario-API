package com.gft.dicionario.dicionario_api.controller;


import com.gft.dicionario.dicionario_api.dto.LoginRequest;
import com.gft.dicionario.dicionario_api.dto.LoginResponse;
import com.gft.dicionario.dicionario_api.entities.Role;
import com.gft.dicionario.dicionario_api.entities.User;
import com.gft.dicionario.dicionario_api.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        var userLog = userRepository.findByUsername(loginRequest.username());

        if (userLog.isEmpty() || !userLog.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("User or password is invalid!");
        }

        var user = userLog.get();
        var now = Instant.now();
        var expiresIn = 3600L; // 1 hora

        // Roles como lista de strings
        List<String> roles = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("roles", roles)  // claim roles
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(token, expiresIn));
    }
}