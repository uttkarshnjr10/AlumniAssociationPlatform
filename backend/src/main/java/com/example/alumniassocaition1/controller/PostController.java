package com.example.alumniassocaition1.controller;

import com.example.alumniassocaition1.dto.ApiResponse;
import com.example.alumniassocaition1.dto.post.PostCreateRequest;
import com.example.alumniassocaition1.dto.post.PostDto;
import com.example.alumniassocaition1.entity.Post;
import com.example.alumniassocaition1.entity.User;
import com.example.alumniassocaition1.service.FileStorageService;
import com.example.alumniassocaition1.service.PostService;
import com.example.alumniassocaition1.util.SecurityUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for post CRUD, likes, and image serving.
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;

    public PostController(PostService postService,
            FileStorageService fileStorageService,
            SecurityUtils securityUtils) {
        this.postService = postService;
        this.fileStorageService = fileStorageService;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    public ResponseEntity<Page<PostDto>> getAllPosts(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    /** Multipart post creation with optional image. */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ALUMNUS', 'ADMIN')")
    public ResponseEntity<PostDto> createPostMultipart(
            @RequestPart(value = "content", required = false) String content,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        User currentUser = securityUtils.getCurrentUser();
        Post post = new Post();
        post.setContent(content);

        if (image != null && !image.isEmpty()) {
            String storedResult = fileStorageService.storeFile(image);
            post.setImageUrl(buildFileUrl(storedResult, "/api/posts/uploads/"));
        }

        PostDto savedPost = postService.createAndSavePost(post, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    /** JSON-only post creation. */
    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ALUMNUS', 'ADMIN')")
    public ResponseEntity<PostDto> createPostJson(
            @Valid @RequestBody PostCreateRequest createRequest) {
        User currentUser = securityUtils.getCurrentUser();
        PostDto savedPost = postService.createPostFromDto(createRequest, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    /** Multipart post update with optional image replacement. */
    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ALUMNUS', 'ADMIN')")
    public ResponseEntity<PostDto> updatePostMultipart(
            @PathVariable Long postId,
            @RequestPart(value = "content", required = false) String content,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        User currentUser = securityUtils.getCurrentUser();
        return ResponseEntity.ok(postService.updateFullPost(postId, content, image, currentUser));
    }

    /** JSON-only post update. */
    @PutMapping(value = "/{postId}/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ALUMNUS', 'ADMIN')")
    public ResponseEntity<PostDto> updatePostJson(
            @PathVariable Long postId,
            @Valid @RequestBody PostCreateRequest updateRequest) {
        User currentUser = securityUtils.getCurrentUser();
        return ResponseEntity.ok(postService.updatePostFromDto(postId, updateRequest, currentUser));
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("hasAnyRole('ALUMNUS', 'ADMIN')")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok(new ApiResponse(true, "Post deleted successfully."));
    }

    @PostMapping("/{postId}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> likePost(@PathVariable Long postId) {
        postService.likePost(postId);
        return ResponseEntity.ok(new ApiResponse(true, "Post liked."));
    }

    @DeleteMapping("/{postId}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> unlikePost(@PathVariable Long postId) {
        postService.unlikePost(postId);
        return ResponseEntity.ok(new ApiResponse(true, "Post unliked."));
    }

    /** Serves locally uploaded files. */
    @GetMapping("/uploads/{fileName:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    private String buildFileUrl(String storedResult, String pathPrefix) {
        if (storedResult.startsWith("http"))
            return storedResult;
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(pathPrefix).path(storedResult).toUriString();
    }
}
