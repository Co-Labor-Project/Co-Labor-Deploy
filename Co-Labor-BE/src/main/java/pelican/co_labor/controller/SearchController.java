package pelican.co_labor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pelican.co_labor.domain.enterprise.Enterprise;
import pelican.co_labor.domain.job.Job;
import pelican.co_labor.domain.review.Review;
import pelican.co_labor.service.Search.SearchService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Search", description = "검색 관련 API")
@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Operation(summary = "키워드 기반 검색", description = "키워드를 기반으로 기업, 채용 공고, 리뷰를 검색합니다.")
    @GetMapping
    public Map<String, Object> search(
            @Parameter(description = "검색에 사용할 키워드") @RequestParam String keyword) {
        Map<String, Object> response = new HashMap<>();

        List<Job> jobs = searchService.searchJobs(keyword);
        List<Review> reviews = searchService.searchReviews(keyword);
        List<Enterprise> enterprises = searchService.searchEnterprises(keyword);

        response.put("jobs", jobs);
        response.put("reviews", reviews);
        response.put("enterprises", enterprises);

        System.out.println("response = " + response);

        return response;
    }
}
