package br.com.suficiencia.services;

import br.com.suficiencia.domain.dtos.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDTO getUserByLogin(String login);

    UserDTO getUserById(Long id);

    UserDTO createUser(String token, UserDTO user);

    Boolean isUserAdminById(Long id);

    Page<UserDTO> getAllUsers(Pageable pageable);
}
