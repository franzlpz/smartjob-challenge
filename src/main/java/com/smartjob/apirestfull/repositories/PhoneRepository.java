package com.smartjob.apirestfull.repositories;

import com.smartjob.apirestfull.models.Phone;
import org.springframework.data.repository.CrudRepository;

public interface PhoneRepository extends CrudRepository<Phone,String> {
}
