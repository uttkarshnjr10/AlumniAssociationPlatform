package com.example.alumniassocaition1.dto.college;

<<<<<<< HEAD
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request payload for registering a new college along with an initial admin user.
 *
 * <p>The nested {@link AdminUserDetails} object contains the credentials for the
 * first admin account that will be created for this college upon approval.</p>
 */
@Data
public class CollegeRegistrationRequest {

    @NotBlank(message = "College name is required")
    private String collegeName;

    private String address;

    @NotBlank(message = "Contact person name is required")
    private String contactPerson;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Must be a valid email address")
    private String contactEmail;

    private String contactPhone;

    /** Credentials for the initial college administrator account. */
    @Data
    public static class AdminUserDetails {

        @NotBlank(message = "Admin name is required")
        private String name;

        @NotBlank(message = "Admin email is required")
        @Email(message = "Must be a valid email address")
        private String email;

        @NotBlank(message = "Admin password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;
    }

    private AdminUserDetails adminUser;
=======
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CollegeRegistrationRequest {
    @NotBlank
    private String collegeName;
    @NotBlank
    private String address;
    @NotBlank
    private String contactPerson;
    @NotBlank
    @Email
    private String contactEmail;
    @NotBlank
    private String contactPhone;

    @NotNull
    @Valid
    private AdminUserDetails adminUser;



    @Data
    public static class AdminUserDetails {
        @NotBlank
        private String name;
        @NotBlank
        @Email
        private String email;
        @NotBlank
        private String password;
    }
>>>>>>> upstream/main
}
