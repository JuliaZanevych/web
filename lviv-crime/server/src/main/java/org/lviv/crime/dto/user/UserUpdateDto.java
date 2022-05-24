package org.lviv.crime.dto.user;

import lombok.Data;

@Data
public class UserUpdateDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
    private String info;
}
