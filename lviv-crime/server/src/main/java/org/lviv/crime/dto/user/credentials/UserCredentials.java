package org.lviv.crime.dto.user.credentials;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserCredentials {

    @ApiModelProperty(example = "julia47")
    private String username;

    @ApiModelProperty(example = "password")
    private String password;
}
