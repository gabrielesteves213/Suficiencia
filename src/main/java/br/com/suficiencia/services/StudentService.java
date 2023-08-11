package br.com.suficiencia.services;

import br.com.suficiencia.domain.dtos.StudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {

    StudentDTO createStudent(String token, StudentDTO studentDTO);

    Page<StudentDTO> getAllStudents(Pageable pageable);

    StudentDTO getStudentById(Long id);

    void deleteStudentById(Long id);

}
