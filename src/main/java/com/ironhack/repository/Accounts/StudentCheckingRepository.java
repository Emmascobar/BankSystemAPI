package com.ironhack.repository.Accounts;

import com.ironhack.model.Accounts.StudentChecking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCheckingRepository extends JpaRepository<StudentChecking, Long> {
    StudentChecking findStudentCheckingById(Long id);

}