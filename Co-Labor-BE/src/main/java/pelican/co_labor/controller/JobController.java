package pelican.co_labor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pelican.co_labor.domain.enterprise_user.EnterpriseUser;
import pelican.co_labor.domain.job.Job;
import pelican.co_labor.domain.job.JobEng;
import pelican.co_labor.dto.JobPostingDTO;
import pelican.co_labor.dto.JobUpdatedDTO;
import pelican.co_labor.repository.enterprise_user.EnterpriseUserRepository;
import pelican.co_labor.service.AuthService;
import pelican.co_labor.service.JobService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Job", description = "채용 공고 관련 API")
@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;
    private final AuthService authService;
    private final EnterpriseUserRepository enterpriseUserRepository;

    @Autowired
    public JobController(JobService jobService, AuthService authService, EnterpriseUserRepository enterpriseUserRepository) {
        this.jobService = jobService;
        this.authService = authService;
        this.enterpriseUserRepository = enterpriseUserRepository;
    }

    @Operation(summary = "모든 채용 공고 조회", description = "등록된 모든 채용 공고를 조회합니다.")
    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @Operation(summary = "특정 채용 공고 조회", description = "채용 공고 ID로 특정 채용 공고를 조회합니다.")
    @GetMapping("/{job_id}")
    public ResponseEntity<Job> getJobById(
            @Parameter(description = "채용 공고 ID") @PathVariable("job_id") Long job_id) {
        Optional<Job> job = jobService.incrementAndGetJobById(job_id);
        return job.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "채용 공고 생성", description = "새로운 채용 공고를 생성합니다. JSON 형식의 공고 정보와 이미지 파일을 함께 업로드할 수 있습니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createJob(
            @Parameter(description = "채용 공고 정보") @RequestPart("job") Job job,
            @Parameter(description = "채용 공고 본문") @RequestPart("jobPostingDTO") JobPostingDTO jobPostingDTO,
            @Parameter(description = "채용 공고 이미지 파일") @RequestPart(value = "image", required = false) MultipartFile image,
            HttpServletRequest httpServletRequest) {

        Map<String, Object> response = new HashMap<>();
        String jsessionId = extractJSessionIdFromCookie(httpServletRequest.getCookies());

        // 세션에서 JSESSIONID를 찾을 수 없으면 401 UNAUTHORIZED 응답 반환
        if (jsessionId == null) {
            response.put("message", "No JSESSIONID found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // 세션에서 가져온 userType이 enterprise_user가 아니면 403 FORBIDDEN 응답 반환
        Optional<Map<String, Object>> currentUser = authService.getCurrentUser(jsessionId);
        if (currentUser.isEmpty() || !"enterprise_user".equals(currentUser.get().get("userType"))) {
            response.put("message", "기업 회원이 아닙니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        EnterpriseUser enterpriseUser = enterpriseUserRepository.findByEnterpriseUserId((String) currentUser.get().get("username"));
        // 사용자의 기업 정보가 없으면 404 NOT FOUND 응답 반환
        if (enterpriseUser.getEnterprise() == null) {
            response.put("message", "회원의 기업 정보가 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        job.setEnterpriseUser(enterpriseUser);
        job.setEnterprise(enterpriseUser.getEnterprise());

        // 채용 공고 생성
        Job createdJob = jobService.createJob(job, jobPostingDTO, image);
        response.put("message", "채용 공고가 성공적으로 생성되었습니다.");
        response.put("job", createdJob);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "채용 공고 수정", description = "채용 공고 ID에 해당하는 채용 공고를 수정합니다.")
    @PatchMapping(value = "/{job_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> updateJob(
            @Parameter(description = "채용 공고 ID") @PathVariable("job_id") Long job_id,
            @Parameter(description = "수정할 채용 공고 정보") @RequestPart("jobUpdatedDTO") JobUpdatedDTO jobUpdatedDTO,
            @Parameter(description = "수정할 채용 공고 본문") @RequestPart("jobPostingDTO") JobPostingDTO jobPostingDTO,
            @Parameter(description = "수정할 이미지 파일") @RequestPart(value = "image", required = false) MultipartFile image,
            HttpServletRequest httpServletRequest) {

        Map<String, Object> response = new HashMap<>();
        String jsessionId = extractJSessionIdFromCookie(httpServletRequest.getCookies());
        if (jsessionId == null) {
            response.put("message", "No JSESSIONID found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Optional<Map<String, Object>> currentUser = authService.getCurrentUser(jsessionId);
        Optional<Job> jobOptional = jobService.getJobById(job_id);
        if (currentUser.isEmpty()) {
            response.put("message", "로그인 후 다시 시도해주세요.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        if (!"enterprise_user".equals(currentUser.get().get("userType"))) {
            response.put("message", "기업 사용자만 수정할 수 있습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        if (jobOptional.isEmpty()) {
            response.put("message", "수정할 채용 공고가 존재하지 않습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (!jobOptional.get().getEnterpriseUser().getEnterprise_user_id().equals(currentUser.get().get("username"))) {
            response.put("message", "채용 공고 수정 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        // 수정된 Job 객체와 이미지 파일을 사용하여 업데이트
        Optional<Job> updatedJob = jobService.updateJob(job_id, jobUpdatedDTO, jobPostingDTO, image);

        return updatedJob.map(job1 -> {
            response.put("message", "채용 공고가 성공적으로 수정되었습니다.");
            response.put("job", job1);
            return ResponseEntity.ok(response);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @Operation(summary = "채용 공고 삭제", description = "채용 공고 ID에 해당하는 채용 공고를 삭제합니다.")
    @DeleteMapping("/{job_id}")
    public ResponseEntity<Map<String, Object>> deleteJob(
            @Parameter(description = "채용 공고 ID") @PathVariable("job_id") Long job_id,
            HttpServletRequest httpServletRequest) {
        Map<String, Object> response = new HashMap<>();
        String jsessionId = extractJSessionIdFromCookie(httpServletRequest.getCookies());
        if (jsessionId == null) {
            response.put("message", "No JSESSIONID found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Optional<Map<String, Object>> currentUser = authService.getCurrentUser(jsessionId);
        // 세션에서 가져온 username이 채용 공고의 enterprise_user_id와 일치하지 않으면 403 FORBIDDEN 응답 반환
        Optional<Job> jobOptional = jobService.getJobById(job_id);
        if (currentUser.isEmpty() || !"enterprise_user".equals(currentUser.get().get("userType")) || jobOptional.isEmpty() || !jobOptional.get().getEnterpriseUser().getEnterprise_user_id().equals(currentUser.get().get("username"))) {
            response.put("message", "채용 공고를 삭제할 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        jobService.deleteJob(job_id);
        response.put("message", "채용 공고가 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "채용 공고 이미지 파일 제공", description = "서버에 저장된 채용 공고 이미지를 제공합니다.")
    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(
            @Parameter(description = "이미지 파일 이름") @PathVariable String filename) {
        try {
            Path filePath = Paths.get("path/to/save/images").resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);

                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, contentType)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "영어로 모든 채용 공고 조회", description = "영어로 등록된 모든 채용 공고를 조회합니다.")
    @GetMapping("/eng")
    public List<JobEng> getAllJobsEng() {
        return jobService.getAllJobsEng();
    }

    @Operation(summary = "영어로 특정 채용 공고 조회", description = "영어로 된 채용 공고 ID로 특정 채용 공고를 조회합니다.")
    @GetMapping("/eng/{job_id}")
    public ResponseEntity<JobEng> getJobEngById(
            @Parameter(description = "영어 채용 공고 ID") @PathVariable("job_id") Long job_id) {
        Optional<JobEng> job = jobService.incrementAndGetJobEngById(job_id);
        return job.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 쿠키에서 JSESSIONID 추출하는 헬퍼 메서드
    private String extractJSessionIdFromCookie(Cookie[] cookies) {
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if ("JSESSIONID".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
