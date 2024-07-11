package com.sparklecow.book.services;

import com.sparklecow.book.dto.user.UserResponseDto;
import com.sparklecow.book.dto.user.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService{

    @Override
    public LocalDateTime findCreationDate(Integer id) {
        return null;
    }

    @Override
    public LocalDateTime findUpdateDate(Integer id) {
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
    public UserResponseDto findByEmail(String email) {
        return null;
    }

    @Override
    public UserResponseDto updateUser(UserUpdateDto userUpdateDto, Integer id) {
        return null;
    }

    @Override
    public void deleteUserById(Integer id) {

    }
}
