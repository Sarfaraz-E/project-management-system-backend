package com.example.ProjectManagementSystem.service;

import com.example.ProjectManagementSystem.model.Comment;
import com.example.ProjectManagementSystem.model.Issues;
import com.example.ProjectManagementSystem.model.User;
import com.example.ProjectManagementSystem.repository.CommentRepository;
import com.example.ProjectManagementSystem.repository.IssueRepository;
import com.example.ProjectManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Comment createComment(Long issueId, Long userId, String content) throws Exception {
        Optional<Issues> issuesOptional = issueRepository.findById(issueId);
        Optional<User> userOptional = userRepository.findById(userId);

        if(issuesOptional.isEmpty()){
            throw new Exception("User not  found with id "+ userId);

        }
        Issues issues= issuesOptional.get();
        User user = userOptional.get();

        Comment comment = new Comment();

        comment.setIssues(issues);
        comment.setUser(user);
        comment.setCreatedAtTime(LocalDateTime.now());
        comment.setContent(content);

        Comment savedComment = commentRepository.save(comment);

        issues.getComments().add(savedComment);

        return savedComment;

    }

    @Override
    public void deleteComment(Long commentId, Long userId) throws Exception {
       Optional<Comment> commentOptional = commentRepository.findById(commentId);
       Optional<User> userOptional = userRepository.findById(userId);

       if (commentOptional.isEmpty()){
           throw new Exception("Comment not  found with id" + commentId);
       }
       if (userOptional.isEmpty()){
           throw new Exception("Comment not  found with id" + userId);

       }
       Comment comment = commentOptional.get();
       User user = userOptional.get();

       if (comment.getUser().equals(user)){
           commentRepository.delete(comment);
       }else {
           throw new Exception("user doesnot have permission to delete this comment" + userId);

       }
    }

    @Override
    public List<Comment> findCommentByIssueId(Long issueId) {
        return commentRepository.findByIssuesId(issueId);
    }


}
