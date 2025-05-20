package com.example.demo.Controller;

import com.example.demo.DTO.JwtRequest;
import com.example.demo.DTO.JwtResponse;
import com.example.demo.DTO.UpdateUserDto;
import com.example.demo.DTO.UserDto;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repository.IRoleRepository;
import com.example.demo.Repository.IUserRepository;
import com.example.demo.Services.UserService;
import com.example.demo.Utils.JWTTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private JWTTokenUtils jwtTokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/api/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (Exception e) {
            return new ResponseEntity<>("Неправильный логин или пароль", HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/api/users/create")
    public ResponseEntity<User> create(@RequestBody UserDto user) {
        User createUser = new User();
        createUser.setEmail(user.getEmail());
        createUser.setUsername(user.getUsername());
        createUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createUser.setUrl(user.getUrl());
        createUser.setFullName(user.getFullName());
        createUser.setExtra(user.getExtra());

        List<Role> list = user.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName))
                .filter(Optional::isPresent)
                .map(Optional::get).toList();
        createUser.setRoles(list);

        return ResponseEntity.ok(userRepository.save(createUser));
    }

    @PostMapping("/api/users/update")
    public ResponseEntity<User> update(@RequestBody UpdateUserDto updateUserDto) {
        Optional<User> byId = userRepository.findById(updateUserDto.getId());
        if (byId.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = byId.get();
        UserDto userDto = updateUserDto.getUserDto();

        if(!userDto.getEmail().isEmpty()) user.setEmail(userDto.getEmail());
        if(!userDto.getUrl().isEmpty()) user.setUrl(userDto.getUrl());
        if(!userDto.getFullName().isEmpty()) user.setFullName(userDto.getFullName());
        if(!userDto.getExtra().isEmpty()) user.setExtra(userDto.getExtra());
        if(!userDto.getPassword().isEmpty()) user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        if (!userDto.getRoles().isEmpty()) {
            List<Role> list = userDto.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName))
                    .filter(Optional::isPresent)
                    .map(Optional::get).toList();
            user.setRoles(list);
        }

        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("/api/users/remove")
    public ResponseEntity<User> remove(@RequestBody Long id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
        userRepository.deleteById(id);
        return ResponseEntity.ok(byId.get());
    }

    @PostMapping("/api/users/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("Пользователь не авторизован", HttpStatus.UNAUTHORIZED);
        }

        String username = authentication.getName(); // это login/email, зависит от реализации UserDetails

        Optional<User> optionalUser = userRepository.findByUsername(username); // или findByEmail()
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("Пользователь не найден", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(optionalUser.get());
    }

    @PostMapping("/api/users/getPaged")
    public ResponseEntity<List<User>> getPaged() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/api/users/getById/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userRepository.findById(id).get());
    }
}
