package org.lviv.crime.dto.user;

import lombok.Data;

@Data
public class UserItemDto {
    private Long id;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String email;
}
