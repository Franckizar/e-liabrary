// package com.example.security.Other.Application;

// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.Map;

// @RestController
// @RequestMapping("/api/v1/auth/applications")
// @RequiredArgsConstructor
// public class ApplicationController {

//     private final ApplicationService applicationService;

//      @PostMapping
//     public ResponseEntity<Application> createApplication(@RequestBody ApplicationRequest request) {
//         System.out.println("📥 Received application request...");
//         Application application = applicationService.createApplication(request);
//         System.out.println("📤 Responding with application ID: " + application.getId());
//         return ResponseEntity.ok(application);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Application> getApplicationById(@PathVariable Integer id) {
//         return applicationService.getApplicationById(id)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<Application> updateApplication(@PathVariable Integer id, @RequestBody Application application) {
//         try {
//             Application updated = applicationService.updateApplication(id, application);
//             return ResponseEntity.ok(updated);
//         } catch (IllegalArgumentException e) {
//             return ResponseEntity.notFound().build();
//         }
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteApplication(@PathVariable Integer id) {
//         try {
//             applicationService.deleteApplication(id);
//             return ResponseEntity.noContent().build();
//         } catch (IllegalArgumentException e) {
//             return ResponseEntity.notFound().build();
//         }
//     }

//     @GetMapping("/by-job/{jobId}")
//     public ResponseEntity<List<Application>> getByJob(@PathVariable Integer jobId) {
//         return ResponseEntity.ok(applicationService.getApplicationsByJobId(jobId));
//     }

//     @GetMapping("/by-jobseeker/{jobSeekerId}")
//     public ResponseEntity<List<Application>> getByJobSeeker(@PathVariable Integer jobSeekerId) {
//         return ResponseEntity.ok(applicationService.getApplicationsByJobSeekerId(jobSeekerId));
//     }

//     @GetMapping("/by-technician/{technicianId}")
//     public ResponseEntity<List<Application>> getByTechnician(@PathVariable Integer technicianId) {
//         return ResponseEntity.ok(applicationService.getApplicationsByTechnicianId(technicianId));
//     }


//     @GetMapping("/all")
//     public ResponseEntity<List<Application>> getAllApplications() {
//         List<Application> applications = applicationService.findAll();
//         return ResponseEntity.ok(applications);
//     }



//     @GetMapping("/by-status/{status}")
//     public ResponseEntity<List<Application>> getByStatus(@PathVariable String status) {
//         Application.ApplicationStatus applicationStatus;

//         try {
//             applicationStatus = Application.ApplicationStatus.valueOf(status.toUpperCase());
//         } catch (IllegalArgumentException e) {
//             return ResponseEntity.badRequest().build(); // Invalid status provided
//         }

//         List<Application> applications = applicationService.getApplicationsByStatus(applicationStatus);
//         return ResponseEntity.ok(applications);
//     }
//      @PatchMapping("/{id}/status")
//     public ResponseEntity<Application> updateApplicationStatus(
//             @PathVariable Integer id,
//             @RequestBody Map<String, String> statusUpdate) {

//         String statusStr = statusUpdate.get("status");
//         if (statusStr == null) {
//             return ResponseEntity.badRequest().build();
//         }

//         Application.ApplicationStatus newStatus;
//         try {
//             newStatus = Application.ApplicationStatus.valueOf(statusStr.toUpperCase());
//         } catch (IllegalArgumentException e) {
//             return ResponseEntity.badRequest().build();
//         }

//         try {
//             Application updated = applicationService.updateApplicationStatus(id, newStatus);
//             return ResponseEntity.ok(updated);
//         } catch (IllegalArgumentException e) {
//             return ResponseEntity.notFound().build();
//         }
    
// }



// }
