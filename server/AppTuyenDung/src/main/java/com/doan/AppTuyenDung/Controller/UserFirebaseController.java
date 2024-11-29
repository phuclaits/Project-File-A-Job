package com.doan.AppTuyenDung.Controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.doan.AppTuyenDung.ModelFirebase.User;
import com.doan.AppTuyenDung.Services.Notification.UserService;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/user-firebase")
public class UserFirebaseController {
    private static final Logger log = LoggerFactory.getLogger(UserFirebaseController.class);
    @Autowired
    private UserService userService;
    @PostMapping("")
    public ResponseEntity<String> createUser(@RequestBody User user) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String phoneNumber) throws ExecutionException, InterruptedException {
        User user = userService.getUserById(phoneNumber);
        return user != null ? new ResponseEntity<>(user, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
    @DeleteMapping("/token")
    public ResponseEntity<String> deleteToken(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("token") String token) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(userService.deleteToken(phoneNumber,token),HttpStatus.OK);
    }
    @PostMapping("/token")
    public ResponseEntity<String> addToken(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("token") String token) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(userService.addTokenForUser(phoneNumber,token),HttpStatus.OK);
    }
}