package com.sparklecow.book.services;

import com.sparklecow.book.config.jwt.JwtUtils;
import com.sparklecow.book.dto.user.UserLoginDto;
import com.sparklecow.book.dto.user.UserRegisterDto;
import com.sparklecow.book.entities.role.Role;
import com.sparklecow.book.entities.token.Token;
import com.sparklecow.book.entities.user.User;
import com.sparklecow.book.exceptions.ExpiredTokenException;
import com.sparklecow.book.exceptions.RoleNameNotFoundException;
import com.sparklecow.book.exceptions.TokenNotFoundException;
import com.sparklecow.book.exceptions.ValidatedTokenException;
import com.sparklecow.book.models.EmailTemplateName;
import com.sparklecow.book.repositories.RoleRepository;
import com.sparklecow.book.repositories.TokenRepository;
import com.sparklecow.book.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthenticationService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    @Value("${application.mailing.activation-url}")
    private String activationUrl;

    @Override
    public String login(UserLoginDto userLoginDto) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userLoginDto.email(), userLoginDto.password());
        authenticationManager.authenticate(authToken);
        UserDetails user = userRepository.findByEmail(userLoginDto.email()).orElseThrow(()->
                new UsernameNotFoundException("User with email or username "+userLoginDto.email()+" not found"));
        return jwtUtils.createToken(user);
    }
    @Override
    public void register(UserRegisterDto userRegisterDto) throws MessagingException, RoleNameNotFoundException {
        Role role = roleRepository.findByName("USER").orElseThrow( ()->
                new RoleNameNotFoundException("Role with name USER not found"));
        //Handle error
        User user = User.builder()
                .firstName(userRegisterDto.firstName())
                .lastName(userRegisterDto.lastName())
                .email(userRegisterDto.email())
                .password(passwordEncoder.encode(userRegisterDto.password()))
                .accountLocked(false)
                .roles(List.of(role))
                .enabled(false)
                .build();
        userRepository.save(user);
        sendValidation(user);
    }

    @Override
    public void sendValidation(User user) throws MessagingException {
        String token = saveAndGenerateToken(user);
        emailService.sendEmail(user.getEmail(), "Account activation", user.getFullName(),
                token, activationUrl, EmailTemplateName.ACTIVATE_ACCOUNT);
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
    @Transactional
    public void validateToken(String token) throws MessagingException, TokenNotFoundException, ValidatedTokenException, ExpiredTokenException {
        Token tokenResult = tokenRepository.findByToken(token).orElseThrow(()->
                new TokenNotFoundException("Token not found"));
        if(tokenResult.getValidatedAt()!=null){
            throw new ValidatedTokenException("token has been validated before");
        }
        if(tokenResult.getExpiresAt().isBefore(LocalDateTime.now())){
            sendValidation(tokenResult.getUser());
            throw new ExpiredTokenException("token has expired");
        }
        tokenResult.setValidatedAt(LocalDateTime.now());
        User user = tokenResult.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.save(tokenResult);
    }
}
