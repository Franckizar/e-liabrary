// package com.example.security.Other.Application;

// import com.example.security.Other.Job.Job;
// import com.example.security.Other.Job.JobRepository;
// import com.example.security.user.JobSeeker.JobSeeker;
// import com.example.security.user.JobSeeker.JobSeekerRepository;
// import com.example.security.user.Technicien.Technician;
// import com.example.security.user.Technicien.TechnicianRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.Optional;

// @Service
// @RequiredArgsConstructor
// public class ApplicationService {

//     private final ApplicationRepository applicationRepository;
//     private final JobRepository jobRepository;
//     private final JobSeekerRepository jobSeekerRepository;
//     private final TechnicianRepository technicianRepository;

// public Application createApplication(ApplicationRequest request) {

//         System.out.println("=== Starting Application Creation ===");

//         // Step 1: Get and validate job
//         System.out.println("Received jobId: " + request.getJobId());
//         Job job = jobRepository.findById(request.getJobId())
//                 .orElseThrow(() -> new RuntimeException("Job not found with ID: " + request.getJobId()));
//         System.out.println("✔️ Job found: " + job.getTitle());

//         Application application = new Application();
//         application.setJob(job);

//         // Step 2: Get and set applicant (either job seeker or technician)
//         if (request.getJobSeekerId() != null) {
//             System.out.println("Received jobSeekerId: " + request.getJobSeekerId());
//             JobSeeker jobSeeker = jobSeekerRepository.findById(request.getJobSeekerId())
//                     .orElseThrow(() -> new RuntimeException("JobSeeker not found with ID: " + request.getJobSeekerId()));
//             application.setJobSeeker(jobSeeker);
//             System.out.println("✔️ Job seeker set: " + jobSeeker.getFullName());
//         } else if (request.getTechnicianId() != null) {
//             System.out.println("Received technicianId: " + request.getTechnicianId());
//             Technician technician = technicianRepository.findById(request.getTechnicianId())
//                     .orElseThrow(() -> new RuntimeException("Technician not found with ID: " + request.getTechnicianId()));
//             application.setTechnician(technician);
//             System.out.println("✔️ Technician set: " + technician.getFullName());
//         } else {
//             throw new RuntimeException("❌ Either JobSeekerId or TechnicianId must be provided.");
//         }

//         // Step 3: Set resume, cover letter, portfolio
//         application.setResumeUrl(request.getResumeUrl());
//         application.setCoverLetter(request.getCoverLetter());
//         application.setPortfolioUrl(request.getPortfolioUrl());

//         // Step 4: Save
//         Application saved = applicationRepository.save(application);
//         System.out.println("✅ Application saved with ID: " + saved.getId());
//         return saved;
//     }

//     public Application updateApplication(Integer id, Application updatedApplication) {
//         Application existing = applicationRepository.findById(id)
//                 .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + id));

//         if (updatedApplication.getStatus() != null) {
//             existing.setStatus(updatedApplication.getStatus());
//         }
//         if (updatedApplication.getCoverLetter() != null) {
//             existing.setCoverLetter(updatedApplication.getCoverLetter());
//         }
//         if (updatedApplication.getResumeUrl() != null) {
//             existing.setResumeUrl(updatedApplication.getResumeUrl());
//         }
//         if (updatedApplication.getPortfolioUrl() != null) {
//             existing.setPortfolioUrl(updatedApplication.getPortfolioUrl());
//         }

//         return applicationRepository.save(existing);
//     }

//     public Optional<Application> getApplicationById(Integer id) {
//         return applicationRepository.findById(id);
//     }

//     public List<Application> getApplicationsByJobId(Integer jobId) {
//         return applicationRepository.findByJob_Id(jobId);
//     }

//     public List<Application> getApplicationsByJobSeekerId(Integer jobSeekerId) {
//         return applicationRepository.findByJobSeeker_Id(jobSeekerId);
//     }

//     public List<Application> getApplicationsByTechnicianId(Integer technicianId) {
//         return applicationRepository.findByTechnician_Id(technicianId);
//     }

//     public void deleteApplication(Integer id) {
//         if (!applicationRepository.existsById(id)) {
//             throw new IllegalArgumentException("Application not found with ID: " + id);
//         }
//         applicationRepository.deleteById(id);
//     }

//     public List<Application> findAll() {
//     return applicationRepository.findAll();
// }



//  public List<Application> getApplicationsByStatus(Application.ApplicationStatus status) {
//         return applicationRepository.findByStatus(status);
//     }


//         public Application updateApplicationStatus(Integer id, Application.ApplicationStatus newStatus) {
//         Application application = applicationRepository.findById(id)
//                 .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + id));
//         application.setStatus(newStatus);
//         return applicationRepository.save(application);
//     }

// }
