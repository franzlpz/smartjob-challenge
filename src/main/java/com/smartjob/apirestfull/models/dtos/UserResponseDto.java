package com.smartjob.apirestfull.models.dtos;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserResponseDto {

    private String id;
    private String name;
    private String email;
    private String created;
    private String modified;
    private String lastLogin;
    private String token;
    private Boolean isActive;

}
