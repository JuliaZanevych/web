package org.lviv.crime.entity.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("users")
public class UserUpdateEntity {
    @Id
    private Long id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String phone;

    private String info;

}
