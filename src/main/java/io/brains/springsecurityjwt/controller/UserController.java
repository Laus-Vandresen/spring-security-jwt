package io.brains.springsecurityjwt.controller;

import io.brains.springsecurityjwt.models.AuthenticationRequest;
import io.brains.springsecurityjwt.models.AuthenticationResponse;
import io.brains.springsecurityjwt.models.UserModel;
import io.brains.springsecurityjwt.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @GetMapping("/hello")
    public String hello() {
        return "Testing the authentication with the jwt token";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticatioRequest) throws Exception {
        AuthenticationResponse authenticationResponse = userDetailsService.createAuthenticationToken(authenticatioRequest);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/create-user")
    public void createNewUser(@RequestBody UserModel user) {
        this.userDetailsService.createNewUser(user);
    }
}
