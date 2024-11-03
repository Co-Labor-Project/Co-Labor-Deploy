//package pelican.co_labor.controller;
//
//import org.json.JSONException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import pelican.co_labor.domain.enterprise.Enterprise;
//import pelican.co_labor.domain.job.Job;
//import pelican.co_labor.domain.review.Review;
//import pelican.co_labor.service.Search.KeywordSearchService;
//import pelican.co_labor.service.Search.OpenAiService;
//import pelican.co_labor.service.Search.SearchService;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RestController
//public class AISearchController {
//
//    private static final Logger logger = LoggerFactory.getLogger(AISearchController.class);
//
//    @Autowired
//    private KeywordSearchService keywordSearchService;
//
//    @Autowired
//    private SearchService searchService;
//
//    @Autowired
//    private OpenAiService openAiService;
//
//    @GetMapping("/ai-search")
//    public Map<String, Object> search(@RequestParam String sentence) throws JSONException {
//        Map<String, Object> response = new HashMap<>();
//
//        // 문장을 키워드로 변환
//        List<String> keywords = openAiService.extractKeywords(sentence);
//        logger.info("Extracted keywords In Controller: {}", keywords);
//
//        if (keywords.isEmpty()) {
//            response.put("message", "No keywords extracted from the given sentence.");
//            return response;
//        }
//
//        // 유사 키워드 검색
//        List<String> similarKeywords = keywords.stream()
//                .flatMap(keyword -> keywordSearchService.searchSimilarWords(keyword).stream())
//                .distinct()
//                .collect(Collectors.toList());
//        logger.info("Similar keywords: {}", similarKeywords);
//
//        if (similarKeywords.isEmpty()) {
//            response.put("message", "No similar words found for the given keywords in the database.");
//            return response;
//        }
//
//        // 키워드를 통해 DB 검색
//        List<Job> jobs = searchService.searchJobs(similarKeywords);
//        List<Review> reviews = searchService.searchReviews(similarKeywords);
//        List<Enterprise> enterprises = searchService.searchEnterprises(similarKeywords);
//
//        response.put("jobs", jobs);
//        response.put("reviews", reviews);
//        response.put("enterprises", enterprises);
//
//        return response;
//    }
//}
