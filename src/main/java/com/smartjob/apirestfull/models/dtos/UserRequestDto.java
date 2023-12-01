package com.smartjob.apirestfull.models.dtos;

import com.smartjob.apirestfull.config.validation.ValidationConfig;
import com.smartjob.apirestfull.validation.ValidateEmail;
import com.smartjob.apirestfull.validation.ValidatePassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserRequestDto {

        private String id;
        private String name;
        @NotBlank
        @ValidateEmail
        private String email;
        @NotBlank
        @ValidatePassword
        private String password;
        private List<PhoneDto> phones;
}
