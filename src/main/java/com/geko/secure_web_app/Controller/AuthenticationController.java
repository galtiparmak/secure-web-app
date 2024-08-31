package com.geko.secure_web_app.Controller;

import com.geko.secure_web_app.Auth.AuthenticationRequest;
import com.geko.secure_web_app.Auth.AuthenticationResponse;
import com.geko.secure_web_app.Auth.RegisterRequest;
import com.geko.secure_web_app.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    } // http://localhost:8080/api/auth/register

    @PostMapping("/register_admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.registerAdmin(request));
    } // http://localhost:8080/api/auth/register_admin

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    } // http://localhost:8080/api/auth/authenticate

}
