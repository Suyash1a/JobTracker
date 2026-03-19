package com.jobtracker.jobtracker.repository;

import com.jobtracker.jobtracker.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByJobIdOrderByRoundNumber(Long jobId);
}