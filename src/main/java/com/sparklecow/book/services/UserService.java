package com.sparklecow.book.services;

import com.sparklecow.book.dto.user.UserResponseDto;
import com.sparklecow.book.dto.user.UserUpdateDto;

public interface UserService extends AuditingService{
    Iterable<UserResponseDto> findAll();
    UserResponseDto findById(Integer id);
    UserResponseDto findByEmail(String email);
    UserResponseDto updateUser(UserUpdateDto userUpdateDto, Integer id);
    void deleteUserById(Integer id);
}
