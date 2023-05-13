package com.dumitrascuantonio.auctionapp.service;

import com.dumitrascuantonio.auctionapp.dto.SignUpRequest;
import com.dumitrascuantonio.auctionapp.entity.User;
import com.dumitrascuantonio.auctionapp.entity.enumeration.Role;
import com.dumitrascuantonio.auctionapp.repository.UserRepository;
import java.security.Principal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User createUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.getRoles().add(Role.ROLE_USER);

        return userRepository.save(user);
    }

    public List<User> getFirst10Users() {
        Pageable top10Users = PageRequest.of(0, 10);
        return userRepository.findFirstWithPageable(top10Users);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User cannot be found with id: " + id));
    }

    public List<User> getUsersByKeyword(String keyword) {
        return userRepository.findAllByUsernameContains(keyword);
    }

    public User updateUser(User user) {
        user.getRoles().add(Role.ROLE_USER);
        return userRepository.save(user);
    }

    public User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name: " + username));
    }
}
