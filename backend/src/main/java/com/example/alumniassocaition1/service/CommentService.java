package com.example.alumniassocaition1.service;

import com.example.alumniassocaition1.dto.comment.CommentCreateRequest;
import com.example.alumniassocaition1.dto.comment.CommentDto;
import com.example.alumniassocaition1.exception.ResourceNotFoundException;

import java.util.List;

/**
 * Service contract for comment CRUD on posts.
 */
public interface CommentService {

    List<CommentDto> getCommentsForPost(Long postId) throws ResourceNotFoundException;

    CommentDto addCommentToPost(Long postId, CommentCreateRequest createRequest)
            throws ResourceNotFoundException;

    void deleteComment(Long commentId) throws ResourceNotFoundException;
}
