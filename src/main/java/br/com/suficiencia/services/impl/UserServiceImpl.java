package br.com.suficiencia.services.impl;

import br.com.suficiencia.domain.dtos.RoleDTO;
import br.com.suficiencia.domain.dtos.UserDTO;
import br.com.suficiencia.domain.entities.User;
import br.com.suficiencia.domain.enumerators.Roles;
import br.com.suficiencia.domain.exceptions.ServiceException;
import br.com.suficiencia.domain.repositories.UserRepository;
import br.com.suficiencia.services.UserService;
import br.com.suficiencia.utils.JwtTokenUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByLogin(String login) {
        var user = userRepository.findByLoginIgnoreCase(login)
                .orElseThrow( () -> new ServiceException(HttpStatus.NOT_FOUND, "Usuário com o Login "+login+ "não foi encontrado"));
        return mapEntityToDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow( () -> new ServiceException(HttpStatus.NOT_FOUND, "Usuário com ID : "+id+ "não foi encontrado"));
        return mapEntityToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO createUser(String token, UserDTO user) {
        var userIdByToken = jwtTokenUtils.getIdUserFromToken(jwtTokenUtils.validateTokenAndReturnWithoutBearer(token));
        if (Objects.nonNull(user.getRoles()) && user.getRoles().contains(new RoleDTO(Roles.ADMIN))) {
            if (!isUserAdminById(Long.valueOf(userIdByToken))) {
                throw new ServiceException(HttpStatus.FORBIDDEN, "Você precisar ser ADMIN para criar um usuário ADMIN");
            }
        }
        if (Objects.isNull(user.getLogin()) || user.getLogin().isBlank()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "O campo Login não pode ser vázio");
        }
        if (Objects.isNull(user.getPassword()) || user.getPassword().isBlank()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "O campo Senha não pode ser vázio");
        }
        user.setId(null);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        if (userRepository.existsByLoginIgnoreCase(user.getLogin())) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Já existe um usuário com o Login: "+user.getLogin());
        }
        if (Objects.isNull(user.getRoles()) || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(new RoleDTO(Roles.USER)));
        }
        var savedUser = mapEntityToDTO(userRepository.save(mapDTOToEntity(user)));
        savedUser.setPassword(null);
        return savedUser;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable) //
                .map(this::mapEntityToDTO) //
                .map(this::mapRemovingPassword);//
    }

    @Override
    public Boolean isUserAdminById(Long id) {
        return getUserById(id).getRoles().contains(new RoleDTO(Roles.ADMIN));
    }

    private UserDTO mapEntityToDTO(User entity) {
        return modelMapper.map(entity, UserDTO.class);
    }

    private User mapDTOToEntity(UserDTO dto) {
        return modelMapper.map(dto, User.class);
    }

    private UserDTO mapRemovingPassword(UserDTO userDTO) {
        userDTO.setPassword(null);
        return userDTO;
    }
}
