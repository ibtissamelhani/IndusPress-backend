package org.ibtissam.induspress.service;

import lombok.RequiredArgsConstructor;
import org.ibtissam.induspress.dto.auth.AuthenticationRequest;
import org.ibtissam.induspress.dto.auth.AuthenticationResponse;
import org.ibtissam.induspress.dto.auth.RegisterRequest;
import org.ibtissam.induspress.exception.UserNotFoundException;
import org.ibtissam.induspress.model.Role;
import org.ibtissam.induspress.model.User;
import org.ibtissam.induspress.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.REDACTEUR)
                .build();
        appUserRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = appUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
