package br.com.suficiencia.infra.auth.controller;

import br.com.suficiencia.infra.auth.service.AuthService;
import br.com.suficiencia.infra.dtos.AuthRequest;
import br.com.suficiencia.infra.dtos.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600L)
@RequestMapping("/authentication")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.loginAndReturnToken(authRequest));
    }


}
