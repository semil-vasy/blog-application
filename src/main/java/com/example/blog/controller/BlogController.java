package com.example.blog.controller;

import com.example.blog.dto.ApiResponse;
import com.example.blog.dto.blog.BlogCustomDTO;
import com.example.blog.dto.blog.BlogDTO;
import com.example.blog.service.BlogService;
import com.example.blog.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
@RequestMapping("/api/blog")
public class BlogController {

    private final BlogService blogService;

    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    public BlogController(BlogService blogService, FileService fileService) {
        this.blogService = blogService;
        this.fileService = fileService;
    }

    @GetMapping
    ResponseEntity<List<BlogCustomDTO>> getAllBlogs(
            @RequestParam(value = "userId", defaultValue = "0", required = false) long userId,
            @RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
            @RequestParam(value = "limit", defaultValue = "10", required = false) int limit,
            @RequestParam(value = "tagNames", defaultValue = "", required = false) String tagNames
    ) {
        List<BlogCustomDTO> blogResponse = this.blogService.getAllBlog(userId, offset, limit, tagNames);
        return new ResponseEntity<>(blogResponse, HttpStatus.OK);
    }

    @GetMapping("trending")
    ResponseEntity<List<BlogCustomDTO>> getTrendingBlogs() {
        List<BlogCustomDTO> blogResponse = this.blogService.getTrendingBlogs();
        return new ResponseEntity<>(blogResponse, HttpStatus.OK);
    }

    @GetMapping("{blogId}")
    ResponseEntity<BlogCustomDTO> getBlogByBlogId(@PathVariable String blogId) {
        BlogCustomDTO blogDto = this.blogService.getBlogByBlogId(blogId);
        return new ResponseEntity<>(blogDto, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<BlogDTO> createBlog(@Valid @RequestBody BlogDTO blogDto) {
        BlogDTO createdBlog = this.blogService.createBlog(blogDto);
        return new ResponseEntity<>(createdBlog, HttpStatus.CREATED);
    }

    @DeleteMapping("{blogId}")
    ResponseEntity<ApiResponse> deleteBlog(@PathVariable long blogId) {
        this.blogService.deleteBlog(blogId);
        return new ResponseEntity<>(new ApiResponse(200, true, "Blog deleted successfully"), HttpStatus.OK);
    }

    @PutMapping("{blogId}")
    ResponseEntity<BlogDTO> updateBlog(@RequestBody BlogDTO blogDto, @PathVariable long blogId) {
        BlogDTO updatedBlog = this.blogService.updateBlog(blogDto, blogId);
        return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    ResponseEntity<List<BlogDTO>> searchTitle(@PathVariable String keyword) {
        List<BlogDTO> blogDTOS = this.blogService.searchTitle(keyword);
        return new ResponseEntity<>(blogDTOS, HttpStatus.OK);
    }

    @PostMapping("/image/upload/{blogId}")
    public ResponseEntity<BlogCustomDTO> uploadImage(@RequestParam MultipartFile image, @PathVariable String blogId) throws IOException {
        BlogCustomDTO blogDto = this.blogService.getBlogByBlogId(blogId);
//        String fileName = this.fileService.uploadImage(path, image);
//        blogDto.setBanner(fileName);
//        BlogDTO updatedBlog = this.blogService.updateBlog(blogDto, blogId);

        return new ResponseEntity<>(blogDto, HttpStatus.OK);

    }

    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {
        InputStream image = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(image, response.getOutputStream());
    }
}
