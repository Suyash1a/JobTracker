package com.jobtracker.jobtracker.controller;

import com.jobtracker.jobtracker.dto.JobDto;
import com.jobtracker.jobtracker.enums.ApplicationStatus;
import com.jobtracker.jobtracker.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // POST /api/jobs — Add a new job application
    @PostMapping
    public ResponseEntity<JobDto.JobResponse> createJob(
            @Valid @RequestBody JobDto.CreateJobRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.createJob(request));
    }

    // GET /api/jobs — List all jobs with optional filters
    @GetMapping
    public ResponseEntity<List<JobDto.JobResponse>> getAllJobs(
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String company
    ) {
        return ResponseEntity.ok(jobService.getAllJobs(status, from, to, company));
    }

    // GET /api/jobs/{id} — Get single job with interview history
    @GetMapping("/{id}")
    public ResponseEntity<JobDto.JobResponse> getJob(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJob(id));
    }

    // PUT /api/jobs/{id}/status — Update application status
    @PutMapping("/{id}/status")
    public ResponseEntity<JobDto.JobResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody JobDto.UpdateStatusRequest request) {
        return ResponseEntity.ok(jobService.updateStatus(id, request));
    }

    // POST /api/jobs/{id}/interview — Log an interview round
    @PostMapping("/{id}/interview")
    public ResponseEntity<JobDto.JobResponse> addInterview(
            @PathVariable Long id,
            @Valid @RequestBody JobDto.CreateInterviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.addInterview(id, request));
    }

    // GET /api/jobs/stats — Get aggregate stats
    @GetMapping("/stats")
    public ResponseEntity<JobDto.StatsResponse> getStats() {
        return ResponseEntity.ok(jobService.getStats());
    }
}