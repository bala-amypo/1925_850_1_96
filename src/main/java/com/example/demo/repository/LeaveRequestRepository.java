package com.example.demo.repository;

import com.example.demo.model.LeaveRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository
        extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployee(
            com.example.demo.model.EmployeeProfile employee
    );

    @Query("""
        SELECT lr FROM LeaveRequest lr
        WHERE lr.employee.teamName = :teamName
          AND lr.status = 'APPROVED'
          AND lr.startDate <= :endDate
          AND lr.endDate >= :startDate
    """)
    List<LeaveRequest> findApprovedOverlappingForTeam(
            @Param("teamName") String teamName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
