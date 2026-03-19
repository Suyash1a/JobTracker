package com.jobtracker.jobtracker.repository;

import com.jobtracker.jobtracker.entity.Job;
import com.jobtracker.jobtracker.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByStatus(ApplicationStatus status);

    List<Job> findByCompanyContainingIgnoreCase(String company);

    List<Job> findByDateAppliedBetween(LocalDate from, LocalDate to);

    List<Job> findByStatusAndDateAppliedBetween(ApplicationStatus status, LocalDate from, LocalDate to);

    @Query("SELECT COUNT(j) FROM Job j WHERE j.status = :status")
    long countByStatus(@Param("status") ApplicationStatus status);

    @Query("SELECT COUNT(j) FROM Job j WHERE j.status != 'APPLIED'")
    long countResponded();
}