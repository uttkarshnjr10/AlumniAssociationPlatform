package com.example.alumniassocaition1.service;

import com.example.alumniassocaition1.dto.post.PostCreateRequest;
import com.example.alumniassocaition1.dto.post.PostDto;
import com.example.alumniassocaition1.entity.Post;
import com.example.alumniassocaition1.entity.PostLike;
import com.example.alumniassocaition1.entity.PostLikeId;
import com.example.alumniassocaition1.entity.User;
import com.example.alumniassocaition1.exception.FileStorageException;
import com.example.alumniassocaition1.exception.ResourceNotFoundException;
import com.example.alumniassocaition1.repository.PostLikeRepository;
import com.example.alumniassocaition1.repository.PostRepository;
import com.example.alumniassocaition1.util.DtoMapper;
import com.example.alumniassocaition1.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Implementation of {@link PostService}.
 *
 * <p>
 * Handles post CRUD, image upload/replacement, and like/unlike toggles.
 * Posts are always scoped to the author's college.
 * </p>
 */
@Service
public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;
    private final DtoMapper dtoMapper;

    public PostServiceImpl(PostRepository postRepository,
            PostLikeRepository postLikeRepository,
            FileStorageService fileStorageService,
            SecurityUtils securityUtils,
            DtoMapper dtoMapper) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.fileStorageService = fileStorageService;
        this.securityUtils = securityUtils;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostDto> getAllPosts(Pageable pageable) {
        User currentUser = securityUtils.getCurrentUserOrNull();
        Page<Post> postsPage;

        if (currentUser != null && currentUser.getCollege() != null) {
            postsPage = postRepository.findByAuthorCollegeCollegeIdOrderByCreatedAtDesc(
                    currentUser.getCollege().getCollegeId(), pageable);
        } else {
            postsPage = postRepository.findAll(pageable);
        }

        return postsPage.map(post -> dtoMapper.toPostDto(post, currentUser));
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostById(Long postId) throws ResourceNotFoundException {
        User currentUser = securityUtils.getCurrentUserOrNull();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        return dtoMapper.toPostDto(post, currentUser);
    }

    @Override
    @Transactional
    public PostDto createAndSavePost(Post post, User currentUser) throws FileStorageException {
        validatePostCreator(currentUser);
        if (post.getAuthor() == null)
            post.setAuthor(currentUser);
        post.setCollege(currentUser.getCollege());

        if (!StringUtils.hasText(post.getContent()) && !StringUtils.hasText(post.getImageUrl())) {
            throw new IllegalArgumentException("Post must have content or an image.");
        }

        Post savedPost = postRepository.save(post);
        logger.info("Post saved with ID: {}", savedPost.getPostId());
        return dtoMapper.toPostDto(savedPost, currentUser);
    }

    @Override
    @Transactional
    public PostDto createPostFromDto(PostCreateRequest createRequestDto, User currentUser) {
        validatePostCreator(currentUser);

        Post post = new Post();
        post.setContent(createRequestDto.getContent());
        post.setAuthor(currentUser);
        post.setCollege(currentUser.getCollege());

        Post savedPost = postRepository.save(post);
        return dtoMapper.toPostDto(savedPost, currentUser);
    }

    @Override
    @Transactional
    public PostDto updateFullPost(Long postId, String content, MultipartFile imageFile, User currentUser)
            throws FileStorageException, ResourceNotFoundException {
        if (currentUser == null)
            throw new AccessDeniedException("Authentication required.");
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        validatePostOwnerOrAdmin(post, currentUser);

        if (content != null)
            post.setContent(content);

        if (imageFile != null && !imageFile.isEmpty()) {
            deleteOldImage(post.getImageUrl());
            String storedResult = fileStorageService.storeFile(imageFile);
            post.setImageUrl(buildFileUrl(storedResult, "/api/posts/uploads/"));
        }

        if (!StringUtils.hasText(post.getContent()) && !StringUtils.hasText(post.getImageUrl())) {
            throw new IllegalArgumentException("Post update would result in no content and no image.");
        }

        Post updatedPost = postRepository.save(post);
        return dtoMapper.toPostDto(updatedPost, currentUser);
    }

    @Override
    @Transactional
    public PostDto updatePostFromDto(Long postId, PostCreateRequest updateRequestDto, User currentUser)
            throws ResourceNotFoundException {
        if (currentUser == null)
            throw new AccessDeniedException("Authentication required.");
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        validatePostOwnerOrAdmin(post, currentUser);

        post.setContent(updateRequestDto.getContent());
        Post updatedPost = postRepository.save(post);
        return dtoMapper.toPostDto(updatedPost, currentUser);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) throws ResourceNotFoundException {
        User currentUser = securityUtils.getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        validatePostOwnerOrAdmin(post, currentUser);

        String imageUrl = post.getImageUrl();
        postRepository.delete(post);
        logger.info("Deleted post with ID: {}", postId);
        deleteOldImage(imageUrl);
    }

    @Override
    @Transactional
    public void likePost(Long postId) throws ResourceNotFoundException {
        User currentUser = securityUtils.getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        PostLikeId likeId = new PostLikeId(post.getPostId(), currentUser.getUserId());
        if (postLikeRepository.existsById(likeId))
            return;

        PostLike postLike = new PostLike();
        postLike.setId(likeId);
        postLike.setPost(post);
        postLike.setUser(currentUser);
        postLikeRepository.save(postLike);
    }

    @Override
    @Transactional
    public void unlikePost(Long postId) throws ResourceNotFoundException {
        User currentUser = securityUtils.getCurrentUser();
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }

        PostLikeId likeId = new PostLikeId(postId, currentUser.getUserId());
        if (!postLikeRepository.existsById(likeId))
            return;
        postLikeRepository.deleteById(likeId);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private void validatePostCreator(User currentUser) {
        if (currentUser == null)
            throw new AccessDeniedException("Authentication required.");
        if (!("alumnus".equalsIgnoreCase(currentUser.getRole())
                || "admin".equalsIgnoreCase(currentUser.getRole()))) {
            throw new AccessDeniedException("User does not have permission to create posts.");
        }
        if (currentUser.getCollege() == null) {
            throw new IllegalStateException("User is not associated with a college.");
        }
    }

    private void validatePostOwnerOrAdmin(Post post, User currentUser) {
        boolean isOwner = post.getAuthor().getUserId().equals(currentUser.getUserId());
        boolean isAdminOfSameCollege = "admin".equalsIgnoreCase(currentUser.getRole())
                && currentUser.getCollege() != null
                && post.getCollege() != null
                && currentUser.getCollege().getCollegeId().equals(post.getCollege().getCollegeId());

        if (!isOwner && !isAdminOfSameCollege) {
            throw new AccessDeniedException("User does not have permission for this post.");
        }
    }

    private void deleteOldImage(String imageUrl) {
        if (!StringUtils.hasText(imageUrl))
            return;
        try {
            if (imageUrl.startsWith("http") && imageUrl.contains("cloudinary")) {
                fileStorageService.deleteFile(imageUrl);
            } else {
                String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                if (!fileName.isEmpty())
                    fileStorageService.deleteFile(fileName);
            }
        } catch (Exception e) {
            logger.warn("Could not delete old image: {}", e.getMessage());
        }
    }

    private String buildFileUrl(String storedResult, String pathPrefix) {
        if (storedResult.startsWith("http"))
            return storedResult;
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(pathPrefix).path(storedResult).toUriString();
    }
}
