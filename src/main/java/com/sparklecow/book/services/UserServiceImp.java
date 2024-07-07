package com.sparklecow.book.services;

import com.sparklecow.book.dto.user.UserLoginDto;
import com.sparklecow.book.dto.user.UserRegisterDto;
import com.sparklecow.book.dto.user.UserResponseDto;
import com.sparklecow.book.dto.user.UserUpdateDto;
import com.sparklecow.book.entities.Role;
import com.sparklecow.book.entities.Token;
import com.sparklecow.book.entities.User;
import com.sparklecow.book.repositories.RoleRepository;
import com.sparklecow.book.repositories.TokenRepository;
import com.sparklecow.book.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class UserServiceImp implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    @Override
    public String login(UserLoginDto userLoginDto) {
        return null;
    }

    @Override
    public void register(UserRegisterDto userRegisterDto) {
        Role role = roleRepository.findByName("USER").orElseThrow( ()-> new RuntimeException(""));
        User user = User.builder()
                .firstName(userRegisterDto.firstName())
                .lastName(userRegisterDto.lastName())
                .email(passwordEncoder.encode(userRegisterDto.email()))
                .password(userRegisterDto.password())
                .accountLocked(false)
                .roles(List.of(role))
                .enabled(false)
                .build();
        userRepository.save(user);
        sendValidation(user);
    }

    @Override
    public void sendValidation(User user) {
        String token = saveAndGenerateToken(user);
    }

    @Override
    public String saveAndGenerateToken(User user) {
        String tokenInfo = generateToken(6);
        Token token = Token.builder()
                .token(tokenInfo)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();
        tokenRepository.save(token);
        return tokenInfo;
    }

    @Override
    public String generateToken(Integer tokenLength) {
        String characters = "0123456789";
        StringBuilder token = new StringBuilder();
        SecureRandom sr = new SecureRandom();
        for (int i = 0; i < tokenLength; i++) {
            int randomIndex = sr.nextInt(characters.length());
            token.append(characters.charAt(randomIndex));
        }
        return token.toString();
    }

    @Override
    public UserResponseDto create(UserRegisterDto userRegisterDto) {
        return null;
    }

    @Override
    public Iterable<UserResponseDto> findAll() {
        return null;
    }

    @Override
    public UserResponseDto findById(Integer id) {
        return null;
    }

    @Override
    public void updateById(UserUpdateDto userUpdateDto, Integer id) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public LocalDateTime findCreationDate(Integer id) {
        return null;
    }

    @Override
    public LocalDateTime findUpdateDate(Integer id) {
        return null;
    }
}
