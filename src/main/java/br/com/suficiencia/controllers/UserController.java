package br.com.suficiencia.controllers;

import br.com.suficiencia.domain.dtos.UserDTO;
import br.com.suficiencia.infra.auth.service.JwtUserDetailsService;
import br.com.suficiencia.services.UserService;
import br.com.suficiencia.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*", maxAge = 36000L)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestHeader("Authorization") String token, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(token, userDTO));
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateUserToken(@RequestHeader("Authorization") String token) {
        var tokenWithoutBearer = jwtTokenUtils.validateTokenAndReturnWithoutBearer(token);
        var usernameByToken = jwtTokenUtils.getUsernameFromToken(tokenWithoutBearer);
        return ResponseEntity.ok(jwtTokenUtils.validateToken(tokenWithoutBearer, jwtUserDetailsService.loadUserByUsername(usernameByToken)));
    }

}
