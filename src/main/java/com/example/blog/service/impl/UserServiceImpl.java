    package com.example.blog.service.impl;

    import com.cloudinary.Cloudinary;
    import com.example.blog.dto.user.SocialLinksDTO;
    import com.example.blog.dto.user.UserDTO;
    import com.example.blog.exception.ResourceNotFoundException;
    import com.example.blog.model.SocialLinks;
    import com.example.blog.model.User;
    import com.example.blog.repository.UserRepository;
    import com.example.blog.service.UserService;
    import lombok.RequiredArgsConstructor;
    import org.modelmapper.ModelMapper;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.util.List;
    import java.util.Map;
    import java.util.Objects;
    import java.util.function.Consumer;

    @Service
    @RequiredArgsConstructor
    public class UserServiceImpl implements UserService {
        private final UserRepository userRepository;
        private final ModelMapper modelMapper;
        private final Cloudinary cloudinary;

        @Override
        public UserDTO updateUser(UserDTO userDTO) {

            User user = getAuthUser();

            if (user == null)
                throw new UsernameNotFoundException("User not found");

            updateIfDifferent(user::setEmail, user.getEmail(), userDTO.getEmail());
            updateIfDifferent(user::setUsername, user.getUsername(), userDTO.getUsername());
            updateIfDifferent(user::setFirstName, user.getFirstName(), userDTO.getFirstName());
            updateIfDifferent(user::setLastName, user.getLastName(), userDTO.getLastName());
            updateIfDifferent(user::setBio, user.getBio(), userDTO.getBio());

            if (userDTO.getSocialLinks() != null) {
                SocialLinks socialLinks = user.getSocialLinks();
                SocialLinksDTO socialLinksDTO = userDTO.getSocialLinks();

                updateIfDifferent(socialLinks::setYoutube, socialLinks.getYoutube(), socialLinksDTO.getYoutube());
                updateIfDifferent(socialLinks::setInstagram, socialLinks.getInstagram(), socialLinksDTO.getInstagram());
                updateIfDifferent(socialLinks::setFacebook, socialLinks.getFacebook(), socialLinksDTO.getFacebook());
                updateIfDifferent(socialLinks::setTwitter, socialLinks.getTwitter(), socialLinksDTO.getTwitter());
                updateIfDifferent(socialLinks::setGithub, socialLinks.getGithub(), socialLinksDTO.getGithub());
                updateIfDifferent(socialLinks::setWebsite, socialLinks.getWebsite(), socialLinksDTO.getWebsite());
            }

            this.userRepository.save(user);

            return modelMapper.map(user, UserDTO.class);
        }

        private <T> void updateIfDifferent(Consumer<T> setter, T currentValue, T newValue) {
            if (!Objects.equals(currentValue, newValue)) {
                setter.accept(newValue);
            }
        }

        @Override
        public UserDTO getUserByUsername(String username) {
            UserDTO user = this.userRepository.getDTOByUsername(username);
            if (user == null)
                throw new ResourceNotFoundException("User", "Username", username);
            return user;
        }

        @Override
        public List<User> getAllUsers() {
            return this.userRepository.findAll();
        }

        @Override
        public void deleteUser(Long userId) {
            User user = this.userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

            this.userRepository.delete(user);
        }

        @Override
        public User getAuthUser() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return (User) authentication.getPrincipal();
        }

        @Override
        public List<UserDTO> getUserBySearchKey(String searchKey) {
            return userRepository.getUserBySearchType(searchKey);
        }

        @Override
        public String uploadImage(long userId, MultipartFile image){

            User user = getAuthUser();
            try {
                Map data  =this.cloudinary.uploader().upload(image.getBytes(),Map.of());
                String url = (String) data.get("url");
                userRepository.uploadImage(url,user.getUserId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return "User Added ! Successfully";
        }
    }
