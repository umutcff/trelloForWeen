package com.ironhack.trelloforween.service;

import com.ironhack.trelloforween.dto.AuthRequest;
import com.ironhack.trelloforween.dto.AuthResponse;
import com.ironhack.trelloforween.dto.RegisterRequest;
import com.ironhack.trelloforween.entity.User;
import com.ironhack.trelloforween.exception.EmailAlreadyExistsException;
import com.ironhack.trelloforween.exception.InvalidTokenException;
import com.ironhack.trelloforween.exception.UserNotFoundException;
import com.ironhack.trelloforween.repository.UserRepository;
import com.ironhack.trelloforween.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        
        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse refreshToken(String requestRefreshToken) {
        String userEmail = jwtService.extractUsername(requestRefreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UserNotFoundException("User not found for this token"));
            if (jwtService.isTokenValid(requestRefreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                return AuthResponse.builder()
                        .token(accessToken)
                        .refreshToken(requestRefreshToken)
                        .build();
            }
        }
        throw new InvalidTokenException("Refresh token is invalid or expired");
    }
}
