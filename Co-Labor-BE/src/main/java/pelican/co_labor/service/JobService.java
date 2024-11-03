package pelican.co_labor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pelican.co_labor.domain.job.Job;
import pelican.co_labor.domain.job.JobEng;
import pelican.co_labor.dto.JobPostingDTO;
import pelican.co_labor.dto.JobUpdatedDTO;
import pelican.co_labor.repository.job.JobEngRepository;
import pelican.co_labor.repository.job.JobRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class JobService {

    private final JobRepository jobRepository;
    private final ObjectMapper objectMapper;
    private final JobEngRepository jobEngRepository;

    @Autowired
    public JobService(JobRepository jobRepository, ObjectMapper objectMapper, JobEngRepository jobEngRepository) {
        this.jobRepository = jobRepository;
        this.objectMapper = objectMapper;
        this.jobEngRepository = jobEngRepository;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<JobEng> getAllJobsEng() {
        return jobEngRepository.findAll();
    }

    public Optional<Job> getJobById(Long jobId) {
        return jobRepository.findById(jobId);
    }

    public Optional<Job> incrementAndGetJobById(Long jobId) {
        return jobRepository.findById(jobId).map(job -> {
            job.setViews(job.getViews() + 1);
            jobRepository.save(job);
            return job;
        });
    }

    public Optional<JobEng> incrementAndGetJobEngById(Long jobId) {
        return jobEngRepository.findById(jobId).map(job -> {
            job.setViews(job.getViews() + 1);
            jobEngRepository.save(job);
            return job;
        });
    }


    @Transactional
    public Job createJob(Job job, JobPostingDTO jobPostingDTO, MultipartFile image) {
        // 이미지 파일이 존재하면 저장
        if (image != null && !image.isEmpty()) {
            try {
                String imagePath = saveImage(image);
                job.setImageName(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }

        // DTO에서 데이터를 가져와 Job description에 삽입
        job.setDescription(generateJobPostingHtml(jobPostingDTO));

        return jobRepository.save(job);
    }

    @Transactional
    public Optional<Job> updateJob(Long jobId, JobUpdatedDTO jobUpdatedDTO, JobPostingDTO jobPostingDTO, MultipartFile image) {
        return jobRepository.findById(jobId).map(job -> {
            // JobUpdatedDTO에서 데이터를 가져와 Job 객체 업데이트
            if (jobUpdatedDTO.getTitle() != null) {
                job.setTitle(jobUpdatedDTO.getTitle());
            }
            if (jobUpdatedDTO.getRequirement() != null) {
                job.setRequirement(jobUpdatedDTO.getRequirement());
            }
            if (jobUpdatedDTO.getJobRole() != null) {
                job.setJobRole(jobUpdatedDTO.getJobRole());
            }
            if (jobUpdatedDTO.getExperience() != null) {
                job.setExperience(jobUpdatedDTO.getExperience());
            }
            if (jobUpdatedDTO.getEmploymentType() != null) {
                job.setEmploymentType(jobUpdatedDTO.getEmploymentType());
            }
            if (jobUpdatedDTO.getAddress1() != null) {
                job.setAddress1(jobUpdatedDTO.getAddress1());
            }
            if (jobUpdatedDTO.getAddress2() != null) {
                job.setAddress2(jobUpdatedDTO.getAddress2());
            }
            if (jobUpdatedDTO.getAddress3() != null) {
                job.setAddress3(jobUpdatedDTO.getAddress3());
            }
            if (jobUpdatedDTO.getSkills() != null) {
                job.setSkills(jobUpdatedDTO.getSkills());
            }
            if (jobUpdatedDTO.getDeadDate() != null) {
                job.setDeadDate(jobUpdatedDTO.getDeadDate());
            }

            // JobPostingDTO로부터 데이터를 가져와 Job description에 HTML 형태로 삽입
            job.setDescription(generateJobPostingHtml(jobPostingDTO));

            // 수정된 시간 업데이트
            job.setModified_at(LocalDateTime.now());

            // 이미지가 존재할 경우 처리
            if (image != null && !image.isEmpty()) {
                try {
                    // 기존 이미지 삭제
                    if (job.getImageName() != null) {
                        Files.deleteIfExists(Paths.get(job.getImageName()));
                    }
                    // 새로운 이미지 저장 후 경로 설정
                    String imagePath = saveImage(image);
                    job.setImageName(imagePath);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image", e);
                }
            }

            // 변경된 Job 객체 저장
            return Optional.of(jobRepository.save(job));
        }).orElse(Optional.empty());
    }

    public void deleteJob(Long jobId) {
        jobRepository.findById(jobId).ifPresent(job -> {
            try {
                Files.deleteIfExists(Paths.get(job.getImageName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            jobRepository.deleteById(jobId);
        });
    }

    private String saveImage(MultipartFile image) throws IOException {
        String imageDirectory = "path/to/save/images"; // 원하는 이미지 저장 경로 설정
        String imageName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path imagePath = Paths.get(imageDirectory, imageName);
        Files.createDirectories(imagePath.getParent());
        Files.write(imagePath, image.getBytes());
        return imageName; // 전체 경로 대신 파일 이름만 반환
    }

    public Job mapJobFromJson(String jobJson) throws IOException {
        return objectMapper.readValue(jobJson, Job.class);
    }

    @Transactional
    public void deleteExpiredJobs() {
        LocalDate today = LocalDate.now();
        jobRepository.deleteByDeadDateBefore(today);
    }

    public String generateJobPostingHtml(JobPostingDTO dto) {
        String htmlTemplate = """
                <sample>
                    <style>
                        #DetailContent {
                            padding: 20px;
                            max-width: 820px;
                            margin: 0 auto;
                        }
                        .con {
                            max-width: 800px;
                            margin: 0 auto;
                            background-color: #fff;
                        }
                        .main-title {
                            font-weight: bold;
                            font-size: 30px;
                            color: #222;
                        }
                        .section-title {
                            font-weight: bold;
                            font-size: 22px;
                            color: #222;
                            margin: 20px 10px;
                        }
                        .info-table {
                            width: 100%%;
                            font-size: 16px;
                            border-collapse: collapse;
                            margin-bottom: 30px;
                        }
                        .info-table th,
                        .info-table td {
                            padding: 15px;
                            border-bottom: 1px solid #dadada;
                            text-align: center;
                            background-color: #f5f5f5;
                        }
                        .info-table th {
                            width: 30%%;
                        }
                        .info-table td {
                            background-color: #fff;
                            width: 70%%;
                        }
                        .content {
                            display: flex;
                            flex-direction: column;
                            gap: 10px;
                            width: 100%%;
                        }
                        .wrapper {
                            display: flex;
                            flex-direction: column;
                            gap: 15px;
                            width: 100%%;
                        }
                    </style>
                
                    <div id="DetailContent" class="con">
                        <div class="wrapper">
                            <div class="content">
                                <div class="section-title">채용정보</div>
                                <table class="info-table">
                                    <tr>
                                        <th>업무내용</th>
                                        <td>%s</td>
                                    </tr>
                                    <tr>
                                        <th>지원자격</th>
                                        <td>%s</td>
                                    </tr>
                                    <tr>
                                        <th>우대사항</th>
                                        <td>%s</td>
                                    </tr>
                                    <tr>
                                        <th>접수방법</th>
                                        <td>%s</td>
                                    </tr>
                                </table>
                            </div>
                            <div class="content">
                                <div class="section-title">근무조건</div>
                                <table class="info-table">
                                    <tr>
                                        <th>근무요일</th>
                                        <td>%s</td>
                                    </tr>
                                    <tr>
                                        <th>근무시간</th>
                                        <td>%s</td>
                                    </tr>
                                    <tr>
                                        <th>근무기간</th>
                                        <td>%s</td>
                                    </tr>
                                    <tr>
                                        <th>급여</th>
                                        <td>%s</td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                </sample>
                """;

        // DTO에서 데이터를 가져와 HTML 템플릿에 삽입
        return String.format(
                htmlTemplate,
                dto.getJobDescription() != null ? dto.getJobDescription() : "-",
                dto.getApplicantRequirements() != null ? dto.getApplicantRequirements() : "-",
                dto.getPreferredQualifications() != null ? dto.getPreferredQualifications() : "-",
                dto.getApplicationMethod() != null ? dto.getApplicationMethod() : "-",
                dto.getWorkingDays() != null ? dto.getWorkingDays() : "-",
                dto.getWorkingHours() != null ? dto.getWorkingHours() : "-",
                dto.getWorkingPeriod() != null ? dto.getWorkingPeriod() : "-",
                dto.getSalary() != null ? dto.getSalary() : "-"
        );
    }

}
