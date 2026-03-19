package com.jobtracker.jobtracker.service;

import com.jobtracker.jobtracker.dto.JobDto;
import com.jobtracker.jobtracker.entity.Interview;
import com.jobtracker.jobtracker.entity.Job;
import com.jobtracker.jobtracker.enums.ApplicationStatus;
import com.jobtracker.jobtracker.repository.InterviewRepository;
import com.jobtracker.jobtracker.repository.JobRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final InterviewRepository interviewRepository;

    // ── Create Job ────────────────────────────────────────────────────────────
    @Transactional
    public JobDto.JobResponse createJob(JobDto.CreateJobRequest request) {
        Job job = Job.builder()
                .company(request.getCompany())
                .role(request.getRole())
                .jdLink(request.getJdLink())
                .dateApplied(request.getDateApplied())
                .source(request.getSource())
                .notes(request.getNotes())
                .followUpDate(request.getFollowUpDate())
                .status(ApplicationStatus.Applied)
                .build();

        return toJobResponse(jobRepository.save(job));
    }

    // ── Get All Jobs ──────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<JobDto.JobResponse> getAllJobs(ApplicationStatus status, LocalDate from, LocalDate to, String company) {
        List<Job> jobs;

        if (status != null && from != null && to != null) {
            jobs = jobRepository.findByStatusAndDateAppliedBetween(status, from, to);
        } else if (status != null) {
            jobs = jobRepository.findByStatus(status);
        } else if (from != null && to != null) {
            jobs = jobRepository.findByDateAppliedBetween(from, to);
        } else if (company != null && !company.isBlank()) {
            jobs = jobRepository.findByCompanyContainingIgnoreCase(company);
        } else {
            jobs = jobRepository.findAll();
        }

        return jobs.stream().map(this::toJobResponse).collect(Collectors.toList());
    }

    // ── Get Single Job ────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public JobDto.JobResponse getJob(Long id) {
        return toJobResponse(findJobById(id));
    }

    // ── Update Status ─────────────────────────────────────────────────────────
    @Transactional
    public JobDto.JobResponse updateStatus(Long id, JobDto.UpdateStatusRequest request) {
        Job job = findJobById(id);
        job.setStatus(request.getStatus());
        return toJobResponse(jobRepository.save(job));
    }

    // ── Log Interview ─────────────────────────────────────────────────────────
    @Transactional
    public JobDto.JobResponse addInterview(Long jobId, JobDto.CreateInterviewRequest request) {
        Job job = findJobById(jobId);

        Interview interview = Interview.builder()
                .job(job)
                .roundNumber(request.getRoundNumber())
                .interviewDate(request.getInterviewDate())
                .type(request.getType())
                .notes(request.getNotes())
                .build();

        interviewRepository.save(interview);

        // Auto-advance status to INTERVIEW if still at APPLIED or SHORTLISTED
        if (job.getStatus() == ApplicationStatus.Applied
                || job.getStatus() == ApplicationStatus.Shortlisted) {
            job.setStatus(ApplicationStatus.Interview);
            jobRepository.save(job);
        }

        return toJobResponse(findJobById(jobId));
    }

    @Transactional(readOnly = true)
    public JobDto.StatsResponse getStats() {
        long total = jobRepository.count();
        long shortlisted = jobRepository.countByStatus(ApplicationStatus.Shortlisted);
        long interviews = jobRepository.countByStatus(ApplicationStatus.Interview);
        long offers = jobRepository.countByStatus(ApplicationStatus.Offer);
        long rejected = jobRepository.countByStatus(ApplicationStatus.Rejected);
        long responded = jobRepository.countResponded();

        JobDto.StatsResponse stats = new JobDto.StatsResponse();
        stats.setTotalApplied(total);
        stats.setShortlisted(shortlisted);
        stats.setInterviews(interviews);
        stats.setOffers(offers);
        stats.setRejected(rejected);
        stats.setResponseRate(total > 0 ? (double) responded / total * 100 : 0);
        stats.setOfferRate(total > 0 ? (double) offers / total * 100 : 0);

        return stats;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private Job findJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with id: " + id));
    }

    private JobDto.JobResponse toJobResponse(Job job) {
        JobDto.JobResponse response = new JobDto.JobResponse();
        response.setId(job.getId());
        response.setCompany(job.getCompany());
        response.setRole(job.getRole());
        response.setJdLink(job.getJdLink());
        response.setDateApplied(job.getDateApplied());
        response.setSource(job.getSource());
        response.setStatus(job.getStatus());
        response.setNotes(job.getNotes());
        response.setFollowUpDate(job.getFollowUpDate());
        response.setCreatedAt(job.getCreatedAt());
        response.setUpdatedAt(job.getUpdatedAt());

        List<JobDto.InterviewResponse> interviews = job.getInterviews().stream()
                .map(this::toInterviewResponse)
                .collect(Collectors.toList());
        response.setInterviews(interviews);

        return response;
    }

    private JobDto.InterviewResponse toInterviewResponse(Interview interview) {
        JobDto.InterviewResponse response = new JobDto.InterviewResponse();
        response.setId(interview.getId());
        response.setRoundNumber(interview.getRoundNumber());
        response.setInterviewDate(interview.getInterviewDate());
        response.setType(interview.getType());
        response.setNotes(interview.getNotes());
        response.setCreatedAt(interview.getCreatedAt());
        return response;
    }
}