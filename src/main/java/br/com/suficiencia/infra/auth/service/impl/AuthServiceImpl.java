package br.com.suficiencia.infra.auth.service.impl;

import br.com.suficiencia.domain.dtos.RoleDTO;
import br.com.suficiencia.domain.dtos.UserDTO;
import br.com.suficiencia.domain.enumerators.Roles;
import br.com.suficiencia.domain.exceptions.ServiceException;
import br.com.suficiencia.infra.auth.service.AuthService;
import br.com.suficiencia.infra.auth.service.JwtUserDetailsService;
import br.com.suficiencia.infra.dtos.AuthRequest;
import br.com.suficiencia.infra.dtos.AuthResponse;
import br.com.suficiencia.services.UserService;
import br.com.suficiencia.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;


    @Override
    public AuthResponse loginAndReturnToken(AuthRequest authRequest) {

        if (Objects.isNull(authRequest.getLogin()) || Objects.isNull(authRequest.getPassword())) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "O campo Login e Senha devem ser preenchidos");
        }

        authenticate(authRequest.getLogin(), authRequest.getPassword());
        final var userDetails = jwtUserDetailsService.loadUserByUsername(authRequest.getLogin());
        final var userDTO = userService.getUserByLogin(userDetails.getUsername());
        final var token = jwtTokenUtils.generateToken(createUserClaims(userDetails, authRequest.getRememberMe(), //
                TimeZone.getTimeZone(ZoneId.systemDefault()), userDTO), userDetails, authRequest.getRememberMe(), userDTO.getId().toString()); //

        if (!userDTO.getRoles().isEmpty()) {
            var userRole = userDTO.getRoles()
                    .stream().map(RoleDTO::getRoleName)
                    .collect(Collectors.toList());
            if (!userRole.isEmpty()) {
                return new AuthResponse(token,
                        userDTO.getId(), userDTO.getLogin(), userRole.get(0));
            }
        }
        return new AuthResponse(token,
                userDTO.getId(), userDTO.getLogin(), Roles.USER);
    }

    private void authenticate(String login, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        } catch (DisabledException e) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Esse Usuário está dasabilitado");
        } catch (LockedException e) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Esse Usuário está Bloqueado");
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Credenciais de Login Inválidas");
        }
    }

    private Map<String, Object> createUserClaims(UserDetails userDetails, Boolean rememberMe, TimeZone timeZone, UserDTO userDTO) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDTO.getId());
        claims.put("login", userDTO.getLogin());
        claims.put("remember_me", rememberMe);
        claims.put("user_time_zone", timeZone.getID());
        claims.put("roles", userDTO.getRoles());
        return claims;
    }

}
