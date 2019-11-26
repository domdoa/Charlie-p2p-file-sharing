package com.filesharing.iot.controller;

import com.filesharing.iot.Chord.Constants;
import com.filesharing.iot.models.ForeignPC;
import com.filesharing.iot.models.Group;
import com.filesharing.iot.models.User;
import com.filesharing.iot.repository.ForeignPcRepository;
import com.filesharing.iot.repository.GroupRepository;
import com.filesharing.iot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/")
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    ForeignPcRepository foreignPcRepository;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody User user, @RequestParam @Nullable String inviteString, @RequestParam @Nullable String groupName) throws Exception {
        LOGGER.log( Level.INFO, "Registering");
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body("{\"" + "Error" + "\":\"" + "Invalid email" + "\"}");
        }
        Group publicGroup = groupRepository.findByName("public");
        if (publicGroup == null) {
            publicGroup = new Group();
            publicGroup.setName("public");
            groupRepository.save(publicGroup);
        }
        if (inviteString != null && !inviteString.isEmpty()) {
            Group group = groupRepository.findByInviteString(inviteString);
            if (group != null)
                user.addGroup(group);
            user.addGroup(publicGroup);

            sychronizeUsers(user);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body("{\"" + "Success" + "\":\"" + "Registered successfully" + "\"}");
        } else if (groupName != null && !groupName.isEmpty()) {
            String newInviteString = generateRandomString();
            Group group = new Group();
            group.setName(groupName);
            group.setInviteString(newInviteString);

            user.addGroup(group);
            user.addGroup(publicGroup);

            sychronizeUsers(user);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body("{\"" + "InviteString" + "\":\"" + newInviteString + "\"}");
        } // if both null, it is sent by another central server
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body("{\"" + "Success" + "\":\"" + "Registered successfully" + "\"}");
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<User> findByEmail(@RequestParam String email) {
        LOGGER.log( Level.INFO, "Finding user by email");
        User user = userRepository.findByEmail(email);
        if (user == null) {
            LOGGER.log(Level.WARNING, "User not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/getGroupsForUser")
    public ResponseEntity<List<Group>> getGroupsForUser(@RequestParam String email) {
        LOGGER.log( Level.INFO, "Getting user groups");
        List<Group> groups = userRepository.findByEmail(email).getGroups();

        if (groups == null || groups.size() == 0){
            LOGGER.log( Level.WARNING, "User does not belong to any group");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @PostMapping("/addUserGroup")
    public ResponseEntity addUserGroup(@RequestParam String email, @RequestParam String groupName) {
        LOGGER.log( Level.INFO, "Adding user group");
        User user = userRepository.findByEmail(email);
        Group group = groupRepository.findByName(groupName);

        if (user == null) {
            LOGGER.log(Level.WARNING, "User not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (group == null) {
            group = new Group();
            group.setName(groupName);
            group.addUser(user);
            groupRepository.save(group);
        }
        user.addGroup(group);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String generateRandomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    private void sychronizeUsers(User user) throws Exception {
        for (ForeignPC foreignPC : foreignPcRepository.getForeignPCS()) {
            String foreignPCAddress = foreignPC.getInetSocketAddress().getHostName();
            foreignPCAddress = foreignPCAddress.substring(1);

            if (!(foreignPCAddress.equals(Constants.localAddress) &&
                    foreignPC.getSpringPort().equals(Constants.currentSpringPort))) {
                try{
                    restTemplate.postForObject("http://" + foreignPCAddress + ":" + foreignPC.getSpringPort() + "/sign-up", user, ResponseEntity.class);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }
}
