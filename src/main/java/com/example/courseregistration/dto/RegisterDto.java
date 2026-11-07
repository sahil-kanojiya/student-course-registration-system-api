package com.example.courseregistration.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.sql.Date;

@Getter
@Setter
public class RegisterDto {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;

    @Min(value = 1, message = "Course ID must be greater than zero")
    private Long courseId;

}
