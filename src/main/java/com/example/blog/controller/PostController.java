package com.example.blog.controller;

import com.example.blog.config.AppConstant;
import com.example.blog.dto.ApiResponse;
import com.example.blog.dto.blog.PostDto;
import com.example.blog.dto.blog.PostResponse;
import com.example.blog.service.FileService;
import com.example.blog.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;


    @GetMapping("category/{categoryId}/post")
    ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable long categoryId) {
        List<PostDto> posts = this.postService.getPostByCategory(categoryId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("user/{userId}/post")
    ResponseEntity<List<PostDto>> getPostByUser(@PathVariable long userId) {
        List<PostDto> posts = this.postService.getPostByUser(userId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("post")
    ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir
    ) {
        PostResponse postResponse = this.postService.getAllPost(pageSize, pageNumber, sortBy, sortDir);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("post/{postId}")
    ResponseEntity<PostDto> getPostById(@PathVariable long postId) {
        PostDto postDto = this.postService.getPostById(postId);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @GetMapping("post/search/{keyword}")
    ResponseEntity<List<PostDto>> searchTitle(@PathVariable String keyword) {
        List<PostDto> postDtos = this.postService.searchTitle(keyword);
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @PostMapping("user/{userId}/category/{categoryId}/post")
    ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable long userId, @PathVariable long categoryId) {
        PostDto createdPost = this.postService.createPost(postDto, userId, categoryId);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @DeleteMapping("post/{postId}")
    ResponseEntity<ApiResponse> deletePost(@PathVariable long postId) {
        this.postService.deletePost(postId);
        return new ResponseEntity<>(new ApiResponse(200, true, "Post deleted successfully"), HttpStatus.OK);
    }

    @PutMapping("post/{postId}")
    ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable long postId) {
        PostDto updatedPost = this.postService.updatePost(postDto, postId);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @PostMapping("post/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadImage(@RequestParam MultipartFile image, @PathVariable long postId) throws IOException {
        PostDto postDto = this.postService.getPostById(postId);
        String fileName = this.fileService.uploadImage(path, image);
        postDto.setPostImage(fileName);
        PostDto updatedPost = this.postService.updatePost(postDto, postId);

        return new ResponseEntity<PostDto>(updatedPost, HttpStatus.OK);

    }

    @GetMapping(value = "post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {
        InputStream image = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(image, response.getOutputStream());
    }
}
