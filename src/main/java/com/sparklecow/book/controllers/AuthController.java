package com.sparklecow.book.controllers;

import com.sparklecow.book.dto.user.UserLoginDto;
import com.sparklecow.book.dto.user.UserRegisterDto;
import com.sparklecow.book.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody UserRegisterDto userRegisterDto){
        authenticationService.register(userRegisterDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto){
        return ResponseEntity.ok(authenticationService.login(userLoginDto));
    }
}
