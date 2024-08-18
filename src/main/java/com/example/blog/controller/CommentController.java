package com.example.blog.controller;

import com.example.blog.dto.ApiResponse;
import com.example.blog.dto.comment.CommentDto;
import com.example.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("post/{postId}/comment")
    ResponseEntity<List<CommentDto>> getCommentByPostId(@PathVariable long postId) {
        List<CommentDto> commentDtos = this.commentService.getCommentByPostId(postId);
        return ResponseEntity.ok(commentDtos);
    }

    @PostMapping("post/{postId}/user/{userId}/comment")
    ResponseEntity<CommentDto> createComment(@PathVariable long postId, @PathVariable long userId, @RequestBody CommentDto commentDto) {
        CommentDto comment = this.commentService.createComment(commentDto, userId, postId);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("comment/{commentId}")
    ResponseEntity<ApiResponse> deleteComment(@PathVariable long commentId) {
        this.commentService.deleteComment(commentId);
        return ResponseEntity.ok(new ApiResponse(200, true, "Comment deleted successfully"));

    }

}
