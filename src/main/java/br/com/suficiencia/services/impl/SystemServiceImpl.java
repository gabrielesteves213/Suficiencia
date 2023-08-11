package br.com.suficiencia.services.impl;

import br.com.suficiencia.configurations.environments.AdminUserEnvironments;
import br.com.suficiencia.domain.entities.Role;
import br.com.suficiencia.domain.entities.User;
import br.com.suficiencia.domain.enumerators.Roles;
import br.com.suficiencia.domain.repositories.RoleRepository;
import br.com.suficiencia.domain.repositories.UserRepository;
import br.com.suficiencia.services.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminUserEnvironments adminUserEnvironments;

    @Override
    @Transactional
    public void createRoles() {
        Roles.getRolesAsList().forEach(role -> {
            if (!roleRepository.existsByRoleName(role)) {
                roleRepository.save(new Role(role));
            }
        });
    }

    @Override
    @Transactional
    public void createAdminUser() {
        if (!userRepository.existsByLoginIgnoreCase(adminUserEnvironments.getLogin())) {
            var user = User.builder()
                    .login(adminUserEnvironments.getLogin())
                    .password(new BCryptPasswordEncoder().encode(adminUserEnvironments.getPassword()))
                    .roles(Set.of(new Role(Roles.ADMIN)))
                    .build();
            userRepository.save(user);
        }
    }
}
