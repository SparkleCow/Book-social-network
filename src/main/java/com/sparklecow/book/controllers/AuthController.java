package com.sparklecow.book.controllers;

import com.sparklecow.book.dto.user.UserLoginDto;
import com.sparklecow.book.dto.user.UserRegisterDto;
import com.sparklecow.book.services.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication management endpoints")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody UserRegisterDto userRegisterDto) throws MessagingException {
        authenticationService.register(userRegisterDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto){
        return ResponseEntity.ok(authenticationService.login(userLoginDto));
    }

    @PostMapping("/validate")
    public ResponseEntity<HttpStatus> validate(@RequestParam String token) throws MessagingException {
        authenticationService.validateToken(token);
        return ResponseEntity.ok().build();
    }
}
