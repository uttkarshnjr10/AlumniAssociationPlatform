package com.example.alumniassocaition1.dto.college;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data-transfer object representing a college entity.
 *
 * <p>Used in responses to expose college details without leaking internal entity references.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollegeDto {

    private Long collegeId;
    private String name;
    private String address;
    private String contactPersonName;
    private String contactEmail;
    private String contactPhone;
    private String registrationStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
