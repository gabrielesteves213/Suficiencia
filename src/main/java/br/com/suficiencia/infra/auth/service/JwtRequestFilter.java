package br.com.suficiencia.infra.auth.service;

import br.com.suficiencia.domain.exceptions.ServiceException;
import br.com.suficiencia.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (!ObjectUtils.isEmpty(requestTokenHeader) && !requestTokenHeader.contains("null") && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);

            try {
                username = jwtTokenUtils.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, "Não foi possivel pegar o JWT Token");
            } catch (ExpiredJwtException e) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, "JWT Token Expirou");
            }
        } else {
            log.warn("JWT Token does not begin with Bearer String");
            if (!request.getRequestURL().toString().equalsIgnoreCase("http://localhost:8080/ProgramacaoWeb2021/authentication/login")) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, "Token não começa com Bearer");
            }
        }

        if (Objects.nonNull(username)) {
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

            if (jwtTokenUtils.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

}