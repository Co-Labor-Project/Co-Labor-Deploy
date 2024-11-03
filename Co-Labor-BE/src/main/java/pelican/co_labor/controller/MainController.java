package pelican.co_labor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pelican.co_labor.domain.job.Job;
import pelican.co_labor.domain.review.Review;
import pelican.co_labor.repository.job.JobRepository;
import pelican.co_labor.repository.review.ReviewRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Main", description = "메인 페이지 데이터 관련 API")
@RestController
@RequestMapping("/api/main")
public class MainController {

    private final JobRepository jobRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public MainController(JobRepository jobRepository, ReviewRepository reviewRepository) {
        this.jobRepository = jobRepository;
        this.reviewRepository = reviewRepository;
    }

    @Operation(summary = "메인 페이지 데이터 조회", description = "메인 페이지에 필요한 채용 공고와 리뷰 데이터를 조회합니다.")
    @GetMapping
    public Map<String, Object> getMainPageData() {

        Map<String, Object> response = new HashMap<>();

        List<Job> jobs = jobRepository.findAll();
        List<Review> reviews = reviewRepository.findAll();

        response.put("jobs", jobs);
        response.put("reviews", reviews);

        return response;
    }
}
