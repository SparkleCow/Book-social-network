package com.sparklecow.book.services;

import com.sparklecow.book.dto.user.UserLoginDto;
import com.sparklecow.book.dto.user.UserRegisterDto;
import com.sparklecow.book.entities.User;

public interface AuthenticationService {
    void register(UserRegisterDto userRegisterDto);
    String login(UserLoginDto userLoginDto);
    void sendValidation(User user);
    String saveAndGenerateToken(User user);
    String generateToken(Integer tokenLength);
}
