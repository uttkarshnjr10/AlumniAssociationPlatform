package com.example.alumniassocaition1.service;

import com.example.alumniassocaition1.dto.post.PostCreateRequest;
import com.example.alumniassocaition1.dto.post.PostDto;
import com.example.alumniassocaition1.entity.Post;
import com.example.alumniassocaition1.entity.User;
import com.example.alumniassocaition1.exception.FileStorageException;
import com.example.alumniassocaition1.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service contract for post CRUD, image uploads, and like/unlike operations.
 */
public interface PostService {

    Page<PostDto> getAllPosts(Pageable pageable);

    PostDto getPostById(Long postId) throws ResourceNotFoundException;

    PostDto createAndSavePost(Post post, User currentUser) throws FileStorageException;

    PostDto createPostFromDto(PostCreateRequest createRequestDto, User currentUser);

    PostDto updateFullPost(Long postId, String content, MultipartFile imageFile, User currentUser)
            throws FileStorageException, ResourceNotFoundException;

    PostDto updatePostFromDto(Long postId, PostCreateRequest updateRequestDto, User currentUser)
            throws ResourceNotFoundException;

    void deletePost(Long postId) throws ResourceNotFoundException;

    void likePost(Long postId) throws ResourceNotFoundException;

    void unlikePost(Long postId) throws ResourceNotFoundException;
}
