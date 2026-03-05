package com.example.alumniassocaition1.service;

import com.example.alumniassocaition1.dto.comment.CommentCreateRequest;
import com.example.alumniassocaition1.dto.comment.CommentDto;
import com.example.alumniassocaition1.entity.Comment;
import com.example.alumniassocaition1.entity.Post;
import com.example.alumniassocaition1.entity.User;
import com.example.alumniassocaition1.exception.ResourceNotFoundException;
import com.example.alumniassocaition1.repository.CommentRepository;
import com.example.alumniassocaition1.repository.PostRepository;
import com.example.alumniassocaition1.util.DtoMapper;
import com.example.alumniassocaition1.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CommentService}.
 *
 * <p>
 * Deletion is permitted for the comment author, the post author, or any admin.
 * </p>
 */
@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final SecurityUtils securityUtils;
    private final DtoMapper dtoMapper;

    public CommentServiceImpl(CommentRepository commentRepository,
            PostRepository postRepository,
            SecurityUtils securityUtils,
            DtoMapper dtoMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.securityUtils = securityUtils;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsForPost(Long postId) throws ResourceNotFoundException {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }
        return commentRepository.findByPostPostIdOrderByCreatedAtAsc(postId).stream()
                .map(dtoMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addCommentToPost(Long postId, CommentCreateRequest createRequest)
            throws ResourceNotFoundException {
        User currentUser = securityUtils.getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = new Comment();
        comment.setTextContent(createRequest.getText());
        comment.setPost(post);
        comment.setAuthor(currentUser);

        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment {} added to post {} by user {}.",
                savedComment.getCommentId(), postId, currentUser.getUserId());
        return dtoMapper.toCommentDto(savedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) throws ResourceNotFoundException {
        User currentUser = securityUtils.getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        boolean isAuthor = comment.getAuthor().getUserId().equals(currentUser.getUserId());
        boolean isPostOwner = comment.getPost().getAuthor().getUserId().equals(currentUser.getUserId());
        boolean isAdmin = "admin".equalsIgnoreCase(currentUser.getRole());

        if (!(isAuthor || isPostOwner || isAdmin)) {
            throw new AccessDeniedException("User does not have permission to delete this comment.");
        }

        commentRepository.delete(comment);
        logger.info("Comment {} deleted by user {}.", commentId, currentUser.getUserId());
    }
}
