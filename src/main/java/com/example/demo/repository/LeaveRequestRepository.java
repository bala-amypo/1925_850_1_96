package com.example.demo.repository;

import com.example.demo.model.EmployeeProfile;
import com.example.demo.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployee(EmployeeProfile employee);

    @Query("SELECT l FROM LeaveRequest l WHERE l.employee.teamName = ?1 AND l.status = 'APPROVED' " +
           "AND l.startDate <= ?3 AND l.endDate >= ?2")
    List<LeaveRequest> findApprovedOverlappingForTeam(String teamName, LocalDate start, LocalDate end);

    @Query("SELECT l FROM LeaveRequest l WHERE l.status = 'APPROVED' AND ?1 BETWEEN l.startDate AND l.endDate")
    List<LeaveRequest> findApprovedOnDate(LocalDate date);
}