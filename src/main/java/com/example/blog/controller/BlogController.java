package com.example.blog.controller;

import com.example.blog.config.AppConstant;
import com.example.blog.dto.ApiResponse;
import com.example.blog.dto.blog.BlogDto;
import com.example.blog.dto.blog.BlogResponse;
import com.example.blog.service.BlogService;
import com.example.blog.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
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
public class BlogController {

    private final BlogService blogService;

    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    public BlogController(BlogService blogService, FileService fileService) {
        this.blogService = blogService;
        this.fileService = fileService;
    }
    
    @GetMapping("user/{userId}/blog")
    ResponseEntity<List<BlogDto>> getBlogByUser(@PathVariable long userId) {
        List<BlogDto> blogs = this.blogService.getBlogByUser(userId);
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @GetMapping("blog")
    ResponseEntity<BlogResponse> getAllBlogs(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir
    ) {
        BlogResponse blogResponse = this.blogService.getAllBlog(pageSize, pageNumber, sortBy, sortDir);
        return new ResponseEntity<>(blogResponse, HttpStatus.OK);
    }

    @GetMapping("blog/{blogId}")
    ResponseEntity<BlogDto> getBlogById(@PathVariable long blogId) {
        BlogDto blogDto = this.blogService.getBlogById(blogId);
        return new ResponseEntity<>(blogDto, HttpStatus.OK);
    }

    @GetMapping("blog/search/{keyword}")
    ResponseEntity<List<BlogDto>> searchTitle(@PathVariable String keyword) {
        List<BlogDto> blogDtos = this.blogService.searchTitle(keyword);
        return new ResponseEntity<>(blogDtos, HttpStatus.OK);
    }

    @PostMapping("user/{userId}/category/{categoryId}/blog")
    ResponseEntity<BlogDto> createBlog(@RequestBody BlogDto blogDto, @PathVariable long userId, @PathVariable long categoryId) {
        BlogDto createdBlog = this.blogService.createBlog(blogDto, userId, categoryId);
        return new ResponseEntity<>(createdBlog, HttpStatus.CREATED);
    }

    @DeleteMapping("blog/{blogId}")
    ResponseEntity<ApiResponse> deleteBlog(@PathVariable long blogId) {
        this.blogService.deleteBlog(blogId);
        return new ResponseEntity<>(new ApiResponse(200, true, "Blog deleted successfully"), HttpStatus.OK);
    }

    @PutMapping("blog/{blogId}")
    ResponseEntity<BlogDto> updateBlog(@RequestBody BlogDto blogDto, @PathVariable long blogId) {
        BlogDto updatedBlog = this.blogService.updateBlog(blogDto, blogId);
        return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
    }

    @PostMapping("blog/image/upload/{blogId}")
    public ResponseEntity<BlogDto> uploadImage(@RequestParam MultipartFile image, @PathVariable long blogId) throws IOException {
        BlogDto blogDto = this.blogService.getBlogById(blogId);
        String fileName = this.fileService.uploadImage(path, image);
        blogDto.setBlogImage(fileName);
        BlogDto updatedBlog = this.blogService.updateBlog(blogDto, blogId);

        return new ResponseEntity<BlogDto>(updatedBlog, HttpStatus.OK);

    }

    @GetMapping(value = "blog/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {
        InputStream image = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(image, response.getOutputStream());
    }
}
