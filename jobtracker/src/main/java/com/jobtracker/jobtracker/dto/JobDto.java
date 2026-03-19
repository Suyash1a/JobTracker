package com.jobtracker.jobtracker.dto;

import com.jobtracker.jobtracker.enums.ApplicationStatus;
import com.jobtracker.jobtracker.enums.InterviewType;
import com.jobtracker.jobtracker.enums.JobSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class JobDto {


    @Data
    public static class CreateJobRequest {
        @NotBlank(message = "Company is required")
        private String company;

        @NotBlank(message = "Role is required")
        private String role;

        private String jdLink;

        @NotNull(message = "Date applied is required")
        private LocalDate dateApplied;

        @NotNull(message = "Source is required")
        private JobSource source;

        private String notes;
        private LocalDate followUpDate;
    }

    @Data
    public static class UpdateStatusRequest {
        @NotNull(message = "Status is required")
        private ApplicationStatus status;
    }

    @Data
    public static class CreateInterviewRequest {
        @NotNull(message = "Round number is required")
        private Integer roundNumber;

        @NotNull(message = "Interview date is required")
        private LocalDate interviewDate;

        @NotNull(message = "Interview type is required")
        private InterviewType type;

        private String notes;
    }

    @Data
    public static class JobResponse {
        private Long id;
        private String company;
        private String role;
        private String jdLink;
        private LocalDate dateApplied;
        private JobSource source;
        private ApplicationStatus status;
        private String notes;
        private LocalDate followUpDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<InterviewResponse> interviews;
    }

    @Data
    public static class InterviewResponse {
        private Long id;
        private Integer roundNumber;
        private LocalDate interviewDate;
        private InterviewType type;
        private String notes;
        private LocalDateTime createdAt;
    }

    @Data
    public static class StatsResponse {
        private long totalApplied;
        private long shortlisted;
        private long interviews;
        private long offers;
        private long rejected;
        private double responseRate;
        private double offerRate;
    }
}