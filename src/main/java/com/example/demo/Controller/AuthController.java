package com.example.demo.Controller;

import com.example.demo.DTO.JwtRequest;
import com.example.demo.DTO.JwtResponse;
import com.example.demo.Models.User;
import com.example.demo.Repository.IUserRepository;
import com.example.demo.Services.UserService;
import com.example.demo.Utils.JWTTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserRepository userRepository;

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
    public ResponseEntity<User> create(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("/api/users/update")
    public ResponseEntity<User> update(@RequestBody Long Id, @RequestBody User user) {
        User findedUser = userRepository.findById(Id).get();
        findedUser.copy(user);
        userRepository.save(findedUser);
        return ResponseEntity.ok(findedUser);
    }

    @PostMapping("/api/users/remove")
    public ResponseEntity<User> remove(@RequestBody Long Id) {
        User user = userRepository.findById(Id).get();
        userRepository.deleteById(Id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/users/getPaged")
    public ResponseEntity<List<User>> getPaged() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/api/users/getById")
    public ResponseEntity<User> getById(@RequestBody Long id) {
        return ResponseEntity.ok(userRepository.findById(id).get());
    }
}
