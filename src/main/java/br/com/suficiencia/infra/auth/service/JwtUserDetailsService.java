package br.com.suficiencia.infra.auth.service;

import br.com.suficiencia.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user = userRepository.findByLoginIgnoreCase(login).orElseThrow(() -> new UsernameNotFoundException("User not found with login "+login));

        return new User(user.getLogin(), user.getPassword(), user.getAuthorities());
    }
}
