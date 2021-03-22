package com.ramanado.studentdb.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldSelectIfStudentEmailExists() {
        // Given
        String email = "jjones@gmail.com";
        Student student = new Student(
                "Jon Jones",
                email,
                Gender.MALE
        );
        underTest.save(student);
        // When
        Boolean exists = underTest.selectExistsEmail(email);
        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void itShouldNotSelectIfStudentEmailNotExists() {
        // Given
        String email = "jjones@gmail.com";
        // When
        Boolean exists = underTest.selectExistsEmail(email);
        // Then
        assertThat(exists).isFalse();
    }
}