package com.smartjob.apirestfull.controllers;

import com.smartjob.apirestfull.http.ServiceResponse;
import com.smartjob.apirestfull.models.dtos.UserRequestDto;
import com.smartjob.apirestfull.models.dtos.UserResponseDto;
import com.smartjob.apirestfull.services.UserService;
import com.smartjob.apirestfull.utils.Constants;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResponse> getAll() {
        try {
            List<UserResponseDto> users = service.findAll();

            if (users.isEmpty())
                return new ResponseEntity<>(ServiceResponse.noContent(UserResponseDto.builder().build()), HttpStatus.NO_CONTENT);

            return ResponseEntity.ok().body(ServiceResponse.ok(users));

        } catch (DataAccessException e) {
            return new ResponseEntity<>(ServiceResponse.internalError(e.getMostSpecificCause().getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResponse> getById(@PathVariable String id) {

        try {
            Optional<UserResponseDto> user = service.findById(id);

            return user.map(userResponseDto ->
                    ResponseEntity.ok().body(ServiceResponse.ok(userResponseDto))).orElseGet(() ->
                    new ResponseEntity<>(ServiceResponse.noContent(UserResponseDto.builder().build()), HttpStatus.NO_CONTENT));

        } catch (DataAccessException e) {
            return new ResponseEntity<>(ServiceResponse.internalError(e.getMostSpecificCause().getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResponse> save(@RequestBody UserRequestDto dto) {
        try {
            var result = service.save(dto);
            if (result.isEmpty())
                return new ResponseEntity<>(ServiceResponse.general(Constants.MESSAGE_EMAIL_EXIST), HttpStatus.GONE);

            return new ResponseEntity<>( ServiceResponse.ok(result),
                    HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(ServiceResponse.internalError(e.getMostSpecificCause().getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
