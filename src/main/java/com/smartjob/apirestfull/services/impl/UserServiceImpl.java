package com.smartjob.apirestfull.services.impl;

import com.smartjob.apirestfull.config.JwtService;
import com.smartjob.apirestfull.models.Phone;
import com.smartjob.apirestfull.models.Token;
import com.smartjob.apirestfull.models.User;
import com.smartjob.apirestfull.models.dtos.PhoneDto;
import com.smartjob.apirestfull.models.dtos.UserRequestDto;
import com.smartjob.apirestfull.models.dtos.UserResponseDto;
import com.smartjob.apirestfull.models.enums.TokenType;
import com.smartjob.apirestfull.repositories.PhoneRepository;
import com.smartjob.apirestfull.repositories.TokenRepository;
import com.smartjob.apirestfull.repositories.UserRepository;
import com.smartjob.apirestfull.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.server.UID;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> findById(String id) {
        return userRepository.findById(id)
                .map( user -> UserResponseDto.builder()
                .id(user.getId())
                 .created(user.getCreated().toString())
                 .modified(user.getModified().toString())
                 .lastLogin(user.getLastLogin().toString())
                 .token(user.getToken())
                 .isActive(user.getIsActive())
                .build());

    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream()
                .map( user -> UserResponseDto.builder()
                        .id(user.getId())
                        .created(user.getCreated().toString())
                        .modified(user.getModified().toString())
                        .lastLogin(user.getLastLogin().toString())
                        .token(user.getToken())
                        .isActive(user.getIsActive())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<UserResponseDto> save(UserRequestDto requestDto) {
        UID uid = new UID();
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .created(now)
                .modified(now)
                .lastLogin(now)
                .isActive(Boolean.TRUE)
                .build();
        var jwtToken = jwtService.generateToken( user);
        if (!validateEmail(user.getEmail())){
            return Optional.empty();
        }
        user.setToken(jwtToken);
        User userResult = userRepository.save(user);

        registerPhones(userResult, requestDto.getPhones());
        saveUserToken(userResult, jwtToken);

        return Optional.of(UserResponseDto.builder()
                .id(userResult.getId())
                .created(userResult.getCreated().toString())
                .modified(userResult.getModified().toString())
                .lastLogin(userResult.getLastLogin().toString())
                .token(userResult.getToken())
                .isActive(userResult.getIsActive())
                .build());

    }


    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    private void registerPhones(User user, List<PhoneDto> phones) {
        phones.forEach( dto -> {
            phoneRepository.save(Phone.builder()
                    .userId(user.getId())
                    .number(dto.getNumber())
                    .cityCode(dto.getCityCode())
                    .countryCode(dto.getCountryCode())
                    .build());
        });
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
    private Boolean validateEmail(String email){
        return userRepository.findByEmail(email).isEmpty();
    }
}
