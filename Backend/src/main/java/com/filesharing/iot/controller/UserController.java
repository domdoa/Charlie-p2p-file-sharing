package com.filesharing.iot.controller;

import com.filesharing.iot.models.Group;
import com.filesharing.iot.models.User;
import com.filesharing.iot.repository.GroupRepository;
import com.filesharing.iot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body("{\"" + "Error" + "\":\"" + "Invalid email" + "\"}");
        }
        userRepository.save(user);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body("{\"" + "Success" + "\":\"" + "Registered successfully" + "\"}");
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<User> findByEmail(@RequestParam String email) {
        User user = userRepository.findByEmail(email);

        if(user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/getGroupsForUser")
    public ResponseEntity<List<Group>> getGroupsForUser(@RequestParam long user_id) {
        List<Group> groups = userRepository.findByUserId(user_id).getGroups();

        if(groups == null || groups.size() == 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @PostMapping("/addUserGroup")
    public ResponseEntity addUserGroup(@RequestParam String email, @RequestParam String groupName){
        User user = userRepository.findByEmail(email);
        Group group = groupRepository.findByName(groupName);

        if(user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(group == null){
            group = new Group();
            group.setName(groupName);
//            group.set
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
