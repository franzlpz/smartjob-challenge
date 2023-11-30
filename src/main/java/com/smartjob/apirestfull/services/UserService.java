package com.smartjob.apirestfull.services;

import com.smartjob.apirestfull.models.dtos.UserRequestDto;
import com.smartjob.apirestfull.models.dtos.UserResponseDto;

import java.util.Optional;

public interface UserService extends GenericService<UserResponseDto, String> {
    Optional<UserResponseDto> save(UserRequestDto requestDto);

}
