package org.lviv.crime.dto.user;

import lombok.Data;

@Data
public class UserCreateDto extends UserUpdateDto {
    private String username;
    private String password;
}
