package com.smartjob.apirestfull.repositories;

import com.smartjob.apirestfull.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,String> {
    Optional<User> findByEmail(String email);
}
