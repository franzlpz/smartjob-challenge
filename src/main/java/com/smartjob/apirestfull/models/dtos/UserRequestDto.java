package com.smartjob.apirestfull.models.dtos;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserRequestDto {
        private String id;
        private String name;
        private String email;
        private String password;
        private List<PhoneDto> phones;
}
