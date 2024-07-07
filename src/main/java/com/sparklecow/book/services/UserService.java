package com.sparklecow.book.services;

import com.sparklecow.book.dto.user.UserRegisterDto;
import com.sparklecow.book.dto.user.UserResponseDto;
import com.sparklecow.book.dto.user.UserUpdateDto;

public interface UserService extends CrudService<UserRegisterDto, UserResponseDto, UserUpdateDto>,
                                     AuditingService, AuthenticationService{
}
