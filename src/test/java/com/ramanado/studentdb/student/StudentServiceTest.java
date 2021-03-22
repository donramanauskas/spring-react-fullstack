package com.ramanado.studentdb.student;

import com.ramanado.studentdb.student.exception.BadRequestException;
import com.ramanado.studentdb.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void itShouldGetAllStudents() {
        // Given
        // When
        underTest.getAllStudents();
        // Then
        verify(studentRepository).findAll();

    }

    @Test
    void itShouldAddStudent() {
        // Given
        Student student = new Student(
                "Jon Jones",
                "jjones@gmail.com",
                Gender.MALE
        );
        // When
        underTest.addStudent(student);
        // Then
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);

    }

    @Test
    void itShouldThrowWhenEmailIsTaken() {
        // Given
        Student student = new Student(
                "Jon Jones",
                "jjones@gmail.com",
                Gender.MALE
        );
        given(studentRepository.selectExistsEmail(student.getEmail())).willReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " is taken");

        verify(studentRepository, never()).save(any());

    }

    @Test
    void itShouldDeleteStudent() {
        // Given
        given(studentRepository.existsById(1L)).willReturn(true);
        // When
        underTest.deleteStudent(1L);
        // Then
        verify(studentRepository).deleteById(1L);
    }

    @Test
    void itShouldThrowWhenDeletingStudentThatDoesNotExist() {
        // Given
        given(studentRepository.existsById(1L)).willReturn(false);
        // When
        // Then
        assertThatThrownBy(() -> underTest.deleteStudent(1L))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + 1L + " not found");

        verify(studentRepository, never()).delete(any());
    }
}