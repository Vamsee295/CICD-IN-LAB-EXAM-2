package com.example.demo.controller;

import com.example.demo.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.UserRepository;

import java.util.List;

@CrossOrigin(origins = "*") // allow frontend
@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserRepository usersRepository;

    // ✅ Signup Endpoint
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Users user) {
        try {
            if (usersRepository.findByEmail(user.getEmail()) != null) {
                return ResponseEntity.badRequest().body("❌ Email already exists!");
            }
            Users savedUser = usersRepository.save(user);
            savedUser.setPassword(null); // hide password
            return ResponseEntity.ok(savedUser); // return full user info
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ Failed to create account!");
        }
    }

    // ✅ Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users loginRequest) {
        Users user = usersRepository.findByEmail(loginRequest.getEmail());

        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            user.setPassword(null); // hide password
            return ResponseEntity.ok(user); // return full user info
        } else {
            return ResponseEntity.status(401).body("❌ Invalid credentials");
        }
    }

    // ✅ Get All Users (for debugging/admin)
    @GetMapping("/all")
    public List<Users> getAllUsers() {
        List<Users> users = usersRepository.findAll();
        // hide passwords before sending
        for (Users u : users) {
            u.setPassword(null);
        }
        return users;
    }
}
