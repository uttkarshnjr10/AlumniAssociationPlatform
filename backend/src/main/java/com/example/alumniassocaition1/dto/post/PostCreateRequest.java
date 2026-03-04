<<<<<<< HEAD
package com.example.alumniassocaition1.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
=======
package com.example.alumniassocaition1.dto.post; // Your package

>>>>>>> upstream/main
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD

/**
 * Request payload for creating or updating a post (JSON endpoint).
 */
=======
import com.fasterxml.jackson.annotation.JsonProperty;


>>>>>>> upstream/main
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {
<<<<<<< HEAD

    @JsonProperty("content")
    @NotBlank(message = "Post content is required")
    private String content;
}
=======
    @JsonProperty("content") // Explicitly map the JSON key
    @NotBlank(message = "must not be blank")
    private String content;
}
>>>>>>> upstream/main
