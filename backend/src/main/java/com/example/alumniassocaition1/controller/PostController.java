package com.example.alumniassocaition1.controller;

import com.example.alumniassocaition1.dto.ApiResponse;
import com.example.alumniassocaition1.dto.post.PostCreateRequest;
import com.example.alumniassocaition1.dto.post.PostDto;
import com.example.alumniassocaition1.entity.Post;
import com.example.alumniassocaition1.entity.User;
import com.example.alumniassocaition1.exception.MyFileNotFoundException;
import com.example.alumniassocaition1.service.FileStorageService;
import com.example.alumniassocaition1.service.PostService;
import com.example.alumniassocaition1.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

/**
 * REST controller for post management.
 *
 * <p>Supports both multipart (with image upload) and JSON-only
 * endpoints for creating and updating posts. Also exposes
 * like/unlike actions and serves uploaded image files.</p>
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;
    private final FileStorageService fileStorageService;
    private final UserService userService;

    public PostController(PostService postService,
                          FileStorageService fileStorageService,
                          UserService userService) {
        this.postService = postService;
        this.fileStorageService = fileStorageService;
        this.userService = userService;
    }

    // -------------------------------------------------------------------------
    // Query endpoints
    // -------------------------------------------------------------------------

    /** Returns a paginated list of posts for the current user's college. */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<PostDto>> getAllPosts(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<PostDto> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    /** Returns a single post by its ID. */
    @GetMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {
        PostDto post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    // -------------------------------------------------------------------------
    // Create endpoints
    // -------------------------------------------------------------------------

    /** Creates a post from multipart form data (supports image upload). */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyRole('ALUMNUS', 'ADMIN')")
    public ResponseEntity<?> createPostMultipart(
            @RequestPart("content") String content,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        User currentUser = resolveCurrentUser();

        if (!StringUtils.hasText(content) && (imageFile == null || imageFile.isEmpty())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Post content cannot be blank if no image is provided."));
        }

        Post post = new Post();
        post.setContent(content != null ? content : "");
        post.setAuthor(currentUser);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = fileStorageService.storeFile(imageFile);
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/posts/uploads/")
                        .path(fileName)
                        .toUriString();
                post.setImageUrl(fileDownloadUri);
            } catch (Exception e) {
                logger.error("Failed to upload image for post", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse(false, "Failed to upload image: " + e.getMessage()));
            }
        }

        PostDto newPostDto = postService.createAndSavePost(post, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPostDto);
    }

    /** Creates a post from a JSON request body (no image support). */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyRole('ALUMNUS', 'ADMIN')")
    public ResponseEntity<PostDto> createPostJson(
            @Valid @RequestBody PostCreateRequest createRequestDto) {
        User currentUser = resolveCurrentUser();
        PostDto newPost = postService.createPostFromDto(createRequestDto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPost);
    }

    // -------------------------------------------------------------------------
    // Update endpoints
    // -------------------------------------------------------------------------

    /** Updates a post via multipart form data (supports image replacement). */
    @PutMapping(value = "/{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updatePostMultipart(
            @PathVariable Long postId,
            @RequestPart("content") String content,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        if (!StringUtils.hasText(content) && (imageFile == null || imageFile.isEmpty())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Post content cannot be blank if no image is being provided/kept."));
        }

        User currentUser = resolveCurrentUser();
        PostDto updatedPostDto = postService.updateFullPost(postId, content, imageFile, currentUser);
        return ResponseEntity.ok(updatedPostDto);
    }

    /** Updates a post via JSON request body. */
    @PutMapping(value = "/{postId}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDto> updatePostJson(
            @PathVariable Long postId,
            @Valid @RequestBody PostCreateRequest updateRequestDto) {
        User currentUser = resolveCurrentUser();
        PostDto updatedPost = postService.updatePostFromDto(postId, updateRequestDto, currentUser);
        return ResponseEntity.ok(updatedPost);
    }

    // -------------------------------------------------------------------------
    // Delete / Like / Unlike
    // -------------------------------------------------------------------------

    /** Deletes a post. Authorization is enforced at the service layer. */
    @DeleteMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok(new ApiResponse(true, "Post deleted successfully."));
    }

    /** Likes a post on behalf of the current user. */
    @PostMapping("/{postId}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> likePost(@PathVariable Long postId) {
        postService.likePost(postId);
        return ResponseEntity.ok(new ApiResponse(true, "Post liked."));
    }

    /** Removes the current user's like from a post. */
    @DeleteMapping("/{postId}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> unlikePost(@PathVariable Long postId) {
        postService.unlikePost(postId);
        return ResponseEntity.ok(new ApiResponse(true, "Post unliked."));
    }

    // -------------------------------------------------------------------------
    // File serving
    // -------------------------------------------------------------------------

    /** Serves an uploaded post image by filename. */
    @GetMapping("/uploads/{filename:.+}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename,
                                              HttpServletRequest request) {
        Resource resource;
        try {
            resource = fileStorageService.loadFileAsResource(filename);
        } catch (MyFileNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type for: {}", filename);
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private User resolveCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return userService.findUserByEmail(principal.getUsername());
    }
}
