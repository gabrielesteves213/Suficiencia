package br.com.suficiencia.controllers;

import br.com.suficiencia.domain.dtos.StudentDTO;
import br.com.suficiencia.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/aluno")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestHeader("Authorization") String token,
                                                    @RequestBody @NotNull StudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.createStudent(token, studentDTO));
    }

    @GetMapping
    public ResponseEntity<Page<StudentDTO>> getAllStudents(Pageable pageable) {
        return ResponseEntity.ok(studentService.getAllStudents(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteStudentById(@PathVariable("id") Long id) {
        studentService.deleteStudentById(id);
    }

}
