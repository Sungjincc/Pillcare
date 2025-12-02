package com.pillcare.pillcare_server.controller;

import com.pillcare.pillcare_server.dto.LoginRequest;
import com.pillcare.pillcare_server.dto.LoginResponse;
import com.pillcare.pillcare_server.security.TokenProvider;
import com.pillcare.pillcare_server.entity.Userguardian;
import com.pillcare.pillcare_server.repository.UserguardianRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pillcare.pillcare_server.security.AESUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserguardianRepository userRepository;
    private final TokenProvider tokenProvider;

    public AuthController(UserguardianRepository userRepository, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Userguardian user = userRepository.findByID(request.getID());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String encryptedInputPassword = AESUtil.encrypt(request.getPassword());

            if (!user.getPassword().equals(encryptedInputPassword)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String token = tokenProvider.createToken(user.getUserId(), user.getID());
            LoginResponse response = new LoginResponse(user.getUserId(), token, user.getName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
