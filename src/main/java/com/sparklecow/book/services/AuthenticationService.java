package com.sparklecow.book.services;

import com.sparklecow.book.dto.user.UserLoginDto;
import com.sparklecow.book.dto.user.UserRegisterDto;
import com.sparklecow.book.entities.user.User;
import com.sparklecow.book.exceptions.ExpiredTokenException;
import com.sparklecow.book.exceptions.RoleNameNotFoundException;
import com.sparklecow.book.exceptions.TokenNotFoundException;
import com.sparklecow.book.exceptions.ValidatedTokenException;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
    void register(UserRegisterDto userRegisterDto) throws MessagingException, RoleNameNotFoundException;
    String login(UserLoginDto userLoginDto);
    void sendValidation(User user) throws MessagingException;
    String saveAndGenerateToken(User user);
    String generateToken(Integer tokenLength);
    void validateToken(String token) throws MessagingException, TokenNotFoundException, ValidatedTokenException, ExpiredTokenException;
}
