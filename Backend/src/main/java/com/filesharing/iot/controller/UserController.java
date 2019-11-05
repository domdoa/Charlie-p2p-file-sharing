package com.filesharing.iot.controller;

import com.filesharing.iot.models.UserModel;
import com.filesharing.iot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody UserModel userModel) {
        userModel.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
        if (userRepository.findByEmail(userModel.getEmail()) != null) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body("{\"" + "Error" + "\":\"" + "Invalid username" + "\"}");
        }
        userRepository.save(userModel);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body("{\"" + "Success" + "\":\"" + "Registered successfully" + "\"}");
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<UserModel> findByEmail(@RequestParam String email) {
        return new ResponseEntity<>(userRepository.findByEmail(email), HttpStatus.OK);
    }


}
