package br.com.suficiencia.services.impl;

import br.com.suficiencia.domain.dtos.StudentDTO;
import br.com.suficiencia.domain.entities.Student;
import br.com.suficiencia.domain.exceptions.ServiceException;
import br.com.suficiencia.domain.repositories.StudentRepository;
import br.com.suficiencia.services.StudentService;
import br.com.suficiencia.services.UserService;
import br.com.suficiencia.utils.JwtTokenUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public StudentDTO createStudent(String token, StudentDTO studentDTO) {
        var userIdByToken = jwtTokenUtils.getIdUserFromToken(jwtTokenUtils.validateTokenAndReturnWithoutBearer(token));
        if (!userService.isUserAdminById(Long.valueOf(userIdByToken))) {
            throw new ServiceException(HttpStatus.FORBIDDEN, "Seu papel não possui permissão, apenas ADMIN");
        }

        if (Objects.isNull(studentDTO.getName()) || studentDTO.getName().isBlank()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "É Necessário passar um nome para o Estudante");
        }
        return mapEntityToDTO(studentRepository.save(mapDTOToEntity(studentDTO)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentDTO> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable).map(this::mapEntityToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long id) {
        if (Objects.isNull(id)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "É Necessário passar o ID Do Estudante que você quer buscar");
        };
        return mapEntityToDTO(studentRepository.findById(id).orElseThrow(
                () -> new ServiceException(HttpStatus.NOT_FOUND, "Não foi encontrado usuário com o ID: "+id)));
    }

    @Override
    @Transactional
    public void deleteStudentById(Long id) {
        if (Objects.isNull(id)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "É Necessário passar o ID Do Estudante que você quer deletar");
        };
        try {
            studentRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Aconteceu um erro ao tentar deletar o Estudante com ID: "+id);
        }
    }

    private StudentDTO mapEntityToDTO(Student entity) {
        return modelMapper.map(entity, StudentDTO.class);
    }

    private Student mapDTOToEntity(StudentDTO dto) {
        return modelMapper.map(dto, Student.class);
    }
}
