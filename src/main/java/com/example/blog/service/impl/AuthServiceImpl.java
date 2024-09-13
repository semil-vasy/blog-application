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
import com.example.blog.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtTokenHelper jwtTokenHelper;
    private final CustomUserDetailService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public AuthServiceImpl(PasswordEncoder passwordEncoder, RoleRepository roleRepository, JwtTokenHelper jwtTokenHelper, CustomUserDetailService userDetailsService, AuthenticationManager authenticationManager, UserRepository userRepository, ModelMapper modelMapper) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtTokenHelper = jwtTokenHelper;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse registerUser(UserFormDto userFormDto) {
        User user = this.modelMapper.map(userFormDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(user.getEmail().split("@")[0]);
        user.setProfileImage(AppConstant.DUMMY_IMAGE_URL);

        Role role = this.roleRepository.findById(AppConstant.NORMAL_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "Id", AppConstant.NORMAL_USER));

        user.setRoles(Collections.singletonList(role));

        this.userRepository.save(user);
        return new ApiResponse(201, true, "Registration successful! Please log in.");
    }

    @Override
    public JwtAuthResponse loginUser(JwtAuthRequest request) {
        this.authenticate(request.getEmail(), request.getPassword());
        String token = this.jwtTokenHelper.generateToken(request.getEmail());
        return JwtAuthResponse.builder().token(token).status(200).success(true).build();
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

}
