package com.example.blog.service.impl;

import com.example.blog.config.AppConstant;
import com.example.blog.dto.ApiResponse;
import com.example.blog.dto.jwt.JwtAuthRequest;
import com.example.blog.dto.jwt.JwtAuthResponse;
import com.example.blog.dto.user.UserFormDto;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Role;
import com.example.blog.model.User;
import com.example.blog.repository.RoleRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.security.CustomUserDetailService;
import com.example.blog.security.JwtTokenHelper;
import com.example.blog.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtTokenHelper jwtTokenHelper;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService userDetailsService;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository, JwtTokenHelper jwtTokenHelper, AuthenticationManager authenticationManager, CustomUserDetailService userDetailsService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtTokenHelper = jwtTokenHelper;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }


    @Override
    public ApiResponse registerUser(UserFormDto userFormDto) {
        User user = this.modelMapper.map(userFormDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = this.roleRepository.findById(AppConstant.NORMAL_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "Id", AppConstant.NORMAL_USER));

        user.setRoles(Collections.singletonList(role));

        this.userRepository.save(user);
        return new ApiResponse(201, true, "Registration successful! Please log in.");
    }

    @Override
    public JwtAuthResponse loginUser(JwtAuthRequest request) {
        this.authenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.jwtTokenHelper.generateToken(userDetails);
        return JwtAuthResponse.builder().token(token).status(200).success(true).build();
    }


    @Override
    public UserFormDto updateUser(UserFormDto userFormDto, Long userId) {

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        user.setFirstName(userFormDto.getFirstName());
        user.setEmail(userFormDto.getEmail());
        user.setPassword(userFormDto.getPassword());
        user.setBio(userFormDto.getBio());

        User savedUser = this.userRepository.save(user);

        return this.userToDto(savedUser);
    }

    @Override
    public UserFormDto getUserById(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        return this.userToDto(user);
    }

    @Override
    public List<UserFormDto> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        return users.stream().map(this::userToDto).toList();
    }

    @Override
    public void deleteUser(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        this.userRepository.delete(user);
    }

    private void authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authenticate = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        } catch (BadCredentialsException | InternalAuthenticationServiceException exception) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }

    public User dtoToUser(UserFormDto userFormDto) {
        return this.modelMapper.map(userFormDto, User.class);
    }

    public UserFormDto userToDto(User user) {
        return this.modelMapper.map(user, UserFormDto.class);
    }
}
